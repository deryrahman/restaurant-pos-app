var config;
var baseUrl;
var loginUrl;
var registerUserUrl;
var registerIdentityUrl;

var userInfo;
var userId;
var restaurantId;
var userRole;

var coreService = "http://localhost:8080/restaurant"
var authService = "http://localhost:8080/auth"

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

function getCookie(cookiename) {
    var cookiestring=RegExp(""+cookiename+"[^;]+").exec(document.cookie);
    return decodeURIComponent(!!cookiestring ? cookiestring.toString().replace(/^[^=]+./,"") : "");
}

var token = getCookie('POSRESTAURANT')

if(!token){
    window.location.assign(config.pages.login);
} else {
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
            setUserInformation();
        },
        error: function(){
            window.location.assign(config.pages.login);
        }
    });
}

function setUserInformation(){
    getJSON(false,coreService+"/users/"+userId, function (data) {
        var payload = data["payload"]
        userInfo = payload
        restaurantId = payload["restaurantId"]
    })
}

function loadErr(str) {
    return "Load error "+str;
}

function getJSON(async, url, callback){
    $.ajax({
        type: 'GET',
        url: url,
        dataType: 'json',
        async: async,
        success: function (data) {
            callback(data)
        },
        error: function (e) {
            console.log(e.responseJSON.message);
            alert("Error GET Request!")
        }
    });
}
function postJSON(async, url, data, callback){
    $.ajax(url, {
        data : JSON.stringify(data),
        contentType : 'application/json',
        type : 'POST',
        async : async,
        success: function (data){
            callback(data)
        },
        error: function (e) {
            console.log(e.responseJSON.message);
            alert("Error POST Request!")
        }
    });
}
function putJSON(async, url, data, callback){
    $.ajax(url, {
        data : JSON.stringify(data),
        contentType : 'application/json',
        type : 'PUT',
        async : async,
        success: function (data){
            callback(data)
        },
        error: function (e) {
            console.log(e.responseJSON.message);
            alert("Error PUT Request!")
        }
    });
}
function deleteJSON(async, url, callback){
    $.ajax(url, {
        contentType : 'application/json',
        type : 'DELETE',
        async : async,
        success: function (data){
            callback(data)
        },
        error: function (e) {
            console.log(e.responseJSON.message);
            alert("Error PUT Request!")
        }
    });
}