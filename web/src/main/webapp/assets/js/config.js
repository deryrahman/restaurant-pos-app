var config;
var baseUrl;
var loginUrl;
var registerUserUrl;
var registerIdentityUrl;

var configUrl = "configurations.json";
$.getJSON(configUrl, function (data) {
    config = data;
    baseUrl = config.baseUrl;
    loginUrl = baseUrl + config.endpoints.login;
    registerIdentityUrl = baseUrl + config.endpoints.register;
    registerUserUrl = baseUrl + config.endpoints.user;
});