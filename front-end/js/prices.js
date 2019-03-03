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
var gps = new Array();
gps[0] = "";
gps[1] = "";
var token = window.sessionStorage.getItem("token");
$(document).ready(function(){
    console.log("ready");

    console.log("Token ");
    console.log(token);

    var productTags="";
    var shopTags="";

    var query1 = getUrlParameter('query1');
    if (query1 == null) $("#productBar").val("");
    else {
        $("#productBar").val(query1);
        productTags = query.split(' ').join('+');
        console.log(productTags);
    }
    
    var query2 = getUrlParameter('query2');
    if (query2 == null) $("#shopBar").val("");
    else {
        $("#shopBar").val(query2);
        shopTags = query.split(' ').join('+');
        console.log(shopTags);
    }
    
    var map = null;
    var productName;
    var productID;
    var productImage;
    var shopName;
    var shopID;


    // Set default order = 1 asceding 
    var order = 1;
    var sort = 1;
    $("#order").val("1");
    $("#sort").val("1");

    // set default distance
    $("#distance").val("5");
    $("#geoDist").html($("#distance").val() + " Khm");


    var start = 0;
    var count = "";
    
    // get prices and shops
    function getPrices(reload){

        if(reload) $(".card-deck").empty();
        $("#errorFilters").empty();
        $("#errorDeck").empty();

        if ($("#productBar").val() == "") productTags = "";
        if ($("#shopBar").val() == "") shopTags = "";
       
        sort = $("#sort").val();
        order = $("#order").val();
        var geoDist  = "";
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

        // an epelekses apostasi findlocation
        if(sortStr == "dist") {
            findLocation();
        }
        console.log("Order " + order);
        var orderStr = (order==1) ? "ASC" : "DESC";
        if(sortStr=="dist" && gps!=null && gps[0]!="" && gps[1]!="") {
            geoLng = gps[0];
            geoLat = gps[1];
            geoDist = $("#distance").val();
            console.log("shopsUpdate: allow location einai ta eksis lng = " +geoLng + " lat = " + geoLat + " dist = " +geoDist);
        }
        else if (sortStr=="dist" && gps==null) {
            findLocation();
        }
        else {
            geoDist = "";
            geoLat = "";
            geoLng = "";
        }

        // an dialekseis sort me hmerominia prepei na exeis dialeksei kai ta 2
        if (sort == 2 && (dateFrom=="" || dateTo=="")) {
            $("#errorFilters").text("Please select date from/to"); 
        }
        // compare dates
        //
        if (dateFrom > dateTo) {
            $("#errorFilters").text("Date From must be earlier than Date To"); 
            shopsNotFound();
        }
        
        var url = "https://localhost:8765/observatory/api/prices?verbose=false&start="+start
        +"&count="+count
        +"&geoDist="+geoDist
        +"&geoLng="+geoLng
        +"&geoLat="+geoLat
        +"&dateFrom="+dateFrom
        +"&dateTo="+dateTo
        +"&shops="+shops
        +"&products="
        +"&tags="
        +"&productTags="+productTags
        +"&shopTags="+shopTags
        +"&sort="+sortStr
        +"|"+orderStr;
        console.log(url); 
       // var shopsID = new Array();
        $.ajax({
            type: "GET",
            dataType: "json",
            url: url,
            success: function(data){
                console.log(data);
                var obj = JSON.parse(JSON.stringify(data));
                var shops = obj.prices;
                console.log("shopsUpdate: Success me obj " + obj.total);
                if(obj.total == 0) {
                    console.log("Shops not found total = 0");
                    $("#errorDeck").text("Nothing to show");
                }
                else {
                    $.each(shops, function(key,value){
                        productID = value.product_id;
                        productName = value.product_name;
                        productPrice = value.price;
                        shopName = value.shop_name;
                        shopID = value.shop_id;

                        // for show price id
                        var hash = shopName.split(' ').join('');
                        hash += productID;
                        getProduct(productID);        
                        $(".card-deck").append("<div class=\"col-sm-6 col-md-4 col-lg-3\"><div class=\"card mb-4\"><img class=\"card-img-top img-fluid\" src=\""+productImage+"\" alt=\"Product Image\"><div class=\"card-body\"><a href=\"product.html?id="+productID+"\" class=\"card-title\">"+productName+"</a><br /><a class=\"text-secondary collapsed card-link\" data-toggle=\"collapse\" href=\"#"+hash+"\">Price</a><div id=\""+hash+"\" class=\"\" aria-expanded=\"true\"><p class=\"card-text\">"+productPrice+"&euro;</p></div></div><div class=\"card-footer\"><a href=\"shop.html?=id="+shopID+"\"<small class=\"text-muted\">"+shopName+"</small></a></div></div></div></div>"
                                          );
                    });
                   // if (reload == 1) setMap(shopsID);
                }
            },
            error: function(){
                console.log("prices.js :Prices GET Error!");
                return ;
            }
        });

    }

    function getProduct(currID) {
        
        console.log("prices.js: tha kanw get product gia to " + currID);
    // get product general info
    $.ajax({
        type: "GET",
        async: false,
        dataType: "json",
        url: "https://localhost:8765/observatory/api/productswithimage/"+currID,
        success: function(data){
            console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            if(obj.image != null) {
                let binary = new Uint8Array(obj.image.binaryData);
                let blob = new Blob([binary]);
                let img = new Image();
                productImage = URL.createObjectURL(blob);
            }
            else {
                productImage = "imgs/product.jpg";
            }
        },
        error: function(){
            console.log("Prices.js : Error get product with id " + currID + " not found !");
            return false;
        }
    });

    }
    //listener search bar send request
    $("#productBar").on("keyup", function() {
        console.log("Prices.js: Pliktrologw productTag");
        var tmp = $("#productBar").val();
        if (tmp != "") {
            productTags = tmp.split(' ').join('+');
            console.log(productTags);
        }
        getPrices(1);
           
    });
    //listener search bar send request
    $("#shopBar").on("keyup", function() {
        console.log("Prices.js: Pliktrologw shopTag");
        var tmp = $("#shopBar").val();
        if (tmp != "") {
            shopTags = tmp.split(' ').join('+');
            console.log(shopTags);
        }
        getPrices(1);
           
    });
    // event listener order
    // order change reload products
    $("#order").change(function() {
        order = $("#order").val();
        getPrices(1);
    });


    // event listener distance input
    $("#distance").on("change mousemove", function() {
        $("#geoDist").html($("#distance").val() + " Khm");
    });

    $("#distance").on("change", function() {
        findLocation();
    });
    // event listener submit form
    $("#filters").submit(function() {
        console.log("Form submitted");
        // update shops and reload map
        
        getPrices(1);

        return false;   //prevent default
    });



    // run...

    getPrices(1);
    
    // get shop by id
    // input shop id
    // returns obj [name,x,y]
    var shopsArray = new Array();
    function getShopByID(id) {

        $.ajax({
            type: "GET",
            async: false,
            dataType: "json",
            url: "https://localhost:8765/observatory/api/shops/"+id,
            success: function(data){
                var obj = JSON.parse(JSON.stringify(data));
                shopsArray = [obj.name,obj.lat,obj.lng];
            },
            error: function(){
                console.log("Product.js :Shop with id " + id + " not found !");
                $("#shopsDiv").text("Shops Not Found");
                //        $("#map").text("Error Map");
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
        markersLayer = new L.LayerGroup(); // NOTE: Layer is created here!
        console.log("Creating map...");
        var locations = [];
        locations = shopsLocation(shopsID);
        console.log("Get locations..." + locations);
        // Athens View
        if ( map != null) {
            map.off();
            map.remove();
        }
        //        if( gps[0] == "") map = L.map('map').setView([37.592724,23.441932], 8);
        //      else map = L.map('map').setView([gps[0],gps[1]], 12);


        // to pio panw magazi
        map = L.map('map').setView([locations[0][1],locations[0][2]], 12);



        mapLink = 
            '<a href="https://openstreetmap.org">OpenStreetMap</a>';
        L.tileLayer(
            'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; ' + mapLink + ' Contributors',
            maxZoom: 18,
        }).addTo(map);

        console.log("loop");
        for (var i = 0; i < locations.length; i++) {
            marker = new L.marker([locations[i][1],locations[i][2]])
            .bindPopup(locations[i][0]);
            // .addTo(map);
            markersLayer.addLayer(marker); 
        }

        markersLayer.addTo(map);
    }




    var findLocation = function() {
        if(gps!=null && gps[0]!="" && gps[1]!="") return ;
        if ("geolocation" in navigator){  
            navigator.geolocation.getCurrentPosition(function(position){
                gps[0] = position.coords.latitude;
                gps[1] = position.coords.longitude;
              //  getPrices(1);
            }, function() {
                console.log("Don't allow location");
                $("#errorFilters").text("Allow location please"); 
            });
        }else{
            console.log("Browser doesn't support geolocation!");
        }

    };

});

