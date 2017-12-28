$(document).ready(function () {

    var token = Cookies.get("POSRESTAURANT");
    var config;
    var baseUrl;
    var serviceUrls = {};
    var userData = {};

    (function () {
        if (token === undefined) {
            backToLoginPage("You are not logged in. Please login.");
        }

        var configUrl = "configurations.json";
        $.getJSON(configUrl, function (data) {
            config = data;
            baseUrl = config.baseUrl;
            serviceUrls.parser = baseUrl + config.endpoints.parser;
            serviceUrls.user = baseUrl + config.endpoints.user;
            serviceUrls.item = baseUrl + config.endpoints.item;
            serviceUrls.category = baseUrl + config.endpoints.category;
            serviceUrls.restaurant = baseUrl + config.endpoints.restaurant;
        }).done(function () {
            $.ajax(serviceUrls.parser, {
                method: "POST",
                contentType: "application/x-www-form-urlencoded",
                data: {token: token}
            })
                .success(function (data) {
                    userData = data.payload;
                    if (userData.role === "admin") {
                        console.log("Logged in as an admin.");
                        initializeAdminPage();
                    } else {
                        backToLoginPage("You are not logged in as an admin. Please login as an admin.");
                    }
                })
                .fail(function () {
                    backToLoginPage("Your session is already expired. Please login again.");
                });
        });
    })();

    function backToLoginPage(message) {
        alert(message);
        window.location.replace(config.pages.login);
    }

    function initializeAdminPage() {
        setUsername();
        setCounts();
        //generateSubpages();
    }

    function setUsername() {
        $("#username").html(userData.username);
    }

    function setCounts() {
        $.get(serviceUrls.user, function (data) {
            $("#user-count-badge").html(data.payload.length);
            $("#user-count-well").append(data.payload.length);
        });

        $.get(serviceUrls.restaurant, function (data) {
            $("#restaurant-count-badge").html(data.payload.length);
            $("#restaurant-count-well").append(data.payload.length);
        });

        $.get(serviceUrls.category, function (data) {
            $("#category-count-badge").html(data.payload.length);
            $("#category-count-well").append(data.payload.length);
        });

        $.get(serviceUrls.item, function (data) {
            $("#item-count-badge").html(data.payload.length);
            $("#item-count-well").append(data.payload.length);
        });
    }


    $('#logout-btn').click(function (e) {
        Cookies.remove("POSRESTAURANT");
        window.location.assign(config.pages.login);
    });

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