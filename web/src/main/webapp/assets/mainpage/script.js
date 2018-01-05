var pageRole = "cashier";

var cookie;
var token = Cookies.get("POSRESTAURANT");
var config;
var baseUrl;
var serviceUrl;
var serviceUrls = {};
var userData = {};
var itemIdToRemove;

var dataLists = {};
dataLists.receipt = {};
dataLists.receipt.tax = 5;
dataLists.receipt.note;
dataLists.member;
dataLists.categories = {};
dataLists.categories['all'] = {};
dataLists.items = {};
dataLists.itemOnSidebar= {};

$(document).ready(function () {
    // Get configurations and check cookie
    (function () {

        var configUrl = "configurations.json";
        $.getJSON(configUrl, function (data) {
            config = data;
            baseUrl = config.baseUrl;
            serviceUrl = config.serviceUrl;
            serviceUrls.parser = serviceUrl + config.endpoints.parser;
            serviceUrls.user = serviceUrl + config.endpoints.user;
            serviceUrls.item = serviceUrl + config.endpoints.item;
            serviceUrls.category = serviceUrl + config.endpoints.category;
            serviceUrls.restaurant = serviceUrl + config.endpoints.restaurant;
            serviceUrls.member = serviceUrl + config.endpoints.member;
            serviceUrls.receipt = serviceUrl + config.endpoints.receipt;

            if (token === undefined) {
                backToLoginPage("You are not logged in. Please login.");
            }
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
            if(e.responseJSON) {
                console.log(e.responseJSON.message);
            }
            // alert("Error GET Request! See log for further information");
            if(err) {
                err(e);
            }
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
            if(e.responseJSON) {
                console.log(e.responseJSON.message);
            }
            // alert("Error POST Request! See log for further information");
            if(err) {
                err(e);
            }
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
            if(e.responseJSON) {
                console.log(e.responseJSON.message);
            }
            // alert("Error PUT Request! See log for further information");
            if(err) {
                err(e);
            }
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
            if(e.responseJSON) {
                console.log(e.responseJSON.message);
            }
            // alert("Error DELETE Request! See log for further information");
            if(err) {
                err(e);
            }
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
    $('.tax').each(function(){
        $(this).text(dataLists.receipt.tax);
    });
    $('ul.CTAs').hide();
    document.title = pageRole + " Page";
    if(pageRole === "admin") {

    } else if (pageRole === "manager"){

    } else if (pageRole === 'cashier'){
        loadNavCategory();
        loadItemByCategoryId("all");
    }
}
function loadNavbar() {
    $('#navbar-container').load('fragment/navbar.html', function(){
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
    var result = "<div class='item-"+item.itemId+" item col-md-3 pointer' onclick='addItem("+item.itemId+")'>" +
        "<div class='well dash-box'>" +
        "<h4 class='title-item'>"+item.itemName+"</h4>" +
        "<div>Stock : <span class='item-stock-"+item.itemId+"'>"+item.stock+"</span></div>" +
        "<div>Rp <span class='price-item'>"+item.price+"</span></div>"+
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
    if(dataLists.categories[id].items){
        return;
    } else {
        getJSON(true, url, function (data) {
            var payload = data.payload;
            var items = [];
            dataLists.categories[id].items = {};
            payload.forEach(function (item) {
                dataLists.categories[id].items[item.id] = item;
                if(!dataLists.items[item.itemId]) {
                    dataLists.items[item.itemId] = item;
                }
                items.push(
                    itemToHTML(item)
                )
            });
            $("#category-panel-" + id).empty().append(items);
            renderMain();
        }, function(e){
            if(e.responseJSON){
                if(e.responseJSON.statusCode == 404){
                    $('#not-found-'+id).show();
                }
            }
        });
    }
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
    var result = "<div class='tab-pane fade' id='category-panel-"+id+"'>" +
        "<div class='container not-found-page' id='not-found-"+id+"' style='display: none'>" +
        "<div class='jumbotron'>" +
        "<h3>Item Not Found</h3>" +
        "</div>" +
        "</div>" +
        "</div>";
    return result;
}

function loadNavCategory() {
    getJSON(true,serviceUrls.category, function (data) {
        var payload = data.payload;
        var categories = [];
        var categoryPanels = [];
        payload.forEach(function(category){
            dataLists.categories[category.id] = category;

            categories.push(
                categoryToHTML(category)
            );
            categoryPanels.push(
                categoryPanelToHTML(category.id)
            )
        });

        $("#nav-categories").append(categories);
        $("#tab-panel-main").append(categoryPanels);
    });
}

function addItem(id){
    if(dataLists.items[id].stock-1<0){
        alert("Stock limited!");
    } else {
        if (dataLists.itemOnSidebar[id]) {
            dataLists.itemOnSidebar[id].count += 1;
        } else {
            dataLists.itemOnSidebar[id] = {
                "itemId": id,
                "count": 1
            };
        }
        dataLists.items[id].stock -= 1;
        renderSidebar();
        renderMain();
    }
}

function renderSidebar(){
    var itemSidebarList = [];
    var totalPrice = 0;
    var totalCount = 0;
    var totalPriceAfterTax;
    var tax = dataLists.receipt.tax;
    if($.isEmptyObject(dataLists.itemOnSidebar)){
        $('ul.CTAs').hide();
    } else {
        $('ul.CTAs').show();
    }
    $.each(dataLists.itemOnSidebar, function (key,val) {
        itemSidebarList.push(itemOnSidebarToHTML(val));
        totalCount+=val.count;
        totalPrice+=val.count*dataLists.items[key].price;
    });
    totalPriceAfterTax = totalPrice*(100+tax)/100;

    totalPriceAfterTax = totalPrice*(100+tax)/100;
    $('#item-on-sidebar').empty().append(itemSidebarList);
    $('.total-count').each(function(){
        $(this).text(totalCount);
    });
    $('.total-price').each(function(){
        $(this).text(totalPrice);
    });
    $('.total-price-after-tax').each(function () {
        $(this).text(totalPriceAfterTax);
    });
}

function invokeIdToRemove(id){
    itemIdToRemove = id;
}

function removeOne() {
    dataLists.items[itemIdToRemove].stock+=1;
    dataLists.itemOnSidebar[itemIdToRemove].count-=1;
    if(dataLists.itemOnSidebar[itemIdToRemove].count == 0){
        delete dataLists.itemOnSidebar[itemIdToRemove];
    }
    renderSidebar();
    renderMain();
}

function removeById() {
    dataLists.items[itemIdToRemove].stock+=dataLists.itemOnSidebar[itemIdToRemove].count;
    delete dataLists.itemOnSidebar[itemIdToRemove];
    renderSidebar();
    renderMain();
}

function itemOnSidebarToHTML(itemOnSidebar){
    var id = itemOnSidebar.itemId;
    var cnt = itemOnSidebar.count;
    var result = "<li data-toggle='modal' data-target='#modal-remove-id' onclick='invokeIdToRemove("+id+")'>"+
        "<a href='#'>"+
        dataLists.items[id].itemName +
        "<ul class='list-inline' style='float:right'>"+
        "<li><span class='badge' style='background:#bf360c'>"+cnt+"</span></li>"+
        "<li>Rp "+dataLists.items[id].price*cnt+"</li>"+
        "</ul>"+
        "</a>"+
        "</li>";
    return result;
}

function renderMain(){
    $.each(dataLists.items, function (key,val) {
        $('.item-stock-'+key).each(function(){
            $(this).text(dataLists.items[key].stock);
        });
    });
}

function removeAll(){
    (function() {
        $.each(dataLists.itemOnSidebar, function (key,val) {
            dataLists.items[key].stock+=val.count;
        });
    })();

    dataLists.itemOnSidebar = {};
    renderSidebar();
    renderMain();
}

function itemOnReceiptToHTML(item) {
    var id = item.itemId;
    var cnt = item.count;
    var result = "<tr>"+
        "<td>"+dataLists.items[id].itemName+"</td>"+
        "<td>"+cnt+"</td>"+
        "<td>Rp "+dataLists.items[id].price*cnt+"</td>"+
        "</tr>";
    return result;
}
function renderReceipt(){
    var itemReceiptList = [];
    $.each(dataLists.itemOnSidebar, function (key,val) {
        itemReceiptList.push(itemOnReceiptToHTML(val));
    });
    $('#receipt-items').empty().append(itemReceiptList);
}

function addNote() {
    if($('#note').val() === ""){
        delete dataLists.receipt.note;
        $('#add-note-btn').text("Add Note");
    } else {
        dataLists.receipt.note = $('#note').val();
        $('#add-note-btn').text("Edit Note");
    }
}

function makeReceipt(){
    var receipt = {
        "tax" : dataLists.receipt.tax,
        "items" : []
    };
    if(dataLists.receipt.note){
        receipt.note = dataLists.receipt.note;
    }
    if(dataLists.member){
        receipt.memberId = dataLists.member.id;
    }
    $.each(dataLists.itemOnSidebar,function(key,val){
        receipt.items.push(val);
    });
    var data = [];
    data.push(receipt);
    postJSON(true,serviceUrls.receipt,data,function (e) {
        var payload = e.payload;
        var resultId = "";
        var timestamp = "";
        var memberId = "-";
        if(dataLists.member) {
            memberId = dataLists.member.id;
        }
        payload.forEach(function(data){
            resultId = data.receiptId;
            timestamp = data.timestamp;
        });
        var itemOnCookie = [];
        $.each(dataLists.itemOnSidebar, function(key,val){
            var item = {
                "name" : $('.item-'+key+' .title-item').text(),
                "count" : val.count,
                "price" : $('.item-'+key+' .price-item').text()
            };
            itemOnCookie.push(item);
        });
        cookie = {
            "id" : resultId,
            "data" : timestamp,
            "memberId" : memberId,
            "restaurantId" : userData.restaurantInfo.id,
            "items" : itemOnCookie,
            "tax" : dataLists.receipt.tax
        };
        alert("Success make receipt. Id : "+resultId);
        $('#modal-download-receipt').modal('show');
    });
}

function addMember(){
    if($('#member-id').val() === ""){
        delete dataLists.member;
    } else {
        dataLists.member = {};
        dataLists.member.id = $('#member-id').val();
        var id = dataLists.member.id;
        getJSON(false,serviceUrls.member+"/"+id,function(data){
            var payload = data.payload;
            dataLists.member = payload;
        }, function(){
            delete dataLists.member;
        });
    }
    renderMemberInfo();
}

function renderMemberInfo(){
    var member = dataLists.member;
    if(member){
        $('#submit-add-member').hide();
        $('#submit-edit-member').show();
        $('#modal-remove-member-btn').show();
        $('#modal-add-member-btn').hide();
        $('#edit-member').show();
        $('#modal-member-title').text("Edit");
        $('#member-id').val(member.id);
        $('#member-id').attr('disabled', 'disabled');
        $('#member-info-id').text(member.id);
        $('#member-name').val(member.name);
        $('#member-info-name').text(member.name);
        $('#member-email').val(member.email);
        $('#member-info-email').text(member.email);
        $('#member-address').val(member.address);
        $('#member-info-address').text(member.address);
        $('#add-member-btn').text("Edit Member");
        $('#member-info').show();
    } else {
        $('#submit-add-member').show();
        $('#submit-edit-member').hide();
        $('#modal-remove-member-btn').hide();
        $('#modal-add-member-btn').show();
        $('#edit-member').hide();
        $('#modal-member-title').text("Add");
        $('#member-id').val("");
        $('#member-id').prop("disabled", false);
        $('#add-member-btn').text("Add Member");
        $('#member-info').hide();
    }
}

function newMember(){
    var member = {
        "name" : $('#name').val(),
        "address" : $('#address').val(),
        "email" : $('#email').val()
    };
    var memberList = [];
    memberList.push(member);
    postJSON(false,serviceUrls.member,memberList,function(data){
        var payload = data.payload;
        $.each(payload,function(id, data){
            alert("New Member with Id : " + data.id);
            dataLists.member = data;
        });
    }, function(){
        delete dataLists.member;
    });
    renderMemberInfo();
}

function editMember(){
    var member = {
        "name" : $('#member-name').val(),
        "address" : $('#member-address').val(),
        "email" : $('#member-email').val()
    };
    putJSON(false,serviceUrls.member+"/"+dataLists.member.id,member,function(data){
        alert("Success edit member");
        addMember();
    }, function(){
        delete dataLists.member;
    });
    renderMemberInfo();
}

function removeMember(){
    delete dataLists.member;
    renderMemberInfo();
}

function openReceipt(){
    Cookies.set('receipt', cookie);
    window.open('/receipt.html','_blank');
    window.location.replace(config.pages.home);
}

function logout(){
    Cookies.remove("POSRESTAURANT");
    window.location.assign(config.pages.login);
}