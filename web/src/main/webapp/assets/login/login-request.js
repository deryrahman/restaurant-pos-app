var userToken;

function login(form) {
    var request = new XMLHttpRequest();
    var body = "username="+form.username.value+"&password="+form.password.value;
    console.log(body);

    request.onload = loginHandler;
    request.open("POST", "http://localhost:8080/login");
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send(body);
}

var loginHandler = function () {
    console.log("Onload function called.");
    if (this.status === 200) {
        userToken = this.responseText;
        alert(userToken);
    } else if (this.status === 401) {
        alert(this.responseText);
    } else {
        alert("Unknown error occurred.");
    }
};