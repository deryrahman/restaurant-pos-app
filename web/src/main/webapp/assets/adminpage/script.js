$(document).ready(function () {

    var token = Cookies.get("POSRESTAURANT");
    var userData = {};
    var userIdentities = [];

    var config;
    var baseUrl;
    var serviceUrl;
    var serviceUrls = {};
    var tableStructures = {};
    var tableHeaders = {};
    var requestBodyFormat = {};

    var dataLists = {};

    // Get configurations and check cookie
    (function () {
        if (token === undefined) {
            backToLoginPage("You are not logged in. Please login.");
        }

        var configUrl = "configurations.json";
        $.getJSON(configUrl, function (data) {
            config = data;
            baseUrl = config.baseUrl;
            serviceUrl = config.serviceUrl;
            $.each(config.endpoints, function (service, url) {
                serviceUrls[service] = serviceUrl + url;
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
            if (userData.role === "admin") {
                console.log("Logged in as an admin.");
                initializeAdminPage();
            } else {
                backToLoginPage("You are not logged in as an admin. Please login as an admin.");
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
    function initializeAdminPage() {
        setUsername();
        loadAllData();
        bindAllButtons();
    }

    // Set navbar username
    function setUsername() {
        sendSpecificGetRequest("user", userData.id)
            .done(function (data) {
                var name = data.payload.name;
                $("#username").html("Hello, " + name);
            })
    }

    // Get and load all data from core service
    function loadAllData() {
        loadUserIdentity().then(function () {
            loadData("user");
        });
        loadData("restaurant").then(function (data) {
            console.log(dataLists);
            bindLedgerButton();
        });
        loadData("category");
        loadData("item");
    }

    function loadUserIdentity() {
        return $.get(serviceUrls.userIdentity)
            .done(function (data) {
                var userList = data.payload;
                userList.forEach(function (item) {
                    userIdentities[item.id] = item.username;
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
                dataLists[dataName] = dataList;

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
                if (dataName === "restaurant") {
                    loadRstrId();
                }
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

            modifyModalInterface(modalName, newObject);
        });
    }

    // Bind all general buttons
    function bindAllButtons() {
        bindShowPanelToClickEvent();
        bindModalsToSubmitEvent();
        bindNewButtonsToModals();
        bindUpdateButtonsToClickEvent();
        bindDeleteButtonsToClickEvent();
    }

    // Bind show panel to button click event
    function bindShowPanelToClickEvent() {
        showPanelButtonOnclick("overview");
        showPanelButtonOnclick("users");
        showPanelButtonOnclick("restaurants");
        showPanelButtonOnclick("categories");
        showPanelButtonOnclick("items");
        showPanelButtonOnclick("ledger");
    }

    function showPanelButtonOnclick(panelName) {
        $("#show-"+panelName).click(function () {
            $(".panel.panel-default").hide();
            $("#panel-"+panelName).show();

            $("a[id|='show']").removeClass('active main-bg-color');
            $(this).addClass('active main-bg-color');
        });
    }

    // Bind CREATE API to modals submit event
    function bindModalsToSubmitEvent() {
        addClickEventToUserSubmitButton();
        addClickEventToSubmitButton("restaurant");
        addClickEventToSubmitButton("category");
        addClickEventToSubmitButton("item");
    }

    function addClickEventToSubmitButton(modalName) {
        $("#submit-" + modalName).click(function (e) {
            var dataToBeSent = [];
            dataToBeSent.push(formInputToObject(modalName));
            sendCreateRequest(modalName, dataToBeSent)
                .then(function () {
                    emptyModalForm(modalName);
                    loadData(modalName);
                });

            e.preventDefault();
            e.stopPropagation();
        });
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

    // Bind UPDATE API to update button click event
    function bindUpdateButtonsToClickEvent() {
        addClickEventToUserUpdateButton();
        addClickEventToUpdateButton("restaurant");
        addClickEventToUpdateButton("category");
        addClickEventToUpdateButton("item");
    }

    function addClickEventToUpdateButton(modalName) {
        $("#update-"+modalName).click(function (e) {
            var rowId = $("#form-new-"+modalName).find(".modal-id").html();
            var dataToBeSent = formInputToObject(modalName);
            dataToBeSent.id = Number(rowId);

            sendUpdateRequest(modalName, dataToBeSent)
                .then(function () {
                    loadData(modalName);
                    closeModal(modalName);
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

    // Bind DELETE API to delete button click event
    function bindDeleteButtonsToClickEvent() {
        addClickEventToDeleteButton("user");
        addClickEventToDeleteButton("restaurant");
        addClickEventToDeleteButton("category");
        addClickEventToDeleteButton("item");
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

    // Bind modals to New button click event
    function bindNewButtonsToModals() {
        addClickEventToNewButton("user");
        addClickEventToNewButton("restaurant");
        addClickEventToNewButton("category");
        addClickEventToNewButton("item");
    }

    function addClickEventToNewButton(modalName) {
        $("#btn-new-"+modalName).click(function () {
            revertModalInterface(modalName);
        });
    }

    // Bind logging out to logout button
    $('#logout-btn').click(function (e) {
        Cookies.remove("POSRESTAURANT");
        window.location.assign(config.pages.login);
    });

    //Bind ledger button
    function bindLedgerButton() {
        loadRstrId();

        var datepickerSettings = {
            format: "yyyy-mm-dd",
            todayHighlight: true,
            autoclose: true
        };

        var startDatepicker = $("#start-date");
        startDatepicker.datepicker(datepickerSettings);

        var endDatepicker = $("#end-date");
        endDatepicker.datepicker(datepickerSettings);

        $("#get-ledger").click(function () {
            var rstrId = $("#ledger-id").val();
            var stParam = startDatepicker.find("input").val();
            var etParam = endDatepicker.find("input").val();
            var ledgerUrl = serviceUrls.ledger + "?id=" + rstrId
                + "&st=" + stParam + "&et=" + etParam;

            if (rstrId === "") {
                alert("Please specify the restaurant ID.");
            } else if (stParam === "" || etParam === "") {
                alert("Please specify the date range.");
            } else {
                console.log(ledgerUrl);
                $.get(ledgerUrl)
                    .done(showLedger)
                    .fail(showAlert);
            }

        });
    }

    function loadRstrId() {
        var target = $("#ledger-id");
        target.html("<option value=\"\">-</option>");
        dataLists.restaurant.forEach(function (r) {
            target.append("<option value='"+r.id+"'>"+r.id+"</option>");
        });
    }

    function showLedger(data) {
        var ledger = data.payload;
        var total = "<strong>" + ledger.total + "</strong>";
        var rstrId = ledger.restaurantId;
        var receipts = ledger.receipts;

        var tableHeader = "<tr><th>"
            + tableHeaders.ledger.join("</th><th>")
            + "</th></tr>";

        footer = ["<strong>TOTAL</strong>", "", "", "", "", total];
        var tableFooter = "<tr><td>"
            + footer.join("</td><td>") + "</td></tr>";

        $("#ledger-table").html(tableHeader);
        receipts.forEach(function (r) {
            $("#ledger-table").append(objectToTableRow("ledger", r));
        });
        $("#ledger-table").append(tableFooter);
    }

    function showAlert(jqXHR) {
        if (jqXHR.status === 404) {
            alert("No receipt found for given date range");
        } else {
            alert("Failed to get receipt.");
        }
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
        var updateUrl = serviceUrls[dataName] + "/" + dataToBeSent.id;
        return $.ajax(updateUrl, {
            method: "PUT",
            contentType: "application/json",
            data: JSON.stringify(dataToBeSent)
        }).done(function (data) {
            alert("Successfully updated " + dataName + ".")
        }).fail(function (jqXHR) {
            var message = JSON.parse(jqXHR.responseText).message;
            alert("Failed to update " + dataName +".\n" + message);

            console.log(jqXHR.responseText);
        });
    }

    function sendDeleteRequest(dataName, objectId) {
        var deletUrl = serviceUrls[dataName] + "/" + objectId;
        return $.ajax(deletUrl, {
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
        var modalForm = $("#modal-new-"+modalName);
        modalForm.find("#submit-"+modalName).attr("type", "button");
        modalForm.find("#update-"+modalName).attr("type", "submit");
        modalForm.find(".modal-element-new").hide();
        modalForm
            .find(".modal-element-edit").show()
            .find(".modal-id").html(object.id);

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
        var button = "<td><a class='btn-edit' role='button'>edit</a></td>";

        var rowId = dataName + object.id;
        var row = "<tr id='"+rowId+"'>";
        columns.forEach(function (columnName) {
            row += "<td>" + object[columnName] + "</td>";
        });

        if (dataName !== "ledger") {
            row += button + "</tr>";
        }

        return row;
    }

}).ajaxSend(function (event, jqXHR, settings) {
    if (settings.method === "PUT" ||
        settings.method === "POST" ||
        settings.method === "DELETE") {
        $(".modal-body")
            .find("input, button")
            .prop("disabled", true);
    } else {
        $(".modal-body")
            .find("input, button")
            .prop("disabled", false);
    }
}).ajaxComplete(function (event, jqXHR, settings) {
    if (settings.method === "PUT" ||
        settings.method === "POST" ||
        settings.method === "DELETE") {
        $(".modal-body")
            .find("input, button")
            .prop("disabled", false);
    }
});