var pageRole = "cashier";

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
    item: ["id", "name", "price", "categoryId", "status"],
    itemWithStock: ["itemId", "itemName","stock","price","categoryId"]
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

$(document).ready(function () {
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
            alert("Error GET Request! See log for further information")
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
            alert("Error POST Request! See log for further information")
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
            alert("Error PUT Request! See log for further information")
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
            alert("Error PUT Request! See log for further information")
        }
    });
}

function backToLoginPage(message) {
    alert(message);
    window.location.replace(config.pages.login);
}

// Initialize all functionality
function initializePage() {
    loadNavbar();
    document.title = pageRole + " Page";
    if(pageRole === "admin") {

    } else if (pageRole === "manager"){

    } else if (pageRole === 'cashier'){
        loadNavCategory();
        loadItemByCategoryId("all");
    }
}
function loadNavbar() {
    $('#navbar-container').load('template/navbar.html', function(){
        console.log("load navbar");
        setNavbar();
    });
}

// Set navbar username
function setNavbar() {
    getJSON(true,serviceUrls.user+"/"+userData.id,function (data) {
        userData.userInfo = data.payload;
        var restaurantId = userData.userInfo.restaurantId;
        getJSON(true, serviceUrls.restaurant+"/"+restaurantId,function (data) {
            userData.restaurantInfo = data.payload;
            $("#page-role > a")
                .text(pageRole + " Page")
                .attr("href",pageRole+"page.html");
            $("#user-name > a").text(userData.username);
            if(pageRole == "admin"){
                $("#restaurant-id").remove();
                $("#restaurant-phone").remove();
            } else {
                $("#restaurant-id > a > span").text(userData.restaurantInfo.id);
                $("#restaurant-phone > a").text(userData.restaurantInfo.phone);
            }
        });
    });
}

function itemToHTML(item){
    var result = "<div id='item-"+item.itemId+"' class='item col-md-3 pointer' onclick='addItem("+item.itemId+")'>" +
        "<div class='well dash-box'>" +
        "<h4 class='title-item'>"+item.itemName+"</h4>" +
        "<div>Stock : <span class='item-stock-"+item.itemId+"'>"+item.stock+"</span></div>" +
        "<div>Rp "+item.price+"</div>"+
        "</div>" +
        "</div>";
    return result;
}
function loadItemByCategoryId(id) {
    var url;
    if(id === "all"){
        url = serviceUrls.item;
    } else {
        url = serviceUrls.category+"/"+id+"/items";
    }
    getJSON(true,url,function (data) {
        var payload = data.payload;
        var items = [];
        dataLists.items = []
        payload.forEach(function(item){
            dataLists.items.push(item)
            items.push(
                itemToHTML(item)
            )
        });
        console.log(dataLists);
        $("#category-panel-" + id).empty().append(items);
    });
}

function categoryToHTML(category) {
    var catId = "category-panel-"+category.id;
    var result =
        "<li>"+
        "<a href='#"+catId+"' data-toggle='tab' onclick='loadItemByCategoryId("+category.id+")'>"+
        category.name+
        "</a>"+
        "</li>";

    return result;
}
function categoryPanelToHTML(id){
    var result = "<div class='tab-pane fade' id='category-panel-"+id+"'></div>";
    return result;
}

function loadNavCategory() {
    getJSON(true,serviceUrls.category, function (data) {
        var payload = data.payload;
        var categories = [];
        var categoryPanels = [];
        payload.forEach(function(category){
            categories.push(
                categoryToHTML(category)
            );
            categoryPanels.push(
                categoryPanelToHTML(category.id)
            )
        });

        $("#nav-categories").append(categories)
        $("#tab-panel-main").append(categoryPanels)
    });
}

function addItem(id){

}

function renderSidebar(){

}

function renderMain(){

}