$(document).ready(function () {

    var token = Cookies.get("POSRESTAURANT");
    var config;
    var baseUrl;
    var parserUrl;
    var userServiceUrl;
    var itemServiceUrl;
    var categoryServiceUrl;
    var restaurantServiceUrl;

    (function () {
        if (token === undefined) {
            backToLoginPage("You are not logged in. Please login.");
        }

        var configUrl = "configurations.json";
        $.getJSON(configUrl, function (data) {
            config = data;
            baseUrl = config.baseUrl;
            parserUrl = baseUrl + config.endpoints.parser;
            userServiceUrl = baseUrl + config.endpoints.user;
            itemServiceUrl = baseUrl + config.endpoints.item;
            categoryServiceUrl = baseUrl + config.endpoints.category;
            restaurantServiceUrl = baseUrl + config.endpoints.restaurant;
        }).done(function () {
            $.ajax(parserUrl, {
                method: "POST",
                contentType: "application/x-www-form-urlencoded",
                data: {token: token}
            }).fail(function () {
                backToLoginPage("Your session is already expired. Please login again.");
            });
        });
    })();

    function backToLoginPage(message) {
        alert(message);
        window.location.replace(config.pages.login);
    }

    $('#show-users').click(function () {
        $('#panel-overview').hide();
        $('#panel-items').hide();
        $('#panel-users').show();

        $('#show-overview').removeClass('active main-bg-color');
        $('#show-items').removeClass('active main-bg-color');
        $('#show-users').addClass('active main-bg-color');

    });

    $('#show-overview').click(function () {
        $('#panel-users').hide();
        $('#panel-items').hide();
        $('#panel-overview').show();

        $('#show-users').removeClass('active main-bg-color');
        $('#show-items').removeClass('active main-bg-color');
        $('#show-overview').addClass('active main-bg-color');
    });

    $('#show-items').click(function () {
        $('#panel-users').hide();
        $('#panel-overview').hide();
        $('#panel-items').show();

        $('#show-users').removeClass('active main-bg-color');
        $('#show-overview').removeClass('active main-bg-color');
        $('#show-items').addClass('active main-bg-color');
    });
});