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

    var pTag = getUrlParameter('pTag');
    if (pTag == null) $("#productBar").val("");
    else {
        $("#productBar").val(pTag);
        productTags = pTag.split(' ').join('+');
        console.log(productTags);
    }

    var sTag = getUrlParameter('sTag');
    if (sTag == null) $("#shopBar").val("");
    else {
        $("#shopBar").val(sTag);
        shopTags = sTag.split(' ').join('+');
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
    function getPrices(reload,loading){

        if (loading) {
            $("#loadMe").modal({
                backdrop: "static", //remove ability to close modal with click
                keyboard: false, //remove option to close with keyboard
                show: true //Display loader!
            });
            //set timeout to be sure that will be hide
            setTimeout(function() {
                $("#loadMe").modal("hide");
            }, 1500);
        }
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
                        $(".card-deck").append("<div class=\"col-sm-6 col-md-4 col-lg-3\"><div class=\"card mb-4\"><div class=\"text-center\"><img class=\"card-img-top img-fluid\" src=\""+productImage+"\" alt=\"Product Image\"></div><div class=\"card-body\"><a href=\"product.html?id="+productID+"\" class=\"card-title\">"+productName+"</a><br /><a class=\"text-secondary collapsed card-link\" data-toggle=\"collapse\" href=\"#"+hash+"\">Price</a><div id=\""+hash+"\" class=\"\" aria-expanded=\"true\"><p class=\"card-text\">"+productPrice+"&euro;</p></div><button id=\"modalBtn\" data-product=\""+productID+"\" data-shop=\""+shopID+"\" type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#mapModal\">Show Map</button></div><div class=\"card-footer\"><a href=\"shop.html?=id="+shopID+"\"<small class=\"text-muted\">"+shopName+"</small></a></div></div></div></div>"
                                              );
                    });
                    // if (reload == 1) setMap(shopsID);
                }
                $("#loadMe").modal("hide");

            },
            error: function(){
                console.log("prices.js :Prices GET Error!");  
                $("#loadMe").modal("hide");

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

    function formSubmit() {
        var tmp = $("#productBar").val();
        if (tmp != "") {
            productTags = tmp.split(' ').join('+');
            console.log("prices.js: productTags: " +productTags);
        }
        tmp = $("#shopBar").val();
        if (tmp != "") {
            shopTags = tmp.split(' ').join('+');
            console.log("prices.js: shopTags: " +shopTags);
        }
        console.log("prices.js: Kalw thn get products eimai form submit");
        getPrices(1,1);
    }


    $("#bars").submit(function() {
        console.log("prices.js: Form submit");
        formSubmit();

        return false;
    });

    
    // event listener order
    // order change reload products
    $("#order").change(function() {
        order = $("#order").val();
        console.log("prices.js: Kalw thn get products eimai order change");
        getPrices(1,1);
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

        console.log("prices.js: Kalw thn get products eimai filters refresh");
        getPrices(1,1);

        return false;   //prevent default
    });



    $(document).on('click', '#modalBtn',function() {
        console.log("prices.js: Patithike to modal");
        var pID = $(this).attr('data-product');
        var sID = $(this).attr('data-shop');
        console.log("Product: " + pID + " Shop: " + sID);
        setMap(sID);
    });

    $('#mapModal').on('hidden.bs.modal', function () {
        console.log("Modal eklseise");
        map.off();
        map.remove();
        map = null;
    })
    // run...

    console.log("prices.js: Kalw thn get products eimai init run");
    getPrices(1,1);

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
        getShopByID(shopsID);
        return shopsArray;
    }

    // Create Map
    // input: list of shops ids
    // return: null

    function setMap(shopsID) {
        if ( map != null) {
            map.off();
            map.remove();
        }
        markersLayer = new L.LayerGroup(); // NOTE: Layer is created here!
        console.log("Creating map...");
        var locations = [];
        locations = shopsLocation(shopsID);
        console.log("Get locations..." + locations);

        if (gps == null || gps[0] == "" || gps[1] == "") {
            findLocation(1,shopsID);
        }else {
            map = L.map('map', {
                center: [gps[0], gps[1]],
                zoom: 12
            });
            var greenIcon = new L.Icon({
                iconUrl: 'https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
                shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
                iconSize: [25, 41],
                iconAnchor: [12, 41],
                popupAnchor: [1, -34],
                shadowSize: [41, 41]
            });

            var you = new L.marker([gps[0],gps[1]],{icon: greenIcon})
            .bindPopup("You");

            markersLayer.addLayer(you); 
        }


        // to pio panw magazi
        //  map = L.map('map').setView([locations[1],locations[2]], 12);



        mapLink = 
            '<a href="https://openstreetmap.org">OpenStreetMap</a>';
        L.tileLayer(
            'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; ' + mapLink + ' Contributors',
            maxZoom: 18,
        }).addTo(map);

        marker = new L.marker([locations[1],locations[2]])
        .bindPopup(locations[0]);

        markersLayer.addLayer(marker); 

        markersLayer.addTo(map);
    }




    var findLocation = function(flag,shopsID) {
        if(gps!=null && gps[0]!="" && gps[1]!="") return ;
        if ("geolocation" in navigator){  
            navigator.geolocation.getCurrentPosition(function(position){
                gps[0] = position.coords.latitude;
                gps[1] = position.coords.longitude;
                setMap(shopsID);
            }, function() {
                console.log("Don't allow location");
                $("#errorFilters").text("Allow location please"); 
            });
        }else{
            console.log("Browser doesn't support geolocation!");
        }

    };

});

