$(document).ready(function(){

    console.log("ready");

    var productID = 2;
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "http://localhost:8765/app/observatory/api/productswithimage/"+productID,
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
           
            // Convert binaryData to image
            let binary = new Uint8Array(obj.image.binaryData);
            let blob = new Blob([binary]);
            let img = new Image();

            $("#productImage").attr("src",URL.createObjectURL(blob));
        },
        error: function(){
            console.log("Product.js : Error product with id " + productID + " not found !");
        }
    });

});
