var pageRole = "manager";

// json http request
function getJSON(async, url, callback){
    $.ajax({
        type: 'GET',
        url: url,
        dataType: 'json',
        async: async,
        success: function (data) {
            callback(data)
        },
        error: function (e) {
            console.log(e.responseJSON.message);
            alert("Error GET Request!")
        }
    });
}
function postJSON(async, url, data, callback){
    $.ajax(url, {
        data : JSON.stringify(data),
        contentType : 'application/json',
        type : 'POST',
        async : async,
        success: function (data){
            callback(data)
        },
        error: function (e) {
            console.log(e.responseJSON.message);
            alert("Error POST Request!")
        }
    });
}
function putJSON(async, url, data, callback){
    $.ajax(url, {
        data : JSON.stringify(data),
        contentType : 'application/json',
        type : 'PUT',
        async : async,
        success: function (data){
            callback(data)
        },
        error: function (e) {
            console.log(e.responseJSON.message);
            alert("Error PUT Request!")
        }
    });
}
function deleteJSON(async, url, callback){
    $.ajax(url, {
        contentType : 'application/json',
        type : 'DELETE',
        async : async,
        success: function (data){
            callback(data)
        },
        error: function (e) {
            console.log(e.responseJSON.message);
            alert("Error PUT Request!")
        }
    });
}

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
            "restaurantId": "#restaurant-id"
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
        },
        itemWithStock: {

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
            serviceUrls.item = baseUrl + config.endpoints.item;
            serviceUrls.category = baseUrl + config.endpoints.category;
            serviceUrls.restaurant = baseUrl + config.endpoints.restaurant;
            serviceUrls.member = baseUrl + config.endpoints.member;
            serviceUrls.receipt = baseUrl + config.endpoints.receipt;
        }).done(function () {
            $.ajax(serviceUrls.parser, {
                method: "POST",
                contentType: "application/x-www-form-urlencoded",
                data: {token: token}
            })
                .success(function (data) {
                    userData = data.payload;
                    if (userData.role === pageRole) {
                        console.log("Logged in as an " + pageRole);
                        initializePage();
                    } else {
                        backToLoginPage("You are not logged in as an " + pageRole + ". Please login as an " + pageRole);
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
    function initializePage() {
        setNavbar();
        document.title = pageRole + " Page";
        if(pageRole === "admin") {
            loadAllDataAdmin();
            bindShowPanelToClickEvent();
            bindModalsToSubmitEvent();
        } else if (pageRole === "manager"){

        }
    }

    // Set navbar username
    function setNavbar() {
        getJSON(false,serviceUrls.user+userData.id,function (data) {
            userData.userInfo = data.payload;
            var restaurantId = userData.userInfo.restaurantId;
            getJSON(false, serviceUrls.restaurant+restaurantId,function (data) {
                userData.restaurantInfo = data.payload;
            })
        });
        $("#page-role > a").text(pageRole + " Page");
        $("#page-role > a").attr("href",pageRole+"page.html");
        $("#user-name > a").text(userData.username);
        if(pageRole == "admin"){
            $("#restaurant-id").remove();
            $("#restaurant-phone").remove();
        } else {
            $("#restaurant-id > a > span").text(userData.restaurantInfo.id);
            $("#restaurant-phone > a").text(userData.restaurantInfo.phone);
        }
    }

    // Get and load all data from core service
    function loadAllDataAdmin() {
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
        addSubmitEventToModal("restaurant");
        addSubmitEventToModal("category");
        addSubmitEventToModal("item");
    }
    function addSubmitEventToModal(modalName) {
        $("#form-new-" + modalName).submit(function (e) {
            sendCreateRequest(modalName);

            e.preventDefault();
            e.stopPropagation();
        });
    }
    function sendCreateRequest(objectName) {
        var newObjectList = [];
        newObjectList.push(createNewObject(objectName));

        $.ajax({
            url: serviceUrls[objectName],
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(newObjectList)
        }).done(function (data) {
            alert("Successfully created new " + objectName + ".")
            $("#form-new-" + objectName).find("input.form-control").val("");
            loadData(objectName);

            console.log(data);
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