var coreService = "http://localhost:8080/restaurant"
var authService = "http://localhost:8080/auth"

var categoryItem = {}

// Main
function loadMain(){
    loadCategories()
    loadAllItem()
    loadRestaurant()
}

// Categories
function loadCategories(){
    $.getJSON(coreService+"/categories", function (data) {
        if(!data["success"]){
            console.log(data["message"])
            return;
        }
        var payload = data["payload"]
        var categories = []
        var tabContentCategories = []
        $.each(payload, function (key, val) {
            var id = val["id"]
            var name = val["name"]
            categories.push(
                categoryToHTML(id, name)
            )
            tabContentCategories.push(
                tabContentCategoryToHTML(id)
            )
        })

        $("#nav-categories").append(categories)
        $(".tab-content").append(tabContentCategories)
    })
}
function categoryToHTML(id, name) {
    var catId = "category-"+id
    var result =
    "<li>"+
    "<a href='#"+catId+"' data-toggle='tab' onclick='loadItemByCategoryId("+id+")'>"+
    name+
    "</a>"+
    "</li>"
    return result
}
function tabContentCategoryToHTML(id) {
    var catId = "category-"+id
    var result =
    "<div class='tab-pane fade"+"' id='"+catId+"'>"+
    "</div>"
    return result
}
// Items
function loadItemByCategoryId(categoryId) {
    if(categoryItem['category-'+categoryId] != null) {
        return;
    }
    $.getJSON(coreService+"/categories/"+categoryId+"/items", function (data) {
        if(!data["success"]){
            console.log(data["message"])
            return;
        }
        var payload = data["payload"]
        var items = []
        $.each(payload, function (key, val) {
            var itemId = val["itemId"]
            if($('#item-'+itemId) != null){
                val["stock"] = parseInt($('.item-stock-'+itemId).first().text());
            }
            items.push(
                itemToHTML(val)
            )
        })
        categoryItem['category-'+categoryId] = items
        $("#category-" + categoryId).empty().append(items)
    })
}
function loadAllItem() {
    $.getJSON(coreService+"/items", function (data) {
        if(!data["success"]){
            return;
        }
        var payload = data["payload"]
        var items = []
        $.each(payload, function (key, val) {
            items.push(
                itemToHTML(val)
            )
        })

        $("#category-all").empty().append(items)
    })
}

function itemToHTML(item) {
    var id = item["itemId"]
    var name = item["itemName"]
    var price = item["price"]
    var stock = item["stock"]
    var categoryId = item["categoryId"]
    var itemId = "item-"+item["id"]

    var result = "<div class='col-xs-12 col-sm-3 col-md-2 col-lg-3'>"+
    "<div id='"+itemId+"' class='item pointer' onclick='addItem("+id+",\""+name+"\","+price+")'>" +
    "<div class='row padding-default'>" +
    "<div class='col-xs-8 col-sm-12'>" +
    "<h4 class='title-item'>" +
    name +
    "</h4>" +
    "</div>" +
        "<div class='col-xs-4 col-sm-12 align-right stock-item'>" +
        "Stock : " + "<span class='item-stock-"+id+"'>" + stock + "</span>" +
        "</div>" +
    "<div class='col-xs-4 col-sm-12 align-right price-item'>" +
    "Rp" + price +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>";
    return result
}

function loadRestaurant(){
    $.getJSON(coreService+"/restaurants/"+restaurantId, function (data) {
        var payload = data["payload"]
        $('#restaurant-branch').text(payload['address'])
        $('#restaurant-phone').text(payload['phone'])
    })
        .fail(function (data) {
            console.log(data["message"])
            return;
        })
}