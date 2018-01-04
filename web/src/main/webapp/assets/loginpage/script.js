$(document).ready(function() {

    var config;
    var baseUrl;
    var serviceUrl;
    var loginUrl;
    var parserUrl;

    var configUrl = "configurations.json";
    $.getJSON(configUrl, function (data) {
        config = data;
        baseUrl = config.baseUrl;
        serviceUrl = config.serviceUrl;
        loginUrl = serviceUrl + config.endpoints.login;
        parserUrl = serviceUrl + config.endpoints.parser;
    });

    $('#login-form').submit(function (e) {
        $("#overlay").show();

        var data = {
            username: $(this).find("#username").val(),
            password: $(this).find("#password").val()
        };

        $.ajax(loginUrl, {
            method: "POST",
            contentType: "application/x-www-form-urlencoded",
            data: data
        }).done(function (token) {
            Cookies.set('POSRESTAURANT', token);

        }).then(function (token) {
            return $.ajax(parserUrl, {
                method: "POST",
                contentType: "application/x-www-form-urlencoded",
                data: {token: token}
            });

        }).then(function (response) {
            userInfo = response.payload;
            if (userInfo.role === "cashier") {
                window.location.assign(config.pages.home);
            } else if (userInfo.role === "admin") {
                window.location.assign(config.pages.admin);
            } else if (userInfo.role === "manager") {
                window.location.assign(config.pages.manager);
            }

        }).fail(function (jqXHR) {
            $("#overlay").hide();
            if (jqXHR.status === 401) {
                $('#credential-warning').show();
            } else {
                alert(jqXHR.responseText);
            }
        });
        e.preventDefault();
    });

});
