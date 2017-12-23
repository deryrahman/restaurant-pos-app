$(document).ready(function() {

    var config;
    var loginUrl;
    var registerUserUrl;
    var registerIdentityUrl;
    var userToken;

    function successfulLogin(token) {
        alert(token);
    }

    function invalidLogin() {
        $('#credential-warning').show();
    }

    function errorHandler(jqXHR) {
        if (jqXHR.status !== 401) {
            alert(jqXHR.responseText);
        }
    }

    var configUrl = "configurations.json";
    $.getJSON(configUrl, function (data) {
        config = data;
        var baseUrl = config.baseUrl;
        loginUrl = baseUrl + config.endpoints.login;
        registerIdentityUrl = baseUrl + config.endpoints.registerIdentity;
        registerUserUrl = baseUrl + config.endpoints.registerUser;
    });

    $('#login-form-link').click(function(e) {
        $("#login-form").delay(100).fadeIn(100);
        $("#register-form").fadeOut(100);
        $('#register-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });
    $('#register-form-link').click(function(e) {
        $("#register-form").delay(100).fadeIn(100);
        $("#login-form").fadeOut(100);
        $('#login-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
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

    $('#register-form').submit(function (e) {
        $('.form-control').each(function () {
           console.log($(this).attr("name"));
        });
        e.preventDefault();
    });
});
