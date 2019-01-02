$(document).ready(function(){

    console.log("ready");

    $("form").submit(function(){
        $("#res").empty();
        console.log("submit");
        var input = $("#input").val();
        console.log("H metavliti eiai " + input);
        $.ajax({
            type: "GET",
            dataType: "json",
            url: "http://localhost:8765/app/observatory/api/products/"+input,
            success: function(data){
                // alert(data);
                console.log(data);
                var obj = JSON.parse(JSON.stringify(data));
                $("#res").append("<b>Product Name</b>: " + obj.name);
            },
            error: function(){
                $("#res").append("Product not found");
            }
        });
    });

});
