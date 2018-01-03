var pageRole = "manager";

var token = Cookies.get("POSRESTAURANT");
var config;
var baseUrl;
var serviceUrls = {};
var userData = {};
var userIdentities = [];

var dataLists = {};
var tableHeaders = {};
var tableStructures = {};

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
            $.each(config.endpoints, function (service, url) {
                serviceUrls[service] = baseUrl + url;
            });
            requestBodyFormat = config.requestBodyFormat;
            tableHeaders = config.tableHeaders;
            tableStructures = config.tableStructures;
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
function getJSON(async, url, callback, err){
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
            alert("Error GET Request! See log for further information");
            err();
        }
    });
}
function postJSON(async, url, data, callback, err){
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
            alert("Error POST Request! See log for further information");
            err();
        }
    });
}
function putJSON(async, url, data, callback,err){
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
            alert("Error PUT Request! See log for further information");
            err();
        }
    });
}
function deleteJSON(async, url, callback, err){
    $.ajax(url, {
        contentType : 'application/json',
        type : 'DELETE',
        async : async,
        success: function (data){
            callback(data)
        },
        error: function (e) {
            console.log(e.responseJSON.message);
            alert("Error PUT Request! See log for further information");
            err();
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
        loadAllData();
        bindShowPanelToClickEvent();
        bindNewButtonsToModals();
        bindModalsToSubmitEvent();
        bindUpdateButtonsToClickEvent();
        bindDeleteButtonsToClickEvent();
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

// Bind show panel to button click event
function bindShowPanelToClickEvent() {
    showPanelButtonOnclick("overview");
    showPanelButtonOnclick("restaurant");
    showPanelButtonOnclick("users");
    showPanelButtonOnclick("receipts");
    showPanelButtonOnclick("itemWithStock");
    showPanelButtonOnclick("categories");
    showPanelButtonOnclick("members");
}
function showPanelButtonOnclick(panelName) {
    $("#show-"+panelName).click(function () {
        $(".panel.panel-default").hide();
        $("#panel-"+panelName).show();

        $("a[id|='show']").removeClass('active main-bg-color');
        $(this).addClass('active main-bg-color');
    });
}

function bindNewButtonsToModals() {
    addClickEventToNewButton("user");
    addClickEventToNewButton("restaurant");
    addClickEventToNewButton("category");
    addClickEventToNewButton("itemWithStock");
}
function addClickEventToNewButton(modalName) {
    $("#btn-new-"+modalName).click(function () {
        revertModalInterface(modalName);
    });
}
function revertModalInterface(modalName) {
    var modalForm = $("#modal-new-"+modalName);
    modalForm.find("#submit-"+modalName).attr("type", "submit");
    modalForm.find("#update-"+modalName).attr("type", "button");
    modalForm.find(".modal-element-edit").hide();
    modalForm.find(".modal-element-new").show();

    emptyModalForm(modalName);
}
function emptyModalForm(modalName) {
    $("#form-new-" + modalName).find("input.form-control").val("");
}

// Get and load all data from core service
function loadAllData() {
    loadUserIdentity().then(function () {
        loadData("user",true);
    });
    loadData("category", false);
    loadData("itemWithStock", true);
    loadData("receipt", false);
    loadData("member", false);
}
function loadDataRestaurant() {
    getJSON(true,serviceUrls.restaurant+"/"+userData.userInfo.restaurantId,function(data){
        userData.restaurantInfo = data.payload;
        $.each($('.restaurant-id'),function(){
            $(this).text(userData.restaurantInfo.id);
        });
        $.each($('.restaurant-address'),function(){
            $(this).text(userData.restaurantInfo.address);
        });
        $.each($('.restaurant-phone'),function(){
            $(this).text(userData.restaurantInfo.phone);
        });
        $('#address').val(userData.restaurantInfo.address);
        $('#phone').val(userData.restaurantInfo.phone);
    });
}
function loadData(dataName,hasEdit) {
    var tableHeader;
    if(hasEdit){
        tableHeader = "<tr><th>"
            + tableHeaders[dataName].join("</th><th>")
            + "</th></tr>";
    } else {
        tableHeader = "<tr><th>"
            + tableHeaders[dataName].slice(0,-1).join("</th><th>")
            + "</th></tr>";
    }

    $.get(serviceUrls[dataName])
        .done(function (data) {
            var dataList = data.payload;

            $("#"+dataName+"-count-badge").html(dataList.length);
            $("#"+dataName+"-count-well").html(dataList.length);

            $("#"+dataName+"-table").html(tableHeader);
            dataList.forEach(function (item) {
                if (dataName === "user") {
                    item.username = userIdentities[item.id];
                }
                $("#"+dataName+"-table").append(objectToTableRow(dataName, item, hasEdit));
            })
        })
        .then(function () {
            bindEditButtonToModal(dataName);
        });
}
function loadUserIdentity() {
    return $.get(serviceUrls.userIdentity)
        .done(function (data) {
            var userList = data.payload;
            userList.forEach(function (item) {
                userIdentities[item.id] = item.username;
            });
        })
}
function objectToTableRow(dataName, object, hasEdit) {
    var columns = tableStructures[dataName];
    var button = "<td><a class='btn-edit' role='button'>edit</a></td>";

    var rowId = dataName + object.id;
    var row = "<tr id='"+rowId+"'>";
    columns.forEach(function (columnName) {
        row += "<td>" + object[columnName] + "</td>";
    });
    if(hasEdit) {
        row += button + "</tr>";
    }

    return row;
}

// Bind edit
function bindEditButtonToModal(modalName) {
    var targetId = "#modal-new-"+modalName;

    $("#"+modalName+"-table").find(".btn-edit")
        .addClass("btn-edit-"+modalName)
        .attr({
            "data-toggle": "modal",
            "data-target": targetId
        });

    $(".btn-edit-"+modalName).click(function (event) {
        var row = $(event.target).parent().siblings();
        var newObject = tableRowToObject(modalName, row);

        modifyModalInterface(modalName, newObject);
    });
}
function modifyModalInterface(modalName, object) {
    var modalForm = $("#modal-new-"+modalName);
    modalForm.find("#submit-"+modalName).attr("type", "button");
    modalForm.find("#update-"+modalName).attr("type", "submit");
    modalForm.find(".modal-element-new").hide();
    modalForm
        .find(".modal-element-edit").show()
        .find(".modal-id").html(object.id);

    fillModalForms(modalName, object);
}
function fillModalForms(modalName, object) {
    var template = requestBodyFormat[modalName];
    $.each(template, function (field, elmtId) {
        $("#form-new-"+modalName).find(elmtId).val(object[field]);
    });

    var username;
    if (modalName === "user") {
        username = object.username;
        $("#new-username").val(username);
    }
}
function tableRowToObject(dataName, row) {
    var objectProperties = tableStructures[dataName];
    var newObject = {};

    var propertyName;
    for (var i = 0; i < row.length; i++) {
        propertyName = objectProperties[i];
        newObject[propertyName] = row[i].innerHTML;
    }

    return newObject;
}
function saveEditRestaurant(){
    var restaurant = {
        'address' : $('#address').val(),
        'phone' : $('#phone').val()
    };
    var id = userData.restaurantInfo.id;
    putJSON(false, serviceUrls.restaurant+"/"+id, restaurant, function() {
        alert("Success edit restaurant");
        loadDataRestaurant();
    });
}

// Bind CREATE API to modals submit event
function bindModalsToSubmitEvent() {
    addClickEventToUserSubmitButton();
    addClickEventToSubmitButton("receipt");
    addClickEventToSubmitButton("itemWithStock");
    addClickEventToSubmitButton("member");
}
function addClickEventToSubmitButton(modalName) {
    $("#submit-" + modalName).click(function (e) {
        var dataToBeSent = [];
        dataToBeSent.push(formInputToObject(modalName));
        sendCreateRequest(modalName, dataToBeSent)
            .then(function () {
                emptyModalForm(modalName);
                loadData(modalName);
            });

        e.preventDefault();
        e.stopPropagation();
    });
}
function addClickEventToUserSubmitButton() {
    $("#form-new-user").submit(function (e) {
        var newUserIdentity = formInputToObject("userIdentity");
        var userList = [];
        userList.push(formInputToObject("user"));

        sendCreateRequest("user", userList,function(data){
            newUserIdentity.id = data.payload[0].id;
            postJSON(true,serviceUrls.userIdentity,newUserIdentity,function(){
                alert("Success create user");
                loadUserIdentity().then(function () {
                    loadData("user",true);
                });
            });
        });

        e.preventDefault();
        e.stopPropagation();
    });
}
function sendCreateRequest(dataName, dataToBeSent, callback) {
    postJSON(true,serviceUrls[dataName],dataToBeSent,function(data){
        callback(data);
    }, function (data) {
        var message = JSON.parse(data.responseText).message;
        alert("Failed to create new " + dataName + ".\n" + message);

        console.log(data.responseText);
    });
}
function formInputToObject(dataName) {
    var template = requestBodyFormat[dataName];
    var newObject = {};
    $.each(template, function (field, elmtId) {
        newObject[field] = $("#form-new-" + dataName).find(elmtId).val();
    });
    return newObject;
}
function invokeRestaurantId(){
    $('#form-new-user #restaurant-id').val(userData.restaurantInfo.id);
}

// Bind UPDATE API to update button click event
function bindUpdateButtonsToClickEvent() {
    addClickEventToUserUpdateButton();
    addClickEventToUpdateButton("restaurant");
    addClickEventToUpdateButton("category");
    addClickEventToUpdateButton("item");
}
function addClickEventToUpdateButton(modalName) {
    $("#update-"+modalName).click(function (e) {
        var rowId = $("#form-new-"+modalName).find(".modal-id").html();
        var dataToBeSent = formInputToObject(modalName);
        dataToBeSent.id = Number(rowId);

        sendUpdateRequest(modalName, dataToBeSent)
            .then(function () {
                loadData(modalName);
                closeModal(modalName);
            });
        e.preventDefault();
        e.stopPropagation();
    });
}
function addClickEventToUserUpdateButton() {
    $("#update-user").click(function (e) {
        var rowId = $("#form-new-user").find(".modal-id").html();
        var dataToBeSent = formInputToObject("user");
        dataToBeSent.id = Number(rowId);

        sendUpdateRequest("user", dataToBeSent)
            .then(function () {
                var updateUrl = serviceUrls.userIdentity + "/" + dataToBeSent.id;
                dataToBeSent = formInputToObject("userIdentity")
                return $.ajax(updateUrl, {
                    method: "PUT",
                    contentType: "application/json",
                    data: JSON.stringify(dataToBeSent)
                });
            })
            .done(function (data) {
                closeModal("user");
                loadData("user");
            })
            .fail(function (jqXHR) {
                console.log(JSON.parse(jqXHR.responseText));
            });
        e.preventDefault();
        e.stopPropagation();
    });
}
function sendUpdateRequest(dataName, dataToBeSent, callback) {
    var url = serviceUrls[dataName] + "/" + dataToBeSent.id;
    putJSON(true,url,dataToBeSent,function(data){
        alert("Successfully updated " + dataName + ".");
        callback(data);
    }, function (data) {
        var message = JSON.parse(data.responseText).message;
        alert("Failed to update " + dataName + ".\n" + message);
        console.log(data.responseText);
    });
}