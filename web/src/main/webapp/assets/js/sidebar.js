var coreService = "http://localhost:8080/restaurant"
var authService = "http://localhost:8080/auth"
var tax = 5
function loadSidebar(){
    $('#invoice-tax').text(tax)
    hideReceiptButton()
}

function addItem(itemId, name, price) {

    var stock = parseInt($('.item-stock-'+itemId).first().text());
    stock-=1
    if(stock<0){
        alert("Stock habis!")
        return;
    }
    $('.item-stock-'+itemId).each(function(){
        $(this).text(stock);
    });
    if($('#sidebar-item-'+itemId).length){
        console.log(itemId)
        var cnt = parseInt($('#sidebar-count-item-'+itemId).text());
        cnt+=1
        $('#sidebar-count-item-'+itemId).text(cnt);
        var price = parseInt($('#sidebar-price-item-'+itemId).text());
        $('#sidebar-subtotal-item-'+itemId).text(price*cnt);
        renderEditModal(itemId,name,cnt)
    } else {
        console.log(itemId)
        var result = "<div id='sidebar-item-" + itemId + "' class='sidebar-item invoice-item padding-v-default invoice-item-with-delete pointer row' data-toggle='modal' data-target='#edit-item-modal-" + itemId + "'>" +
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
            "Rp" + "<span class='subtotal-item' id='sidebar-subtotal-item-"+itemId+"'>" + price + "</span>" +
            "</div>" +
            "</div>" +
            "</div>";
        renderEditModal(itemId,name,1)
        $("#invoice-item-list").append(result)
    }
    var total = renderSubTotal(tax)
    renderTotal(total,tax)
    showReceiptButton()
}

function renderDeleteModal(itemId) {
    if($('#delete-item-modal-').length===0){
        var result = "<div id='delete-item-modal-"+itemId+"' class='modal fade' role='dialog'>" +
        "<div class='modal-dialog modal-sm'>" +
        "<div class='modal-content'>" +
        "<div class='modal-header'>" +
        "<h4 class='modal-title'>Are you sure delete this item?</h4>" +
        "</div>" +
        "<div class='modal-footer'>" +
        "<button type='button' class='btn btn-danger' data-dismiss='modal' onclick='deleteItem("+itemId+")'>Delete</button>" +
        "<button type='button' class='btn btn-default' data-dismiss='modal'>Cancel</button>" +
        "</div>" +
        "</div>" +
        "</div>" +
        "</div>"
        $('#delete-item-modal').append(result)
    }
}

function renderEditModal(itemId, name, itemCount){
    if($('#edit-item-modal-'+itemId).length){
        $('#edit-item-modal-'+itemId).remove()
    }
    var result = "<div id='edit-item-modal-"+itemId+"' class='modal fade' role='dialog'>" +
    "<div class='modal-dialog modal-sm'>" +
    "<div class='modal-content'>" +
    " <div class='modal-header'>" +
    "<h4 class='modal-title'>Edit "+name+"</h4>" +
    "</div>" +
    "<div class='modal-body'>"+
    "<p>Count :</p>" +
    "<input type='text' name='edit-count-"+itemId+"' value='"+itemCount+"'>" +
    "<div class='modal-footer'>" +
    "<button class='btn btn-danger col-xs-12' data-toggle='modal' data-target='#delete-item-modal-"+itemId+"'><span class='glyphicon glyphicon-trash'></span> Remove</button>" +
    "</div>" +
    "</div>" +
    "<div class='modal-footer'>" +
    "<button type='button' class='btn btn-danger' data-dismiss='modal'>Cancel</button>"+
    "<button type='button' class='btn btn-success' data-dismiss='modal' onclick='saveEdit("+itemId+")'>Save</button>"+
    "</div>"+
    "</div>"+
    "</div>"+
    "</div>"
    renderDeleteModal(itemId)
    $('#edit-item-modal').append(result)
}

function saveEdit(itemId){
    var cnt = parseInt($('input[name=edit-count-'+itemId+']').val());
    if(cnt<=0){
        alert("Cannot zero or negative")
        return;
    }
    var stock = parseInt($('.item-stock-'+itemId).first().text()) + parseInt($('#sidebar-count-item-'+itemId).text());
    console.log(stock)
    stock-=cnt
    console.log(stock)
    if(stock<0){
        alert("Stock habis!")
        return;
    }

    var price = parseInt($('#sidebar-price-item-'+itemId).text());
    $('#sidebar-subtotal-item-'+itemId).text(price*cnt);

    $('.item-stock-'+itemId).each(function(){
       $(this).text(stock);
    });
    $('#sidebar-count-item-'+itemId).text(cnt);
    var total = renderSubTotal(tax)
    renderTotal(total,tax)
}

function deleteItem(itemId) {
    var stock = parseInt($('.item-stock-'+itemId).first().text()) + parseInt($('#sidebar-count-item-'+itemId).text());
    $('.item-stock-'+itemId).each(function(){
        $(this).text(stock);
    });

    $('#edit-item-modal-'+itemId).modal('hide')
    $('#sidebar-item-'+itemId).remove()
    var total = renderSubTotal(tax)
    renderTotal(total,tax)
    var n = $('.sidebar-item').length
    console.log("length : " + n)
    if(n == 0){
        hideReceiptButton()
    }
}

function renderSubTotal(tax){
    var price = 0
    $('.subtotal-item').each(function(){
        price += parseInt($(this).text())
    });

    $('#invoice-subtotal').text(price)
    $('#invoice-subtotal-tax').text(tax*price/100)
    return price
}

function renderTotal(price,tax){
    $('#invoice-total').text(price*(100+tax)/100)
}

function deleteAllOnReceipt() {
    $('.sidebar-item').each(function(){
        var id = this.id.split("-")
        var itemId = parseInt(id[id.length-1])
        var cnt = parseInt($('#sidebar-count-item-'+itemId).text())
        var stock = parseInt($('.item-stock-'+itemId).first().text()) + cnt
        $('.item-stock-'+itemId).each(function(){
            $(this).text(stock);
        });
    });

    $('#invoice-item-list').empty()
    renderTotal(renderSubTotal(tax),tax)
    hideReceiptButton()
}

function hideReceiptButton() {
    $("#receipt-button").children().hide();
}
function showReceiptButton() {
    $("#receipt-button").children().show();
}