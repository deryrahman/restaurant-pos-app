var coreService = "http://localhost:8080/restaurant"
var authService = "http://localhost:8080/auth"

function addItem(itemId, name, price) {
    // var item = getItemById(itemId)
    if($('#sidebar-item-'+itemId).length){
        console.log(itemId)
        var cnt = parseInt($('#sidebar-count-item-'+itemId).text());
        cnt+=1
        $('#sidebar-count-item-'+itemId).text(cnt);
        var price = parseInt($('#sidebar-price-item-'+itemId).text());
        $('#sidebar-subtotal-item-'+itemId).text(price*cnt);
    } else {
        console.log(itemId)
        var result = "<div id='sidebar-item-" + itemId + "' class='invoice-item padding-v-default invoice-item-with-delete pointer row' data-toggle='modal' data-target='#delete-item-modal-" + itemId + "'>" +
            "<div id='name-item-"+itemId+"' class='col-xs-6 invoice-item-name'> +" +
            name +
            "</div>" +
            "<div id='sidebar-count-item-"+itemId+"' class='col-xs-2 invoice-item-count'>" +
            1 +
            "</div>" +
            "<div class='col-xs-4 invoice-item-price align-right'>" +
            "<div class='row padding-default'>" +
            "Rp" + "<span id='sidebar-price-item-"+itemId+"'>" + price + "</span>" +
            "</div>" +
            "<div class='row padding-default'>" +
            "Rp" + "<span id='sidebar-subtotal-item-"+itemId+"'>" + price + "</span>" +
            "</div>" +
            "</div>" +
            "</div>";
        console.log(result)
        $("#invoice-item-list").append(result)
    }
}

function getItemById(itemId) {
    var item = null
    $.ajax({
        url: coreService+"/items/"+itemId,
        dataType: 'json',
        async: false,
        success: function(data) {
            if(!data["success"]){
                console.log(data["message"])
                return;
            }
            item = data["payload"]
        }
    });
    return item
}