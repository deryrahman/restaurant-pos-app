var userToken;

function login(form) {
    var request = new XMLHttpRequest();
    var body = "username="+form.username.value+"&password="+form.password.value;
    console.log(body);

    request.onload = function () {
        console.log("Onload function called.");
        if (this.status === 200) {
            userToken = this.responseText;
            alert(userToken);
            console.log(userToken);
        } else if (this.status === 401) {
            alert(this.responseText);
            console.log(this.responseText);
        } else {
            alert("Unknown error occurred.");
        }
    };
    request.open("POST", "http://localhost:8080/login");
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send(body);
}

function test(test) {
    console.log(test);
    console.log(test.username.value);
    console.log(test.password.value);
}