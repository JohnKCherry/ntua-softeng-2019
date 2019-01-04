$(document).ready(function(){

    console.log("ready");

    var input = 1;
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "http://localhost:8765/app/observatory/api/products/"+input,
        success: function(data){
            console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            $("#productName").text(obj.name);
            $("#productDescription").append("<span class=\"h6\">"+obj.description+"</span>");
            $("#productCategory").append(obj.category);
            var tags = obj.tags;
            $.each(tags, function(key,value){
                $("#tags").append("<li class=\"list-inline-item\">"+value+"</li>");
            });
        },
        error: function(){
            console.log("Product.js : Error product with id " + input + " not found !");
        }
    });

});
