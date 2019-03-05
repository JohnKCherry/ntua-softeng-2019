var token = window.sessionStorage.getItem("token");

$(document).ready(function(){

    console.log("Add shop ready");
    console.log("Token:");
    console.log(token);

    if (token != null) {
        $("#errorForm").empty();
    }
    else {
        console.log("Not connected.");
        $("#loginModal").modal('show');
        $("#submitBtn").prop('disabled',true);
        $("#errorForm").text("You need to login to add new shop");
    }

    function addShop(event) {
        event.preventDefault();
        event.stopPropagation();
        $("#successForm").empty();
        $("#error").empty();
        var form = $("#formAddShop");

        /*if (!form[0].checkValidity()) {event.preventDefault(); event.stopPropagation();}    // ?????*/
        if ($("#shopName").val()=="" || $("#shopAddress").val()=="" || $("#shopTags").val()=="" || mylatlng === undefined) {
            $("#error").text("Please fill all form");
            alert("Please fill all forms and select location");
            return;
        }
            

        console.log("Check passed");
        form.addClass('was-validated');
        console.log("Shop name: " + $("#shopName").val());
        console.log("Shop address: " + $("#shopAddress").val());
        console.log("Shop location: " + mylatlng.toString());
        console.log("Shop tags: " + $("#shopTags").val());
        $.ajax({
            type: "POST",
            dataType: "json",
            headers: {'X-OBSERVATORY-AUTH': token},
            url: "https://localhost:8765/observatory/api/shops",
            data: {
                "name": $("#shopName").val(),
                "address": $("#shopAddress").val(),
                "lng": mylatlng.lng,
                "lat": mylatlng.lat,
                "tags": $("#shopTags").val()
            },
            success: function(data) {
                console.log("Success adding shop!");
                $("#successForm").text("Shop Added");
                window.location.href="Homepage.html";
            },
            error: function() {
                $("#error").text("Ajax error to add a new shop");
                console.log("add-shop.js : Error to add new shop");
            }
        });
    };

    $("#btnAddShop").click(function(event) {
        addShop(event);
    });

});
