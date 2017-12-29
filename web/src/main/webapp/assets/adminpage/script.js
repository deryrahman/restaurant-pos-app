$(document).ready(function () {

    var token = Cookies.get("POSRESTAURANT");
    var config;
    var baseUrl;
    var serviceUrls = {};
    var userData = {};
    var dataLists = {};
    var tableHeaders = {};

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
        loadAllData();
        bindShowPanelToClickEvent();
    }

    function setUsername() {
        $("#username").html(userData.username);
    }

    function loadAllData() {
        var userTableHeader =
            "<tr>\n" +
            "    <th>#ID</th>\n" +
            "    <th>Username</th>\n" +
            "    <th>Role</th>\n" +
            "    <th>Email</th>\n" +
            "    <th>Restaurant</th>\n" +
            "    <th></th>\n" +
            "</tr>";

        loadData("user", userTableHeader, userToTableRow);

        $.get(serviceUrls.restaurant, function (data) {
            $("#restaurant-count-badge").html(data.payload.length);
            $("#restaurant-count-well").append(data.payload.length);
        });

        $.get(serviceUrls.category, function (data) {
            $("#category-count-badge").html(data.payload.length);
            $("#category-count-well").append(data.payload.length);
        });

        loadItemData();
    }

    function loadData(dataName, tableHeader, toTableRowFunction) {
        $.get(serviceUrls[dataName], function (data) {
            dataLists[dataName] = data.payload;

            $("#"+dataName+"-count-badge").html(dataLists[dataName].length);
            $("#"+dataName+"-count-well").append(dataLists[dataName].length);

            $("#"+dataName+"-table").html(tableHeader);
            dataLists[dataName].forEach(function (item) {
                $("#"+dataName+"-table").append(toTableRowFunction(item));
            })
        });
    }

    function userToTableRow(userObject) {
        row = "<tr>" +
            "       <td>" + userObject.id + "</td>" +
            "       <td>" + userObject.name + "</td>" +
            "       <td>" + userObject.role + "</td>" +
            "       <td>" + userObject.email + "</td>" +
            "       <td>" + userObject.restaurantId + "</td>" +
            "       <td><a role='button'>edit</a></td>" +
            "</tr>";
        return row;
    }

    function loadRestaurantData() {
        var restaurantList;
        var restaurantTableHeader =
            "<tr>\n" +
            "    <th>#ID</th>\n" +
            "    <th>Address</th>\n" +
            "    <th>Phone</th>\n" +
            "    <th></th>\n" +
            "</tr>";

        $.get(serviceUrls.restaurant, function (data) {
            restaurantList = data.payload;

            $("#restaurant-count-badge").html(restaurantList.length);
            $("#restaurant-count-well").append(restaurantList.length);

            $("#restaurant-table").html(restaurantTableHeader);
            restaurantList.forEach(function (user) {
                $("#restaurant-table").append(userToTableRow(user));
            })
        })
    }

    function userToTableRow(userObject) {
        row = "<tr>" +
            "       <td>" + userObject.id + "</td>" +
            "       <td>" + userObject.name + "</td>" +
            "       <td>" + userObject.role + "</td>" +
            "       <td>" + userObject.email + "</td>" +
            "       <td>" + userObject.restaurantId + "</td>" +
            "       <td><a role='button'>edit</a></td>" +
            "</tr>";
        return row;
    }

    function loadItemData() {
        var itemList;
        var itemTableHeader =
            "<tr>\n" +
            "    <th>#ID</th>\n" +
            "    <th>Item name</th>\n" +
            "    <th>Price</th>\n" +
            "    <th>Category</th>\n" +
            "    <th>Status</th>\n" +
            "    <th></th>\n" +
            "</tr>";

        $.get(serviceUrls.item, function (data) {
            itemList = data.payload;

            $("#item-count-badge").html(itemList.length);
            $("#item-count-well").append(itemList.length);

            $("#item-table").html(itemTableHeader);
            itemList.forEach(function (item) {
                $("#item-table").append(itemToTableRow(item));
            });
        });
    }

    function itemToTableRow(itemObject) {
        row = "<tr>" +
            "       <td>" + itemObject.id + "</td>" +
            "       <td>" + itemObject.name + "</td>" +
            "       <td>" + itemObject.price + "</td>" +
            "       <td>" + itemObject.categoryId + "</td>" +
            "       <td>" + itemObject.status + "</td>" +
            "       <td><a role='button'>edit</a></td>" +
            "</tr>";
        return row;
    }


    $('#logout-btn').click(function (e) {
        Cookies.remove("POSRESTAURANT");
        window.location.assign(config.pages.login);
    });

    function bindShowPanelToClickEvent() {
        showPanelButtonOnclick("overview");
        showPanelButtonOnclick("users");
        showPanelButtonOnclick("restaurants");
        showPanelButtonOnclick("items");
    }

    function showPanelButtonOnclick(panelName) {
        $("#show-"+panelName).click(function () {
            $(".panel.panel-default").hide();
            $("#panel-"+panelName).show();

            $("a[id|='show']").removeClass('active main-bg-color');
            $(this).addClass('active main-bg-color');
        });
    }

});