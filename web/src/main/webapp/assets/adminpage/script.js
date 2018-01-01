$(document).ready(function () {

    var token = Cookies.get("POSRESTAURANT");
    var userData = {};
    var userIdentities = [];

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
        bindShowPanelToClickEvent();
        bindModalsToSubmitEvent();
        bindNewButtonsToModals();
        bindUpdateButtonsToClickEvent();
        bindDeleteButtonsToClickEvent();
    }

    // Set navbar username
    function setUsername() {
        $("#username").html(userData.username);
    }

    // Get and load all data from core service
    function loadAllData() {
        loadUserIdentity().then(function () {
            loadData("user");
        });
        loadData("restaurant");
        loadData("category");
        loadData("item");
    }

    function loadData(dataName) {
        var tableHeader = "<tr><th>"
            + tableHeaders[dataName].join("</th><th>")
            + "</th></tr>";

        $.get(serviceUrls[dataName])
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

    function loadUserIdentity() {
        return $.get(serviceUrls.userIdentity)
            .done(function (data) {
                var userList = data.payload;
                userList.forEach(function (item) {
                    userIdentities[item.id] = item.username;
                });
                console.log(userIdentities);
            })
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

    function modifyModalInterface(modalName, object) {
        var modalForm = $("#modal-new-"+modalName);
        modalForm.find(".modal-element-new").hide();
        modalForm
            .find(".modal-element-edit").show()
            .find(".modal-id").html(object.id);


        fillModalForms(modalName, object);
    }

    function fillModalForms(modalName, object) {
        var template = requestBodyFormat[modalName];
        $.each(template, function (field, elmtId) {
            $("#form-new-"+modalName).find(elmtId).val(object[field]);
        });
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
        row += button + "</tr>";

        return row;
    }

    function sendSpecificGetRequest(dataName, objectId) {
        var getUrl = serviceUrls[dataName] + "/" + objectId;
        return $.get(getUrl)
    }

    // Bind CREATE API to modals submit event
    function bindModalsToSubmitEvent() {
        addSubmitEventToUserModal();
        addSubmitEventToModal("restaurant");
        addSubmitEventToModal("category");
        addSubmitEventToModal("item");
    }

    function addSubmitEventToModal(modalName) {
        $("#form-new-" + modalName).submit(function (e) {
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

    function addSubmitEventToUserModal() {
        $("#form-new-user").submit(function (e) {
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
                    console.log(data);
                })
                .fail(function (jqXHR) {
                    console.log(JSON.parse(jqXHR.responseText));
                });

            e.preventDefault();
            e.stopPropagation();
        });
    }

    function sendCreateRequest(dataName, dataToBeSent) {
        return $.ajax({
            url: serviceUrls[dataName],
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(dataToBeSent)
        }).done(function (data) {
            alert("Successfully created new " + dataName + ".");
            console.log(data.payload);
        }).fail(function (jqXHR) {
            var message = JSON.parse(jqXHR.responseText).message;
            alert("Failed to create new restaurant.\n" + message);

            console.log(jqXHR.responseText);
        });
    }

    function formInputToObject(dataName) {
        var template = requestBodyFormat[dataName];
        var newObject = {};
        $.each(template, function (field, elmtId) {
            newObject[field] = $("#form-new-" + dataName).find(elmtId).val();
        });
        return newObject;
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

            console.log(dataToBeSent);
            sendUpdateRequest(modalName, dataToBeSent)
                .then(function () {
                    loadData(modalName);
                    closeModal(modalName);
                });
        });
    }

    function addClickEventToUserUpdateButton() {
        $("#update-user").click(function (e) {
            var rowId = $("#form-new-user").find(".modal-id").html();
            var dataToBeSent = formInputToObject("user");
            dataToBeSent.id = Number(rowId);

            console.log(dataToBeSent);
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
                    console.log(data);
                })
                .fail(function (jqXHR) {
                    console.log(JSON.parse(jqXHR.responseText));
                });
        });
    }

    function sendUpdateRequest(dataName, dataToBeSent) {
        var updateUrl = serviceUrls[dataName] + "/" + dataToBeSent.id;
        return $.ajax(updateUrl, {
            method: "PUT",
            contentType: "application/json",
            data: JSON.stringify(dataToBeSent)
        }).done(function (data) {
            console.log(data.payload);
            alert("Successfully updated " + dataName + ".")
        }).fail(function (jqXHR) {
            var message = JSON.parse(jqXHR.responseText).message;
            alert("Failed to update " + dataName +".\n" + message);

            console.log(jqXHR.responseText);
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

    function sendDeleteRequest(dataName, objectId) {
        var deletUrl = serviceUrls[dataName] + "/" + objectId;
        return $.ajax(deletUrl, {
            method: "DELETE"
        }).done(function (data) {
            console.log(data);
            alert("Successfully deleted " + dataName + ".")
        }).fail(function (jqXHR) {
            var message = JSON.parse(jqXHR.responseText).message;
            alert("Failed to update " + dataName +".\n" + message);

            console.log(jqXHR.responseText);
        })
    }

    // Bind show panel to button click event
    function bindShowPanelToClickEvent() {
        showPanelButtonOnclick("overview");
        showPanelButtonOnclick("users");
        showPanelButtonOnclick("restaurants");
        showPanelButtonOnclick("categories");
        showPanelButtonOnclick("items");
    }

    function showPanelButtonOnclick(panelName) {
        $("#show-"+panelName).click(function () {
            $(".panel.panel-default").hide();
            $("#panel-"+panelName).show();

            $("a[id|='show']").removeClass('active main-bg-color');
            $(this).addClass('active main-bg-color');
        });
    }

    // Bind logging out to logout button
    $('#logout-btn').click(function (e) {
        Cookies.remove("POSRESTAURANT");
        window.location.assign(config.pages.login);
    });

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

    function revertModalInterface(modalName) {
        var modalForm = $("#modal-new-"+modalName);
        modalForm.find(".modal-element-edit").hide();
        modalForm.find(".modal-element-new").show();

        emptyModalForm(modalName);
    }

    function emptyModalForm(modalName) {
        $("#form-new-" + modalName).find("input.form-control").val("");
    }

    function closeModal(modalName) {
        $("#modal-new-"+modalName).modal("hide");
    }

});