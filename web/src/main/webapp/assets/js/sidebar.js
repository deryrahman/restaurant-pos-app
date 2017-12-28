var coreService = "http://localhost:8080/restaurant"
var authService = "http://localhost:8080/auth"
var tax = 5
var itemIdCountList;


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
        var cnt = parseInt($('#sidebar-count-item-'+itemId).text());
        cnt+=1
        $('#sidebar-count-item-'+itemId).text(cnt);
        var price = parseInt($('#sidebar-price-item-'+itemId).text());
        $('#sidebar-subtotal-item-'+itemId).text(price*cnt);
        renderEditModal(itemId,name,cnt)
    } else {
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
    stock-=cnt
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

function renderPayment(){
    var total = parseInt($('#invoice-total').text())
    var itemList = []
    itemIdCountList = {}
    $('.sidebar-item').each(function(){
        var id = this.id.split("-")
        var itemId = parseInt(id[id.length-1])
        var name = $('#name-item-'+itemId).text()
        var cnt = parseInt($('#sidebar-count-item-'+itemId).text())
        var result = "<div id='payment-item-"+itemId+"' class='row'>" +
        "<div class='payment-item-name col-xs-8'>"+name+"</div>" +
        "<div class='payment-item-count col-xs-4'>"+cnt+"</div>" +
        "</div>";
        itemList.push(result)
        itemIdCountList[itemId] = cnt
    });
    $('#payment-list').empty().append(itemList)
    $('#payment-total').text(total)
}

function proceedReceipt(){
    var itemList = []
    $.each(itemIdCountList, function (key,val) {
        var item = {
            "itemId" : key,
            "count" : val
        }
        itemList.push(item)
    });
    var receiptList = []
    var receipt = {
        "tax" : tax,
        "items" : itemList
    };
    var memberId = $('#member-id').text()
    var noteReceipt = $('#receipt-note').text()
    if(memberId != ''){
        receipt['memberId'] = parseInt(memberId)
    }
    if(noteReceipt != ''){
        receipt['note'] = noteReceipt
    }
    receiptList.push(receipt)
    console.log(receiptList)
    $.ajax(coreService+"/receipts", {
        data : JSON.stringify(receiptList),
        contentType : 'application/json',
        type : 'POST',
        async : false,
        success: function (){
            alert("Receipt has been added")
            window.location.assign(config.pages.home);
            $('#invoice-item-list').empty()
        },
        error: function (data) {
            console.log(data["message"])
            alert("Failed to make receipt!")
        }
    });
}

function addMember(){
    var memberId = $('input[name=member-id]').val()
    var isId = $.trim(memberId) != ''
    var regisEmail = $('input[name=registration-member-email]').val()
    var regisName = $('input[name=registration-member-name]').val()
    var regisAddress = $('input[name=registration-member-address]').val()
    var isNew = $.trim(regisName) != '' || $.trim(regisAddress) != '' || $.trim(regisEmail) != ''
    console.log(isNew)
    console.log(isId)
    if(isId ^ isNew){
        if(isNew) {
            var memberList = []
            var member = {
                "name" : regisName,
                "address" : regisAddress,
                "email" : regisEmail
            }
            memberList.push(member)
            addNewMember(memberList)
        } else {
            addMemberById(memberId)
        }
        $('#add-member-modal').modal('hide')
    } else {
        alert("Fill only once")
    }
}

function addNewMember(memberList) {
    console.log(memberList)
    $.ajax(coreService+"/members", {
        data : JSON.stringify(memberList),
        contentType : 'application/json',
        type : 'POST',
        async : false,
        success: function (data){
            var payload = data['payload']
            alert("Member "+payload[0]['name']+" has been added")
            addMemberById(payload[0]['id'])
        },
        error: function () {
            console.log(data['message'])
            alert("Failed to make receipt!")
        }
    });
}

function addMemberById(memberId) {
    console.log(coreService+"/members/"+memberId)
    $.getJSON(coreService+"/members/"+memberId, function (data) {
        var payload = data["payload"]
        alert(payload["name"])
        showMember(payload["name"], payload["id"])
    })
        .fail(function(data) {
            console.log(data["message"])
            alert("Member not found")
            return;
        })
}

function showMember(name,id) {
    $('#add-member').hide()
    $('#member-name').show()
    $('#member-name').text(name)
    $('#member-id').text(id)
    $('#remove-member-name').show()
}

function removeMember(){
    $('#add-member').show()
    $('#member-name').hide()
    $('#member-name').empty()
    $('#member-id').empty()
    $('#remove-member-name').hide()
}