var coreService = "http://localhost:8080/restaurant"
var authService = "http://localhost:8080/auth"
var restaurantId = 1
var userId = 1

// Main
function loadMain(){
    loadCategories()
    loadAllItem()
}
function getCookie(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
}
// Check auth first
function isLogin(){
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
    $.getJSON(coreService+"/restaurants/"+restaurantId+"/categories/"+categoryId+"/items", function (data) {
        if(!data["success"]){
            console.log(data["message"])
            return;
        }
        var payload = data["payload"]
        var items = []
        $.each(payload, function (key, val) {
            console.log(val)
            items.push(
                itemToHTML(val)
            )
        })

        $("#category-"+categoryId).empty().append(items)
    })
}
function loadAllItem() {
    $.getJSON(coreService+"/restaurants/"+restaurantId+"/items", function (data) {
        if(!data["success"]){
            console.log(data["message"])
            return;
        }
        var payload = data["payload"]
        var items = []
        $.each(payload, function (key, val) {
            console.log(val)
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
    "<div class='col-xs-4 col-sm-12 align-right price-item'>" +
    "Rp" + price +
    "</div>" +
    "</div>" +
    "</div>" +
    "</div>";
    return result
}