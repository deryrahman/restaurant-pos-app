var coreService = "http://localhost:8080/restaurant"
// load sidebar js
$.getScript("sidebar.js")

// Main
function loadMain(){
    loadCategories()
    loadAllItem()
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
    $.getJSON(coreService+"/categories/"+categoryId+"/items", function (data) {
        if(!data["success"]){
            console.log(data["message"])
            return;
        }
        var payload = data["payload"]
        var items = []
        $.each(payload, function (key, val) {
            items.push(
                itemToHTML(val)
            )
        })

        $("#category-"+categoryId).empty().append(items)
    })
}
function loadAllItem() {
    $.getJSON(coreService+"/items", function (data) {
        if(!data["success"]){
            console.log(data["message"])
            return;
        }
        var payload = data["payload"]
        var items = []
        $.each(payload, function (key, val) {
            var id = val["id"]
            var name = val["name"]
            var price = val["price"]
            var categoryId = val["categoryId"]
            items.push(
                itemToHTML(id,name,price,categoryId)
            )
        })

        console.log(items)

        $("#category-all").empty().append(items)
    })
}
function itemToHTML(item) {
    var id = item["id"]
    var name = item["name"]
    var price = item["price"]
    var categoryId = item["categoryId"]
    var itemId = "item-"+item["id"]

    var result = "<div class='col-xs-12 col-sm-3 col-md-2 col-lg-3'>"+
    "<div id='"+itemId+"' class='item pointer' onclick='addItem(item)'>" +
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
    "</div>"
    return result
}