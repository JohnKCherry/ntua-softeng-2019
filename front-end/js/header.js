
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
        document.getElementById("username-l").textContent = username;
        $("#username").hide();
    }
    else {
        username = window.sessionStorage.getItem("username");
        document.getElementById("login").style.display = "none";
        document.getElementById("username").textContent = username;
        document.getElementById("username-l").textContent = username;
        $("#username").show();
        $("#username").replaceWith("<a href=\"profile.html\" class=\"dropdown-item \" role=\"button\">"+username+"</a>");
    }


    //clear header search bar
    $("#search-bar").children('input').val("");
    $("#search-bar").on('submit', function() {
        console.log("header.js: search bar patithike");
        var query = $(this).children('input').val();
        query = query.split(' ').join('+');
        console.log("header.js: query = " + query);
        window.location.href = 'products.html?query='+query;

        return false;
    });

});
