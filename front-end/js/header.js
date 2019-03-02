
$(document).ready(function () {
    if (document.title == "Crow Tech") {
        document.getElementById("search-bar").style.display = "none";
    }
    var token = window.sessionStorage.getItem("token");

    var username = "";
    if (token == null) {
        username = "Guest";
        document.getElementById("logout").style.display = "none";
        document.getElementById("fav-button").style.display = "none";
        document.getElementById("add-button").style.display = "none";
    }
    else {
        username = window.sessionStorage.getItem("username");
        document.getElementById("login").style.display = "none";
    }
    document.getElementById("username").textContent = username;
    document.getElementById("username-l").textContent = username;

});
