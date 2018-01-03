$(document).ready(function () {

    var token = Cookies.get("POSRESTAURANT");
    var userData = {};
    var restaurantData = {};
    var userIdentities = [];
    var panelLists = ["overview", "users",
        "restaurant", "receipts", "itemWithStock",
        "categories", "members"];

    var config;
    var baseUrl;
    var serviceUrls = {};
    var tableStructures = {};
    var tableHeaders = {};
    var requestBodyFormat = {};

    // Get configurations and check cookie
    (function () {
        if (token === undefined) {
            backToLoginPage("You are not logged in. Please login.");
        }

        var configUrl = "configurations.json";
        $.getJSON(configUrl, function (data) {
            config = data;
            baseUrl = config.baseUrl;
            $.each(config.endpoints, function (service, url) {
                serviceUrls[service] = baseUrl + url;
            });
            requestBodyFormat = config.requestBodyFormat;
            tableHeaders = config.tableHeaders;
            tableStructures = config.tableStructures;
        }).then(function () {
            return $.ajax(serviceUrls.parser, {
                method: "POST",
                contentType: "application/x-www-form-urlencoded",
                data: {token: token}
            })
        }).done(function (data) {
            userData = data.payload;
            console.log(userData);
            if (userData.role === "manager") {
                console.log("Logged in as a manager.");
                initializeManagerPage();
            } else {
                backToLoginPage("You are not logged in as a manager. Please login as a manager.");
            }
        }).fail(function () {
            backToLoginPage("Your session is already expired. Please login again.");
        });
    })();

    function backToLoginPage(message) {
        alert(message);
        window.location.replace(config.pages.login);
    }

    // Initialize all functionality
    function initializeManagerPage() {
        setUsername().then(setRestaurant);
        bindAllButtons();
        loadAllData();
    }

    // Set user and restaurant profile
    function setUsername() {
        console.log(userData.id);
        return sendSpecificGetRequest("user", userData.id)
            .then(function (data) {
                userData = data.payload;
                var name = userData.name;
                $("#username").html("Hello, " + name);
                console.log(userData);
            })
    }

    function setRestaurant() {
        return sendSpecificGetRequest("restaurant", userData.restaurantId)
            .then(function (data) {
                restaurantData = data.payload;
                console.log(restaurantData);
                $.each(restaurantData, function (prop, value) {
                    $(".restaurant-" + prop).html(value);
                })
            });
    }

    // Get and load all data from core service
    function loadAllData() {
        loadUserIdentity()
            .then(function () {
                return loadData("user");
            });
        loadData("category");
        loadData("receipt");
        loadData("member");
        loadData("itemWithStock");
    }

    function loadUserIdentity() {
        return $.get(serviceUrls.userIdentity)
            .done(function (data) {
                var userList = data.payload;
                userList.forEach(function (user) {
                    userIdentities[user.id] = user.username;
                });
            })
    }

    function loadData(dataName) {
        var tableHeader = "<tr><th>"
            + tableHeaders[dataName].join("</th><th>")
            + "</th></tr>";

        return $.get(serviceUrls[dataName])
            .done(function (data) {
                var dataList = data.payload;

                $("#"+dataName+"-count-badge").html(dataList.length);
                $("#"+dataName+"-count-well").html(dataList.length);

                $("#"+dataName+"-table").html(tableHeader);
                dataList.forEach(function (item) {
                    if (dataName === "user") {
                        item.username = userIdentities[item.id];
                    }
                    $("#"+dataName+"-table").append(objectToTableRow(dataName, item));
                })
            })
            .then(function () {
                bindEditButtonToModal(dataName);
            });
    }

    function bindEditButtonToModal(modalName) {
        var targetId = "#modal-new-"+modalName;

        $("#"+modalName+"-table").find(".btn-edit")
            .addClass("btn-edit-"+modalName)
            .attr({
                "data-toggle": "modal",
                "data-target": targetId
            });

        $(".btn-edit-"+modalName).click(function (event) {
            var row = $(event.target).parent().siblings();
            var newObject = tableRowToObject(modalName, row);
            console.log(newObject);
            modifyModalInterface(modalName, newObject);
        });
    }

    // Bind all general buttons
    function bindAllButtons() {
        bindShowPanelToClickEvent();
        bindUserButtons();
        bindItemButtons();

        $("#btn-save-edit").click(function (e) {
            var dataToBeSent = formInputToObject("restaurant");
            if (dataToBeSent.address === "") {
                dataToBeSent.address = restaurantData.address;
            }
            if (dataToBeSent.phone === "") {
                dataToBeSent.phone = restaurantData.phone;
            }
            dataToBeSent.id = restaurantData.id;
            sendUpdateRequest("restaurant", dataToBeSent)
                .then(function () {
                    setRestaurant();
                    $("#address").val("");
                    $("#phone").val("");
                });
        });

        $('#logout-btn').click(function (e) {
            Cookies.remove("POSRESTAURANT");
            window.location.assign(config.pages.login);
        });
    }

    // Bind show panel to button click event
    function bindShowPanelToClickEvent() {
        panelLists.forEach(function (panelName) {
            showPanelButtonOnclick(panelName);
        })
    }

    function showPanelButtonOnclick(panelName) {
        $("#show-"+panelName).click(function () {
            $(".panel.panel-default").hide();
            $("#panel-"+panelName).show();

            $("a[id|='show']").removeClass('active main-bg-color');
            $(this).addClass('active main-bg-color');
        });
    }

    // Bind buttons in user panel
    function bindUserButtons() {
        addClickEventToUserSubmitButton();
        addClickEventToUserUpdateButton();
        addClickEventToDeleteButton("user");
        addClickEventToNewButton("user");
    }

    function addClickEventToUserSubmitButton() {
        $("#submit-user").click(function (e) {
            var newUserIdentity = formInputToObject("userIdentity");
            var userList = [];
            userList.push(formInputToObject("user"));

            sendCreateRequest("user", userList)
                .then(function (data) {
                    newUserIdentity.id = data.payload[0].id;

                    return $.ajax(serviceUrls.userIdentity, {
                        method: "POST",
                        contentType: "application/json",
                        data: JSON.stringify(newUserIdentity)
                    });
                })
                .done(function (data) {
                    var newUser = data.payload[0];
                    userIdentities[newUser.id] = newUser.username;
                    console.log(newUser);
                    emptyModalForm("user");
                    loadData("user");
                })
                .fail(function (jqXHR) {
                    console.log(JSON.parse(jqXHR.responseText));
                });

            e.preventDefault();
            e.stopPropagation();
        });
    }

    function addClickEventToUserUpdateButton() {
        $("#update-user").click(function (e) {
            var rowId = $("#form-new-user").find(".modal-id").html();
            var dataToBeSent = formInputToObject("user");
            dataToBeSent.id = Number(rowId);

            sendUpdateRequest("user", dataToBeSent)
                .then(function () {
                    var updateUrl = serviceUrls.userIdentity + "/" + dataToBeSent.id;
                    dataToBeSent = formInputToObject("userIdentity")
                    return $.ajax(updateUrl, {
                        method: "PUT",
                        contentType: "application/json",
                        data: JSON.stringify(dataToBeSent)
                    });
                })
                .done(function (data) {
                    var newUser = data.payload[0];
                    userIdentities[newUser.id] = newUser.username;
                    console.log(newUser);
                    closeModal("user");
                    loadData("user");
                })
                .fail(function (jqXHR) {
                    console.log(JSON.parse(jqXHR.responseText));
                });
            e.preventDefault();
            e.stopPropagation();
        });
    }

    function addClickEventToDeleteButton(modalName) {
        $("#delete-"+modalName).click(function () {
            var rowId = $("#form-new-"+modalName).find(".modal-id").html();
            sendDeleteRequest(modalName, Number(rowId))
                .then(function () {
                    loadData(modalName);
                    closeModal(modalName);
                });
        });
    }

    // Bind buttons in item panel
    function bindItemButtons() {
        addClickEventToItemSubmitButton();
        addClickEventToItemUpdateButton();
        addClickEventToItemDeleteButton();
        addClickEventToNewButton("itemWithStock");
    }

    function addClickEventToItemSubmitButton() {
        $("#submit-item").click(function (e) {
            var dataToBeSent = [];
            var dataName = "item";
            dataToBeSent.push(formInputToObject("itemWithStock"));
            $.ajax({
                url: serviceUrls[dataName] + "/" + dataToBeSent.itemId,
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify(dataToBeSent)
            }).done(function (data) {
                alert("Successfully added stock to item.");

            }).fail(function (jqXHR) {
                var message = JSON.parse(jqXHR.responseText).message;
                alert("Failed to add stock to item.\n" + message);

                console.log(jqXHR.responseText);
            });

            e.preventDefault();
            e.stopPropagation();
        });
    }

    function addClickEventToItemUpdateButton() {
        $("#update-item").click(function (e) {
            var rowId = $("#form-new-itemWithStock").find(".modal-id").html();
            var dataToBeSent = formInputToObject("itemWithStock");
            dataToBeSent.itemId = Number(rowId);

            sendUpdateRequest("itemWithStock", dataToBeSent)
                .then(function () {
                    loadData("itemWithStock");
                    closeModal("itemWithStock");
                });

            e.preventDefault();
            e.stopPropagation();
        });
    }

    function addClickEventToItemDeleteButton() {
        $("#delete-item").click(function () {
            var rowId = $("#form-new-itemWithStock").find(".modal-id").html();
            var deleteUrl = "stock/" + rowId;
            sendDeleteRequest("item", deleteUrl);
        });
    }

    function addClickEventToNewButton(modalName) {
        $("#btn-new-"+modalName).click(function () {
            revertModalInterface(modalName);
            $("#restaurant-id").val(restaurantData.id);
        });
    }

    // Send request to API methods
    function sendSpecificGetRequest(dataName, objectId) {
        var getUrl = serviceUrls[dataName] + "/" + objectId;
        return $.get(getUrl)
    }

    function sendCreateRequest(dataName, dataToBeSent) {
        return $.ajax({
            url: serviceUrls[dataName],
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(dataToBeSent)
        }).done(function (data) {
            alert("Successfully created new " + dataName + ".");

        }).fail(function (jqXHR) {
            var message = JSON.parse(jqXHR.responseText).message;
            alert("Failed to create new " + dataName + ".\n" + message);

            console.log(jqXHR.responseText);
        });
    }

    function sendUpdateRequest(dataName, dataToBeSent) {
        var updateUrl = serviceUrls[dataName] + "/";

        if (dataName === "itemWithStock") {
            updateUrl += "stock/" + dataToBeSent.itemId;
        } else {
            updateUrl += dataToBeSent.id;
        }

        return $.ajax(updateUrl, {
            method: "PUT",
            contentType: "application/json",
            data: JSON.stringify(dataToBeSent)
        }).done(function (data) {
            alert("Successfully updated " + dataName + ".")
        }).fail(function (jqXHR) {
            var message = JSON.parse(jqXHR.responseText).message;
            //var message = "";
            alert("Failed to update " + dataName +".\n" + message);

            console.log(jqXHR.responseText);
        });
    }

    function sendDeleteRequest(dataName, objectId) {
        var deleteUrl = serviceUrls[dataName] + "/" + objectId;
        return $.ajax(deleteUrl, {
            method: "DELETE"
        }).done(function (data) {
            alert("Successfully deleted " + dataName + ".")
        }).fail(function (jqXHR) {
            var message = JSON.parse(jqXHR.responseText).message;
            alert("Failed to update " + dataName +".\n" + message);

            console.log(jqXHR.responseText);
        })
    }


    // Modal manipulating methods
    function modifyModalInterface(modalName, object) {
        var rowId;
        if (modalName === "itemWithStock") {
            rowId = object.itemId;
        } else {
            rowId = object.id;
        }

        var modalForm = $("#modal-new-"+modalName);
        modalForm.find("#submit-"+modalName).attr("type", "button");
        modalForm.find("#update-"+modalName).attr("type", "submit");
        modalForm.find(".modal-element-new").hide();
        modalForm
            .find(".modal-element-edit").show()
            .find(".modal-id").html(rowId);

        fillModalForms(modalName, object);
    }

    function revertModalInterface(modalName) {
        var modalForm = $("#modal-new-"+modalName);
        modalForm.find("#submit-"+modalName).attr("type", "submit");
        modalForm.find("#update-"+modalName).attr("type", "button");
        modalForm.find(".modal-element-edit").hide();
        modalForm.find(".modal-element-new").show();

        emptyModalForm(modalName);
    }

    function fillModalForms(modalName, object) {
        var template = requestBodyFormat[modalName];
        $.each(template, function (field, elmtId) {
            $("#form-new-"+modalName).find(elmtId).val(object[field]);
        });

        var username;
        if (modalName === "user") {
            username = object.username;
            $("#new-username").val(username);
        }
    }

    function emptyModalForm(modalName) {
        $("#form-new-" + modalName).find("input.form-control").val("");
    }

    function closeModal(modalName) {
        $("#modal-new-"+modalName).modal("hide");
    }


    // Utility functions
    function formInputToObject(dataName) {
        var template = requestBodyFormat[dataName];
        var newObject = {};
        $.each(template, function (field, elmtId) {
            newObject[field] = $("#form-new-" + dataName).find(elmtId).val();
        });
        return newObject;
    }

    function tableRowToObject(dataName, row) {
        var objectProperties = tableStructures[dataName];
        var newObject = {};

        var propertyName;
        for (var i = 0; i < row.length; i++) {
            propertyName = objectProperties[i];
            newObject[propertyName] = row[i].innerHTML;
        }

        return newObject;
    }

    function objectToTableRow(dataName, object) {
        var columns = tableStructures[dataName];
        var button;
        if(dataName === "user") {
            button = "<td><a class='btn-edit' role='button'>edit</a></td>";
        } else if (dataName === "itemWithStock") {
            button = "<td><a class='btn-edit' role='button'>restock</a></td>";
        } else {
            button = "";
        }

        var rowId = dataName + object.id;
        var row = "<tr id='"+rowId+"'>";
        columns.forEach(function (columnName) {
            row += "<td>" + object[columnName] + "</td>";
        });
        row += button + "</tr>";

        return row;
    }


}).ajaxSend(function (event, jqXHR, settings) {
    if (settings.method === "PUT" ||
        settings.method === "POST" ||
        settings.method === "DELETE") {
        $("input, button").prop("disabled", true);
        // $(".modal-body")
        //     .find("input, button")
        //     .prop("disabled", true);
    } else {
        // $(".modal-body")
        //     .find("input, button")
        //     .prop("disabled", false);
        $("input, button").prop("disabled", false);
        $("#restaurant-id").prop("disabled", true);
    }
}).ajaxComplete(function (event, jqXHR, settings) {
    if (settings.method === "PUT" ||
        settings.method === "POST" ||
        settings.method === "DELETE") {
        // $(".modal-body")
        //     .find("input, button")
        //     .prop("disabled", false);
        $("input, button").prop("disabled", false);
        $("#restaurant-id").prop("disabled", true);
    }
});