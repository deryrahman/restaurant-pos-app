var config;
var baseUrl;
var loginUrl;
var registerUserUrl;
var registerIdentityUrl;

var userInfo;
var userId;
var restaurantId;
var userRole;

var configUrl = "configurations.json";
$.ajax(configUrl, {
    method: "GET",
    url: configUrl,
    contentType: "application/json",
    async: false,
    success: function (data) {
        config = data;
        baseUrl = config.baseUrl;
        loginUrl = baseUrl + config.endpoints.login;
        registerIdentityUrl = baseUrl + config.endpoints.register;
        registerUserUrl = baseUrl + config.endpoints.user;
    }
});

var authService = "http://localhost:8080/auth"
var coreService = "http://localhost:8080/restaurant"
var token = Cookies.get('POSRESTAURANT')

if(!token){
    window.location.assign(config.pages.login);
}
var userInfo;
$.ajax(authService+"/parseToken", {
    method: "POST",
    url: authService+"/parseToken",
    contentType: "application/x-www-form-urlencoded",
    async:false,
    data: {token: token},
    success: function (response) {
        userInfo = response.payload;
        userId = userInfo["id"]
        userRole = userInfo["role"]
    },
    error: function(){
        window.location.assign(config.pages.login);
    }
});

$.ajax(coreService+"/users/"+userId, {
    method: "GET",
    url: coreService+"/users/"+userId,
    dataType: "json",
    async:false,
    success: function (data) {
        var payload = data["payload"]
        restaurantId = payload["restaurantId"]
    },
    error: function(){
        alert("Restaurant id fail to invoke")
    }
});