$(document).ready(function(){
    console.log("ready");

    // Set default order = 1 asceding 
    var order = 1;
    $("#order").val("1");
    var productID = 12;

    // get product general info
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

            if(obj.image != null) {
                // Convert binaryData to image
                let binary = new Uint8Array(obj.image.binaryData);
                let blob = new Blob([binary]);
                let img = new Image();

                $("#productImage").attr("src",URL.createObjectURL(blob));
            }
        },
        error: function(){
            console.log("Product.js : Error product with id " + productID + " not found !");
        }
    });

    // get prices and shops
    function shopsUpdate(){
        var geoDist = "";
        var geoLng = "";
        var geoLat = "";
        var dateFrom = "";
        var dateTo = "";
        var shops = "";
        var products = productID;
        var tags = "";
        var sort = "price";
        var orderStr = (order==1) ? "ASC" : "DESC";
        var url = "http://localhost:8765/app/observatory/api/prices?geo.dist="+geoDist
        +"&geo.lng="+geoLng
        +"&geo.lat="+geoLat
        +"&dateFrom="+dateFrom
        +"&dateTo="+dateTo
        +"&shops="+shops
        +"&products="+products
        +"&tags="+tags
        +"&sort="+sort
        +"|"+orderStr;
       console.log(url); 
        $.ajax({
            type: "GET",
            dataType: "json",
            url: url,
            success: function(data){
                console.log(data);
            },
            error: function(){
                console.log("Product.js :Prices GET Error product with id " + products + " not found !");
            }
        });

    }

    // Get prices
    shopsUpdate();

    // order change reload shops
    $("#order").change(function() {
        order = $("#order").val();
        console.log("Allakse h order se "+order);
        var orderStr = (order==1) ? "ASC" : "DESC";
        console.log(orderStr);
        shopsUpdate();
    });
});
