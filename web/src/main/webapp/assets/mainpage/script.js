var pageRole;

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

$(document).ready(function(){
    if (userRole !== "cashier") {
        alert("Not permitted");
        window.location.assign(config.pages.admin);
    }

    // load
    $('#sidebar-container').load('template/sidebar.html', function(){
        loadSidebar();
    });
    $('#page-homepage-container').load('template/page-homepage.html', function () {
        loadMain();
    });

    $(".toggle-sidebar-in-main").click(function(){
        $("#sidebar").animate({width:'toggle'},'medium',function(){
            $("#inside-wrapper-sidebar").fadeToggle('fast');
        });
        $(".overlay").fadeToggle('slow');
    });
    $(".overlay, .toggle-sidebar-in-sidebar, #add-item").click(function(){
        $("#inside-wrapper-sidebar").fadeToggle(10,function(){
            $("#sidebar").animate({width:'toggle'},'medium');
            $(".overlay").fadeToggle('slow');
        });
    });
    $(".toggle-search").click(function(){
        $('.search-bar').css('position','absolute');
        $('.logo').fadeToggle('fast');
        $('.search-bar').fadeToggle('fast');
    });
});