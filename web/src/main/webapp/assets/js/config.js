var config;
var baseUrl;
var loginUrl;
var registerUserUrl;
var registerIdentityUrl;

var userId;
var restaurantId;

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