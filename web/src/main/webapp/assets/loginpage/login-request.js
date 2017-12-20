var userToken;
var config;
loadConfig("configurations.json");

function login(form) {
    var request = new XMLHttpRequest();
    var body = "username="+form.username.value+"&password="+form.password.value;
    console.log(body);

    request.onload = loginHandler;
    request.onerror = errorHandler;
    request.open("POST", config.endpoints.login);
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send(body);
}

var loginHandler = function () {
    if (this.status === 200) {
        userToken = this.responseText;
        successfulLoginHandler(userToken);
    } else if (this.status === 401) {
        console.log(this.responseText);
        invalidCredentialsHandler();
    } else {
        alert("Unknown error occurred.");
    }
};

var successfulLoginHandler = function (token) {
    alert(token);
};

var errorHandler = function () {
    alert("Unexpected error occurred.");
};

var invalidCredentialsHandler = function () {
    document.getElementById("credential-warning").style.display = "block";
};

function loadConfig(fileName) {
    var request = new XMLHttpRequest();
    request.overrideMimeType("application/json");

    request.onload = function () {
        config = JSON.parse(this.responseText);
    };
    request.open("GET", fileName);
    request.send();
}

