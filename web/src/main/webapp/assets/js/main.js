var coreService = "http://localhost:8080/restaurant"
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
            var id = "item-"+key
            var name = val["name"]
            var price = val["price"]
            var categoryId = val["categoryId"]
            items.push(
                itemToHTML(id,name,price,categoryId)
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
function itemToHTML(id, name, price, categoryId) {
    var itemId = "item"+id
    var result = "<div class='col-xs-12 col-sm-3 col-md-2 col-lg-3'>"+
    "<div class='item pointer'>" +
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