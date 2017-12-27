var coreService = "http://localhost:8080/restaurant"
var authService = "http://localhost:8080/auth"

function addItem(itemId, name, price) {
    // var item = getItemById(itemId)

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
        editModalHTML(itemId,name,cnt)
    } else {
        console.log(itemId)
        var result = "<div id='sidebar-item-" + itemId + "' class='invoice-item padding-v-default invoice-item-with-delete pointer row' data-toggle='modal' data-target='#edit-item-modal-" + itemId + "'>" +
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
        editModalHTML(itemId,name,1)
        $("#invoice-item-list").append(result)
    }
}

function deleteModalHTML(itemId) {
    if($('#delete-item-modal-').length===0){
        var result = "<div id='delete-item-modal-"+itemId+"' class='modal fade' role='dialog'>" +
        "<div class='modal-dialog modal-sm'>" +
        "<div class='modal-content'>" +
        "<div class='modal-header'>" +
        "<h4 class='modal-title'>Are you sure delete this item?</h4>" +
        "</div>" +
        "<div class='modal-footer'>" +
        "<button type='button' class='btn btn-danger' data-dismiss='modal'>Delete</button>" +
        "<button type='button' class='btn btn-default' data-dismiss='modal'>Cancel</button>" +
        "</div>" +
        "</div>" +
        "</div>" +
        "</div>"
        $('#delete-item-modal').append(result)
    }
}

function editModalHTML(itemId, name, itemCount){
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
    deleteModalHTML(itemId)
    $('#edit-item-modal').append(result)
}

function saveEdit(itemId){
    var cnt = parseInt($('input[name=edit-count-'+itemId+']').val());
    var stock = parseInt($('.item-stock-'+itemId).first().text()) + parseInt($('#sidebar-count-item-'+itemId).text());
    console.log(stock)
    stock-=cnt
    console.log(stock)
    if(stock<0){
        alert("Stock habis!")
        return;
    }

    $('.item-stock-'+itemId).each(function(){
       $(this).text(stock);
    });
    $('#sidebar-count-item-'+itemId).text(cnt);
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