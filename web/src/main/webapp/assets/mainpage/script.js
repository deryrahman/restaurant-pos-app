var pageRole = "cashier";

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
        receipt: {

        },
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

});