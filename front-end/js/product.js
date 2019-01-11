$(document).ready(function(){
    console.log("ready");


    // Set default order = 1 asceding 
    var order = 1;
    $("#order").val("1");
    var productID = getUrlParameter('id');
    var lowestPrice;
    var map = null;
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
            $("#productCategory").append("<span class=\"h6\">"+obj.category+"</span>");
            var tags = obj.tags;
            $.each(tags, function(key,value){
                $("#tags").append("<li class=\"list-inline-item\"><span class=\"h6\">"+value+"</span></li>");
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
            window.location.replace("404.html");
        }
    });

    // get prices and shops
    function shopsUpdate(){
        $("#shops").empty();        // Clear previous list
        $("#lowestPrice").empty();  // Clear previous lowest price
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
        var shopsID = new Array();
        $.ajax({
            type: "GET",
            dataType: "json",
            url: url,
            success: function(data){
                console.log(data);
                var obj = JSON.parse(JSON.stringify(data));
                var shops = obj.prices;
                console.log("PLithos katastimatwn " + shops.length);
                console.log("Megethos pinaka shopsID " + shopsID.length);
                // Update lowest Price
                lowestPrice = (order==1 ? shops[0].price : shops[obj.total-1].price);
                $("#lowestPrice").append(lowestPrice + " &euro;");
                var i = 0;
                $.each(shops, function(key,value){
                    console.log(key);
                    var shop_id = value.shop_id;
                    console.log(++i);
                    shopsID.push(shop_id);
                    console.log("Megethos shopsid " + shopsID.length);
                    var price = value.price;
                    var shopName = value.shop_name;
                    console.log("Price: "+price + " Name: " + shopName);
                    $("#shops").append("<li class=\"list-group-item\"><a href=\"http://localhost:8765/app/observatory/api/shops/"+shop_id+"\"><div><span id=\"shopName\">"
                                       +shopName+"</span></a><span id=\"price\">"+price+" &euro; </span></div></li>");
                });
                setMap(shopsID);
            },
            error: function(){
                console.log("Product.js :Prices GET Error product with id " + products + " not found !");
            }
        });

    }

    // Get prices
    shopsUpdate();

    // get shop by id
    // input shop id
    // returns obj [name,x,y]
    var shopsArray = new Array();
    function getShopByID(id) {

        $.ajax({
            type: "GET",
            async: false,
            dataType: "json",
            url: "http://localhost:8765/app/observatory/api/shops/"+id,
            success: function(data){
                var obj = JSON.parse(JSON.stringify(data));
                shopsArray = [obj.name,obj.lat,obj.lng];
            },
            error: function(){
                console.log("Product.js :Shop with id " + id + " not found !");
            }
        });

    }

    // input: list of shops ids
    // return: list of locations
    function shopsLocation(shopsID) {
        var locations = new Array();
        for(let i=0; i < shopsID.length; i++){
            locations[i] = new Array();
            getShopByID(shopsID[i]);
            locations[i][0] = shopsArray[0];
            locations[i][1] = shopsArray[1];
            locations[i][2] = shopsArray[2];
        }
        return locations;
    }

    // Create Map
    // input: list of shops ids
    // return: null 
    function setMap(shopsID) {
        var locations = [];
        locations = shopsLocation(shopsID);
        // Athens View
        if ( map != null ) {
            map.off();
            map.remove();
        }
        map = L.map('map').setView([37.592724,23.441932], 8);
        mapLink = 
            '<a href="http://openstreetmap.org">OpenStreetMap</a>';
        L.tileLayer(
            'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; ' + mapLink + ' Contributors',
            maxZoom: 18,
        }).addTo(map);

        for (var i = 0; i < locations.length; i++) {
            marker = new L.marker([locations[i][1],locations[i][2]])
            .bindPopup(locations[i][0])
            .addTo(map);
        }
    }
    // order change reload shops
    $("#order").change(function() {
        order = $("#order").val();
        shopsUpdate();
    });
});
var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
};
