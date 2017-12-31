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
    var createRequestBody = {
        user: {
            "name": "#fullname",
            "email": "#email",
            "restaurantId": "#restaurant-id",
            "role": "input[name='role']:checked"
        },
        userIdentity: {
            "username": "#new-username",
            "password": "#password",
            "role": "input[name='role']:checked"
        },
        restaurant: {
            "address": "#address",
            "phone": "#phone"
        },
        category: {
            "name": "#category-name",
            "description": "#category-description"
        },
        item: {
            "name": "#item-name",
            "price": "#item-price",
            "categoryId": "#category-id",
            "status": "#item-status"
        }
    };

    // Get configurations and check cookie
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
            serviceUrls.userIdentity = baseUrl + config.endpoints.userIdentity;
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

    // Initialize all functionality
    function initializeAdminPage() {
        setUsername();
        loadAllData();
        bindShowPanelToClickEvent();
        bindModalsToSubmitEvent();
    }

    // Set navbar username
    function setUsername() {
        $("#username").html(userData.username);
    }

    // Get and load all data from core service
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

    // Bind CREATE API to modals submit event
    function bindModalsToSubmitEvent() {
        addSubmitEventToUserModal();
        addSubmitEventToModal("restaurant");
        addSubmitEventToModal("category");
        addSubmitEventToModal("item");
    }

    function addSubmitEventToModal(modalName) {
        $("#form-new-" + modalName).submit(function (e) {
            var dataToBeSent = [];
            dataToBeSent.push(createNewObject(modalName));
            sendCreateRequest(modalName, dataToBeSent);

            e.preventDefault();
            e.stopPropagation();
        });
    }

    function addSubmitEventToUserModal() {
        $("#form-new-user").submit(function (e) {
            var newUserIdentity = createNewObject("userIdentity");
            var userList = [];
            userList.push(createNewObject("user"));

            sendCreateRequest("user", userList)
                .then(function (data) {
                    console.log(data);
                    newUserIdentity.id = data.payload[0].id;

                    console.log(newUserIdentity);
                    $.ajax(serviceUrls.userIdentity, {
                        method: "POST",
                        contentType: "application/json",
                        data: JSON.stringify(newUserIdentity)
                    }).done(function (data) {
                        console.log(data);
                    }).fail(function (jqXHR) {
                        console.log(JSON.parse(jqXHR.responseText));
                    });
                });

            e.preventDefault();
            e.stopPropagation();
        });
    }

    function sendCreateRequest(dataName, dataToBeSent) {
        var successfullyCreatedObject = {};

        return $.ajax({
            url: serviceUrls[dataName],
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(dataToBeSent)
        }).done(function (data) {
            alert("Successfully created new " + dataName + ".");
            $("#form-new-" + dataName).find("input.form-control").val("");
            loadData(dataName);

            successfullyCreatedObject = data.payload[0];
            console.log(successfullyCreatedObject);
        }).fail(function (jqXHR) {
            var message = JSON.parse(jqXHR.responseText).message;
            alert("Failed to create new restaurant.\n" + message);

            console.log(jqXHR.responseText);
        });
    }

    function createNewObject(objectName) {
        var template = createRequestBody[objectName];
        var newObject = {};
        $.each(template, function (field, elmtId) {
            newObject[field] = $("#form-new-" + objectName).find(elmtId).val();
        });
        return newObject;
    }

    // Bind show panel to button click event
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

    // Bind loging out to logout button
    $('#logout-btn').click(function (e) {
        Cookies.remove("POSRESTAURANT");
        window.location.assign(config.pages.login);
    });

});