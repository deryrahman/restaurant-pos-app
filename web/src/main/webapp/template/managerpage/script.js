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

function loadScript() {
    generateToggle();
    loadoverview();
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
        'members' : members.length,
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
    getJSON(true,coreService+"/users", function(data){
        var payload = data["payload"];
        users = payload;
        updateOverview();
        updateBadge();

        renderUserList();
    });
}

function loaditems() {
    console.log("load items");
    getJSON(true,coreService+"/items", function(data){
        var payload = data["payload"];
        items = payload;
        updateOverview();
        updateBadge();
    });
}

function loadreceipts(){
    console.log("load receipts");
    getJSON(true,coreService+"/receipts", function(data){
        var payload = data["payload"];
        receipts = payload;
        updateOverview();
        updateBadge();
    });
}

function loadmembers() {
    console.log("load members");

    getJSON(true,coreService+"/members", function(data){
        var payload = data["payload"];
        members = payload;
        updateOverview();
        updateBadge();
    });
}

function loadcategories() {
    console.log("load categories");
    getJSON(true,coreService+"/categories", function(data){
        var payload = data["payload"];
        categories = payload;
        updateBadge();
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