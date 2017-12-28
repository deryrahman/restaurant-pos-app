$(document).ready(function() {

    var config;
    var baseUrl;
    var loginUrl;

    var configUrl = "configurations.json";
    $.getJSON(configUrl, function (data) {
        config = data;
        baseUrl = config.baseUrl;
        loginUrl = baseUrl + config.endpoints.login;
        registerIdentityUrl = baseUrl + config.endpoints.register;
        registerUserUrl = baseUrl + config.endpoints.user;
    });

    $('#login-form').submit(function (e) {
        var data = {
            username: $(this).find("#username").val(),
            password: $(this).find("#password").val()
        };

        $.ajax(loginUrl, {
            method: "POST",
            contentType: "application/x-www-form-urlencoded",
            data: data,
            success: successfulLogin,
            statusCode: {
                401: invalidLogin
            },
            error: errorHandler
        });
        e.preventDefault();
    });

    var successfulLogin = function (token) {
        Cookies.set('POSRESTAURANT', token);
        console.log(token);

        var parserUrl = baseUrl + config.endpoints.parser;
        var userInfo;
        $.ajax(parserUrl, {
            method: "POST",
            url: parserUrl,
            contentType: "application/x-www-form-urlencoded",
            data: {token: token},
            success: function (response) {
                userInfo = response.payload;
                if (userInfo.role === "cashier") {
                    window.location.assign(config.pages.home);
                } else if (userInfo.role === "admin") {
                    window.location.assign(config.pages.admin);
                } else if (userInfo.role === "manager") {
                    window.location.assign(config.pages.manager);
                }
                console.log(userInfo);
            }
        });
    };

    var invalidLogin = function () {
        $('#credential-warning').show();
    };

    var errorHandler = function (jqXHR) {
        if (jqXHR.status !== 401) {
            alert(jqXHR.responseText);
        }
    };

});
