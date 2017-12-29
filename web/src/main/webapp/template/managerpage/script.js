var users = [];
var receipts = [];
var items = [];
var members = [];
var categories = [];
var restaurants = [];
var restaurant = {};

function getTabList() {
    var list = [];
    $('.list-group-item').each(function(){
        var id = this.id.split("-");
        var tab = id[id.length-1];
        list.push(tab);
    });
    return list;
}
function generateToggle() {
    var tabList = getTabList();
    console.log(tabList)
    $.each(tabList,function (key,item) {
        $('#show-'+item).click(function(){
            $.each(tabList,function(key1, item1){
                if(item!=item1){
                    $('#panel-'+item1).hide();
                    $('#show-'+item1).removeClass('active main-bg-color');
                } else {
                    $('#panel-'+item1).show();
                    $('#show-'+item1).addClass('active main-bg-color');
                }
            });
        })
            .attr('onclick','load'+item+'()');
    });
}

function renderUserInfo() {
    $('#user-name').text(userInfo.name)
}
function loadScript() {
    generateToggle();
    loadoverview();
    renderUserInfo();
}

function loadoverview() {

    loadusers();
    loaditems();
    loadreceipts();
    loadmembers();
    loadrestaurants();
    loadcategories();

}

function updateOverview(){
    var overviewCount = {
        'users' : users.length,
        'items' : items.length,
        'receipts' : receipts.length,
        'members' : members.length
    };
    $.each(overviewCount, function(key, val){
        $('#count-'+key).text(val)
    })
}

function updateBadge() {
    var badgeCount = {
        'users' : users.length,
        'items' : items.length,
        'receipts' : receipts.length,
        'members' : categories.length,
        'categories' : members.length
    };
    $.each(badgeCount, function(key, val){
        $('#show-'+key+' > .badge').text(val)
    })
}

function write() {
    console.log(users)
    console.log(items)
    console.log(receipts)
    console.log(members)
    console.log(categories)
}

function renderUserList() {
    var userList = [];
    var headerTable = "<tr><th>#ID</th><th>Username</th><th>Role</th><th>Email</th><th></th></tr>";
    userList.push(headerTable);
    users.forEach(function(user){
        userList.push(userToHTML(user));
    });
    $('#user-list').empty().append(userList);
}

function userToHTML(user){
    var result = "<tr>" +
        "<td>"+user.id+"</td>"+
        "<td>"+user.name+"</td>"+
        "<td>"+user.role+"</td>"+
        "<td>"+user.email+"</td>"+
        "<td><a role='button'>edit</a></td>"+
        "</tr>";
    return result
}

function loadusers() {
    console.log("load users");
    users = []
    console.log("before : ");
    console.log(users);
    getJSON(true,coreService+"/users", function(data){
        var payload = data["payload"];
        console.log("payload : ")
        console.log(payload)
        users = payload;
        updateOverview();
        updateBadge();
        console.log("after : ");
        console.log(users);
        renderUserList();
    });
}

function itemListToHTML(item) {
    var categoryName;
    categories.forEach(function(data){
        if(data.id == item.categoryId){
            categoryName = data.name;
        }
    })
    var result = "<tr>"+
        "<td>"+item.itemId+"</td>"+
        "<td>"+item.itemName+"</td>"+
        "<td>"+item.price+"</td>"+
        "<td>"+item.stock+"</td>"+
        "<td>"+categoryName+"</td>"+
        "<td><a role='button'>edit</a></td>"+
        "</tr>";
    return result;
}
function renderItemList() {
    var itemList = [];
    var headerTable = "<tr><th>#ID</th><th>Item name</th><th>Price</th><th>Stock</th><th>Category</th><th></th></tr>";
    itemList.push(headerTable);
    loadcategories()
    items.forEach(function(item){
        itemList.push(itemListToHTML(item));
    });
    $('#item-list').empty().append(itemList);
}
function loaditems() {
    console.log("load items");
    getJSON(true,coreService+"/items", function(data){
        var payload = data["payload"];
        items = payload;
        updateOverview();
        updateBadge();

        renderItemList();
    });
}

function receiptToHTML(receipt) {
    var result = "<tr>"+
        "<td>"+receipt.id+"</td>"+
        "<td>"+receipt.timestampCreated+"</td>"+
        "<td>"+receipt.userId+"</td>"+
        "<td>"+receipt.memberId+"</td>"+
        "<td>"+receipt.totalPrice+"</td>"+
        "<td>"+receipt.tax+"</td>"+
        "</tr>";
    return result;
}
function renderReceiptList() {
    var receiptList = [];
    var headerTable = "<tr><th>#ID</th><th>Date</th><th>Cashier Id</th><th>Member Id</th><th>Total</th><th>Tax</th></tr>";
    receiptList.push(headerTable);
    receipts.forEach(function(receipt){
        receiptList.push(receiptToHTML(receipt));
    });
    $('#receipt-list').empty().append(receiptList);
}
function loadreceipts(){
    console.log("load receipts");
    getJSON(true,coreService+"/receipts", function(data){
        var payload = data["payload"];
        receipts = payload;
        updateOverview();
        updateBadge();

        renderReceiptList();
    });
}

function memberListToHTML(member) {
    var result = "<tr>"+
        "<td>"+member.id+"</td>"+
        "<td>"+member.timestampCreated+"</td>"+
        "<td>"+member.name+"</td>"+
        "<td>"+member.email+"</td>"+
        "<td>"+member.address+"</td>"+
        "</tr>";
    return result;
}
function renderMemberList() {
    var memberList = [];
    var headerTable = "<tr><th>#ID</th><th>Date Join</th><th>Name</th><th>Email</th><th>Address</th></tr>";
    memberList.push(headerTable);
    members.forEach(function(member){
        memberList.push(memberListToHTML(member));
    });
    $('#member-list').empty().append(memberList);
}
function loadmembers() {
    console.log("load members");

    getJSON(true,coreService+"/members", function(data){
        var payload = data["payload"];
        members = payload;
        updateOverview();
        updateBadge();

        renderMemberList();
    });
}

function categoryListToHTML(category) {
    var result = "<tr>" +
        "<td>"+category.id+"</td>"+
        "<td>"+category.name+"</td>"+
        "<td>"+category.description+"</td>"+
        "</tr>";
    return result
}
function renderCategoryList() {
    var categoryList = [];
    var headerTable = "<tr><th>#ID</th><th>Name</th><th>Description</th></tr>";
    categoryList.push(headerTable);
    categories.forEach(function(category){
        categoryList.push(categoryListToHTML(category));
    });
    $('#category-list').empty().append(categoryList);
}
function loadcategories() {
    console.log("load categories");
    getJSON(true,coreService+"/categories", function(data){
        var payload = data["payload"];
        categories = payload;
        updateBadge();

        renderCategoryList();
    });
}

function loadrestaurants() {
    console.log("load restaurants");
    getJSON(true,coreService+"/restaurants", function(data){
        var payload = data["payload"];
        restaurants = payload;
        restaurants.forEach(function(data){
            if(data.id == restaurantId){
                restaurant = data;
                console.log(data)
            }
        });

        $('.restaurant-id').each(function(){
            $(this).text(restaurant.id)
        });
        $('.restaurant-address').each(function () {
            $(this).text(restaurant.address)
        });
        $('.restaurant-phone').each(function () {
            $(this).text(restaurant.phone)
        });
        $('#address').val(restaurant.address)
        $('#phone').val(restaurant.phone)
    });
}

function saveEditRestaurant(){
    var restaurant1 = {
        'address' : $('#address').val(),
        'phone' : $('#phone').val()
    };
    var url = coreService+"/restaurants/"+restaurantId;
    putJSON(false, url, restaurant1, function() {
        alert("Success")
        loadrestaurants()
    });
}