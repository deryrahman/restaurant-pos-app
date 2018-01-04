var serviceUrls = {};
var userData = {};

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

var receipt;

(function () {
    receipt = JSON.parse(Cookies.get('receipt'));
    console.log(receipt);
    var configUrl = "configurations.json";
    $.getJSON(configUrl, function (data) {
        var config = data;
        var baseUrl = config.baseUrl;
        var serviceUrl = config.serviceUrl;
        serviceUrls.parser = serviceUrl + config.endpoints.parser;
        serviceUrls.user = serviceUrl + config.endpoints.user;
        serviceUrls.item = serviceUrl + config.endpoints.item;
        serviceUrls.category = serviceUrl + config.endpoints.category;
        serviceUrls.restaurant = serviceUrl + config.endpoints.restaurant;
        serviceUrls.member = serviceUrl + config.endpoints.member;
        serviceUrls.receipt = serviceUrl + config.endpoints.receipt;
    }).done(function () {
        renderReceipt();
    });
})();

function toRow(itemName, count, price) {
    var result = "<tr>" +
        "<td>"+itemName+"</td>" +
        "<td>"+count+"</td>" +
        "<td>"+price+"</td>" +
        "</tr>";
    return result;
}
function renderReceipt(){
    // set id
    document.title = "Invoice #"+receipt.id;
    $('#invoice-id').text(receipt.id);

    // set date
    $('#invoice-date').text(receipt.timestamp);
    // set member id
    $('#member-id').text(receipt.memberId);

    // set restaurant
    $('#restaurant-id').text(receipt.restaurantId);
    getJSON(true,serviceUrls.restaurant+"/"+receipt.restaurantId,function(data){
        var payload = data.payload;
        $('#restaurant-address').text(payload.address);
        $('#restaurant-phone').text(payload.phone);
    });

    // set date
    $('#invoice-date').text(receipt.timestamp);

    // set member id
    $('#member-id').text(receipt.memberId);

    // set item
    var itemToRowList = [];
    var total = 0;
    receipt.items.forEach(function(data){
        itemToRowList.push(
            toRow(data.name,data.count,"Rp"+data.price)
        );
        total+=data.price*data.count;
    });
    var totalAfterTax = (receipt.tax+100) * total / 100;

    itemToRowList.push(toRow("<strong>Subtotal</strong>","&nbsp;", total));
    itemToRowList.push(toRow("<strong>Total</strong>","Tax : "+receipt.tax+"%", totalAfterTax));

    $('#item-list').empty().append(itemToRowList);
}

function printReceipt() {
    (function () {
        $('#print-btn').hide();
    })();
    window.print();
}

Cookies.remove('receipt');