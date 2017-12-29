$(document).ready(function () {

    var token = Cookies.get("POSRESTAURANT");
    var config;
    var baseUrl;
    var serviceUrls = {};
    var userData = {};

    var dataLists = {};
    var tableStructures = {
        user: ["id", "name", "role", "email", "restaurantId"],
        restaurant: ["id", "address", "phone"],
        category: ["id", "name", "description"],
        item: ["id", "name", "price", "categoryId", "status"]
    };
    var tableHeaders = {
        user: ["#ID", "Name", "Role", "Email", "Restaurant", ""],
        restaurant: ["#ID", "Address", "Phone", ""],
        category: ["#ID", "Name", "Description", ""],
        item: ["#ID", "Item Name", "Price", "Category", "Status", ""]
    };

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
        loadData("user");
        loadData("restaurant");
        loadData("category");
        loadData("item");
    }

    function loadData(dataName) {
        var tableHeader = "<tr><th>"
            + tableHeaders[dataName].join("</th><th>")
            + "</th></tr>";

        $.get(serviceUrls[dataName], function (data) {
            dataLists[dataName] = data.payload;

            $("#"+dataName+"-count-badge").html(dataLists[dataName].length);
            $("#"+dataName+"-count-well").append(dataLists[dataName].length);

            $("#"+dataName+"-table").html(tableHeader);
            dataLists[dataName].forEach(function (item) {
                $("#"+dataName+"-table").append(toTableRow(dataName, item));
            })
        });
    }

    function toTableRow(dataName, object) {
        var columns = tableStructures[dataName];
        var button = "<td><a role='button'>edit</a></td>";

        var row = "<tr>";
        columns.forEach(function (columnName) {
            row += "<td>" + object[columnName] + "</td>";
        });
        row += button + "</tr>";

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
        showPanelButtonOnclick("categories");
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