$(document).ready(function() {

    var config;
    var baseUrl;
    var loginUrl;
    var registerUserUrl;
    var registerIdentityUrl;

    var configUrl = "configurations.json";
    $.getJSON(configUrl, function (data) {
        config = data;
        baseUrl = config.baseUrl;
        loginUrl = baseUrl + config.endpoints.login;
        registerIdentityUrl = baseUrl + config.endpoints.register;
        registerUserUrl = baseUrl + config.endpoints.user;
    });

    $('#login-form').submit(function (e) {
        var data = {
            username: $(this).find("#username").val(),
            password: $(this).find("#password").val()
        };

        $.ajax(loginUrl, {
            method: "POST",
            contentType: "application/x-www-form-urlencoded",
            data: data,
            success: successfulLogin,
            statusCode: {
                401: invalidLogin
            },
            error: errorHandler
        });
        e.preventDefault();
    });

    var successfulLogin = function (token) {
        Cookies.set('POSRESTAURANT', token);
        console.log(token);

        var parserUrl = baseUrl + config.endpoints.parser;
        var userInfo;
        $.ajax(parserUrl, {
            method: "POST",
            url: parserUrl,
            contentType: "application/x-www-form-urlencoded",
            data: {token: token},
            success: function (response) {
                userInfo = response.payload;
                if (userInfo.role === "cashier") {
                    window.location.assign(config.pages.home);
                } else if (userInfo.role === "admin") {
                    window.location.assign(config.pages.admin);
                } else if (userInfo.role === "manager") {
                    window.location.assign(config.pages.manager);
                }
                console.log(userInfo);
            }
        });
    };

    var invalidLogin = function () {
        $('#credential-warning').show();
    };

    var errorHandler = function (jqXHR) {
        if (jqXHR.status !== 401) {
            alert(jqXHR.responseText);
        }
    };

    $('#register-form').submit(function (e) {
        var userData = [{
            name: $(this).find("#fullname").val(),
            restaurantId: $(this).find("#restaurant-id").val(),
            email: $(this).find("#email").val()
        }];
        console.log("userData: ");
        console.log(userData);

        var userIdentity = {
            username: $(this).find("#new-username").val(),
            password: $(this).find("#new-password").val(),
            role: "cashier"
        };
        console.log("userIdentity: ");
        console.log(userIdentity);
        

        if (isFormValid()) {
            console.log("Form is valid.");
            
            $.ajax(registerUserUrl, {
                method: "POST",
                contentType: "application/json",
                data: userData
            }).done(function (data) {
                console.log("Successfully registered user. Response:");
                console.log(data);
                userIdentity.id = data.payload[0].id;

                registerIdentity(userIdentity);
            })
        }

        e.preventDefault();
    });

    var registerIdentity = function (userIdentity) {
        console.log("Registerint identity...");
        console.log(userIdentity);

        $.ajax(registerIdentityUrl, {
            method: "POST",
            contentType: "application/json",
            data: userIdentity
        }).done(function (data) {
            console.log("Successful register user with id = " + data.userId);
        })
    };

    var isFormValid = function () {
        var validation = {
            fullname: isFullnameValid(),
            username: isUsernameValid(),
            password: isPasswordValid(),
            email: isEmailValid(),
            restaurantId: isRestaurantIdValid()
        };
        var isValid = true;

        $.each(validation, function (key, value) {
            isValid = isValid && value;
        });
        return  isValid;
    };

    var isFullnameValid = function () {
        var warning = $('#fullname-warning');
        if (isEmpty('#fullname')) {
            warning.html("Please insert your name.").show();
        } else {
            warning.hide();
            return true;
        }

        return false;
    };

    var isUsernameValid = function () {
        var warning = $('#username-warning');
        if (isEmpty('#new-username')) {
            warning
                .html("Please insert a username.")
                .show();
        } else if ($('#new-username').val().length > 20) {
            warning
                .html("Username cannot be longer than 20 characters.")
                .show();
        } else {
            warning.hide();
            return true;
        }

        return false;
    };

    var isPasswordValid = function () {
        var warning = $('#password-warning');
        if (isEmpty('#new-password')) {
            warning
                .html("Please insert a password.")
                .show();
        } else if ($('#new-password').val() !== $('#confirm-password').val()) {
            warning
                .html("Password didn't match.")
                .show();
        } else {
            warning.hide();
            return true;
        }
        return false;
    };

    var isEmailValid = function () {
        var warning = $('#email-warning');
        if (isEmpty('#email')) {
            warning
                .html("Please insert your email address.")
                .show();
        } else if (!$('#email').val().contains("@")) {
            warning
                .html("Please insert a valid email address.")
                .show();
        } else {
            warning.hide();
            return true;
        }

        return false;
    };

    var isRestaurantIdValid = function () {
        var restaurantId = $('#restaurant-id').val();
        var restaurantUrl = baseUrl + config.endpoints.restaurant + "/" + restaurantId;

        var warning = $('#restaurant-warning');
        var isRestaurantExist = $.get(restaurantUrl).status === 200;
        if (isEmpty('#restaurant-id') || !isRestaurantExist) {
            warning
                .html("Please insert a valid restaurant ID.")
                .show();
        } else {
            warning.hide();
            return true;
        }

        return false;
    };

    var isEmpty = function(element) {
        var value = $(element).val();
        return value === "" || value === undefined;
    };

    $('#confirm-password').keyup(function () {
        var newPassword = $('#new-password').val();
        var confirmPassword = $(this).val();
        if (newPassword === confirmPassword) {
            $(this).removeClass('invalid');
        } else {
            $(this).addClass('invalid');
        }
    });

    $('#login-form-link').click(function(e) {
        $("#login-form").delay(100).fadeIn(100);
        $("#register-form").fadeOut(100);
        $('#register-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });
    $('#register-form-link').click(function(e) {
        $("#register-form").delay(100).fadeIn(100);
        $("#login-form").fadeOut(100);
        $('#login-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });
});
