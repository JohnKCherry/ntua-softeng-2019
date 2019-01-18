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

$(document).ready(function(){
    console.log("ready");

    var token = window.sessionStorage.getItem("token");
    console.log("Token ");
    console.log(token);
    if (token != null) {
        $("#loginBtn").hide();
        $("#loginBtn").text(window.sessionStorage.getItem("username"));
    }
    else {
        $("#loginBtn").show();
    }
    var gps = new Array();
    var productID = getUrlParameter('id');
    if (productID == null) productID = 12;
    var lowestPrice;
    var map = null;

    // get product general info
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "http://localhost:8765/observatory/api/productswithimage/"+productID,
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
            $("body").load("404.html");
            return false;
            // window.location.href = "404.html?error=1";
        }
    });

    // get prices and shops
    function shopsUpdate(reload){
        $("#shops").empty();        // Clear previous list
        $("#lowestPrice").empty();  // Clear previous lowest price
        var sort = $("#sort").val();
        var geoDist = $("#distance").val();
        var geoLat = "";
        var geoLng = "";
        var dateFrom = $("#dateFrom").val();
        var dateTo = $("#dateTo").val();
        var shops = "";
        var products = productID;
        var tags = "";
        var sortStr;
        if ( sort == 1 ) sortStr = "price";
        else if ( sort == 2 ) sortStr = "date";
        else sortStr = "dist";
        var orderStr = (order==1) ? "ASC" : "DESC";
        if(gps!=null) {
            geoLng = gps[0];
            geoLat = gps[1];
        }
        var url = "http://localhost:8765/observatory/api/prices?verbose=false&geo.dist="+geoDist
        +"&geo.lng="+geoLng
        +"&geo.lat="+geoLat
        +"&dateFrom="+dateFrom
        +"&dateTo="+dateTo
        +"&shops="+shops
        +"&products="+products
        +"&tags="+tags
        +"&sort="+sortStr
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

                // Update lowest Price
                lowestPrice = (order==1 ? shops[0].price : shops[shops.length-1].price);
                $("#lowestPrice").append(lowestPrice + " &euro;");
                $.each(shops, function(key,value){
                    var shop_id = value.shop_id;
                    shopsID.push(shop_id);
                    var price = value.price;
                    var shopName = value.shop_name;
                    $("#shops").append("<li class=\"list-group-item\"><a href=\"http://localhost:8765/observatory/api/shops/"+shop_id+"\"><div><span id=\"shopName\">"
                                       +shopName+"</span></a><span id=\"price\">"+price+" &euro; </span></div></li>");
                });
                if (reload == 1) setMap(shopsID);
            },
            error: function(){
                console.log("Product.js :Prices GET Error product with id " + products + " not found !");
                $("#shopsDiv").text("Shops Not Found");
                $("#map").text("Error Map");
                //$("#shopsDiv").load("404.html");
                //$("html").load("404.html");
                return ;
            }
        });

    }


    // get shop by id
    // input shop id
    // returns obj [name,x,y]
    var shopsArray = new Array();
    function getShopByID(id) {

        $.ajax({
            type: "GET",
            async: false,
            dataType: "json",
            url: "http://localhost:8765/observatory/api/shops/"+id,
            success: function(data){
                var obj = JSON.parse(JSON.stringify(data));
                shopsArray = [obj.name,obj.lat,obj.lng];
            },
            error: function(){
                console.log("Product.js :Shop with id " + id + " not found !");
                $("#shopsDiv").text("Shops Not Found");
                $("#map").text("Error Map");
                return ;
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
        if( gps[0] == "") map = L.map('map').setView([37.592724,23.441932], 8);
        else map = L.map('map').setView([gps[0],gps[1]], 12);

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

    // event listener order
    // order change reload shops
    $("#order").change(function() {
        order = $("#order").val();
        // doesn't need to reload map so reload = 0
        shopsUpdate(0);
    });



    // event listener distance input
    $("#distance").on("change mousemove", function() {
        $("#geoDist").html($("#distance").val() + " Khm");
    });

    // event listener submit form
    $("#filters").submit(function() {
        console.log("Form submitted");
        // update shops and reload map
        shopsUpdate(1);

        return false;   //prevent default
    });


    // init


    // Set default order = 1 asceding 
    var order = 1;
    $("#order").val("1");
    $("#sort").val("1");

    // Get prices
    shopsUpdate(1);

    // set default distance
    $("#distance").val("5");
    $("#geoDist").html($("#distance").val() + " Khm");

    var findLocation = function() {
        gps[0] = "";
        gps[1] = "";
        if ("geolocation" in navigator){  
            navigator.geolocation.getCurrentPosition(function(position){
                gps[0] = position.coords.latitude;
                gps[1] = position.coords.longitude;
                shopsUpdate(1);
            }, function() {
                console.log("Don't allow location");
            });
        }else{
            console.log("Browser doesn't support geolocation!");
        }

    };
    findLocation();
});

