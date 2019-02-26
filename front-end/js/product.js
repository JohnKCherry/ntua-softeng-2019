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
    if (token != null) {
        $("#loginBtn").text(window.sessionStorage.getItem("username"));
        $("#loginBtn").attr("href","");
        $("#editButton").css("visibility","visible");
    }
    else {
        $("#loginBtn").show();
    }
    var productID = getUrlParameter('id');
    if (productID == null) productID = 27;
    var lowestPrice;
    var map = null;

    var name;
    var description;
    var category;
    var tags;
    // get product general info
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "https://localhost:8765/observatory/api/productswithimage/"+productID,
        success: function(data){
            console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            name = obj.name;
            description = obj.description;
            category = obj.category;
            $("#productName").text(obj.name);
            document.title = obj.name;
            $("#productDescription").append("<span class=\"text-sm-left\">"+obj.description+"</span>");
            $("#productCategory").append("<span class=\"text-sm-left\">"+obj.category+"</span>");
            tags = obj.tags;
            $.each(tags, function(key,value){
                $("#tags").append("<li class=\"list-inline-item\"><span class=\"text-sm-left\">"+value+"</span> <span id=\""+key+"\" class=\"close\" style=\"visibility: hidden\">&times;</span></li>");
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
        $("#errorFilters").empty();
        var sort = $("#sort").val();
        var geoDist  = "";
        //  var geoDist = $("#distance").val();
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
        if(sortStr == "dist") findLocation();
        var orderStr = (order==1) ? "ASC" : "DESC";
        if(sortStr=="dist" && gps!=null && gps[0]!="" && gps[1]!="") {
            geoLng = gps[0];
            geoLat = gps[1];
            geoDist = $("#distance").val();
            console.log("shopsUpdate: allow location einai ta eksis lng = " +geoLng + " lat = " + geoLat + " dist = " +geoDist);
        }
        else {
            geoDist = "";
            geoLat = "";
            geoLng = "";
        }

        // compare dates
        //
        if (dateFrom > dateTo) {
            $("#errorFilters").text("Date From must be earlier than Date To"); 
            shopsNotFound();
        }
        var url = "https://localhost:8765/observatory/api/prices?verbose=false&geoDist="+geoDist
        +"&geoLng="+geoLng
        +"&geoLat="+geoLat
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
                console.log("shopsUpdate: Success me obj " + obj.total);
                if(obj.total == 0) {
                    console.log("Shops not found total = 0");
                    shopsNotFound();
                }
                else {
                    // Update lowest Price
                    lowestPrice = (order==1 ? shops[0].price : shops[shops.length-1].price);
                    $("#lowestPrice").append(lowestPrice + " &euro;");
                    $.each(shops, function(key,value){
                        console.log("Each " + value.shop_id);
                        var shop_id = value.shop_id;
                        shopsID.push(shop_id);
                        var price = value.price;
                        var shopName = value.shop_name;
                        $("#shops").append("<li class=\"list-group-item\"><a href=\"https://localhost:8765/observatory/api/shops/"+shop_id+"\"><div><span id=\"shopName\">"
                                           +shopName+"</span></a><span id=\"price\">"+price+" &euro; </span></div></li>");
                    });
                    if (reload == 1) setMap(shopsID);
                }
            },
            error: function(){
                console.log("Product.js :Prices GET Error product with id " + productID + " not found !");
                // $("#shopsDiv").text("Shops Not Found");
                $("#map").text("Error Map");
                //$("#shopsDiv").load("404.html");
                //$("html").load("404.html");
                return ;
            }
        });

    }


    function shopsNotFound() {
        console.log("Product.js :Prices GET Error product with id " + productID+ " not found !");
        //    $("#shopsDiv").text("Shops Not Found");
        $("#shops").append("<div class=\"h3\"> Shops Not Found. Use other Filters");
        $("#map").text("Error Map");
        //$("#shopsDiv").load("404.html");
        //$("html").load("404.html");
        return ;

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
            url: "https://localhost:8765/observatory/api/shops/"+id,
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
            '<a href="https://openstreetmap.org">OpenStreetMap</a>';
        L.tileLayer(
            'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
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

    $("#distance").on("change", function() {
        findLocation();
    });
    // event listener submit form
    $("#filters").submit(function() {
        console.log("Form submitted");
        // update shops and reload map
        //
        patch();
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


    $("#editButton").click(function(){
        $("#newTag").val("");

        $("#editButton").css("visibility","hidden");
        $("#applyButton").css("visibility","visible");
        $("#cancelButton").css("visibility","visible");
        $(".close").css("visibility","visible");
        $("#addButton").css("visibility", "visible");
        $("#newTag").css("visibility","visible");


        $("#productName").replaceWith("<div><u class=\"h4\">Name</u>: <input id=\"productName\" class=\"h4\" type=\"text\" value= \""+name+"\"></div> ");
        $("#productDescription").replaceWith("<div><u class=\"h4\">Description</u>: <textarea id=\"productDescription\" class=\"text-sm-left\" rows=\"1\">"+description+"</textarea></div>");
        $("#productCategory").replaceWith("<div><u class=\"h4\">Category</u>: <input id=\"productCategory\" class=\"h4\" type=\"text\" value= \""+category+"\"></div> ");
    });


    // Hide error tag if exists
    $("#newTag").click(function() {
        $("#errorTag").hide();
    });
    // Add to tags array
    $("#addButton").click(function() {
        var tmp = $("#newTag").val();
        if(tmp!="") {
            $("#newTag").val("");
            var ret = tmp.split(" ");
            var c = tags.length;
            $.each(ret,function(index,value) {
                if(jQuery.inArray(value, tags) !== -1) {
                    $("#errorTag").text("Tag " + value + "already exists");
                    $("#errorTag").show();
                    $("#newTag").val("");
                    return false;
                }
                tags.push(value);
                $("#tags").append("<li class=\"list-inline-item\"><span class=\"text-sm-left\">"+value+"</span> <span id=\""+(c+index)+"\" class=\"close\" style=\"visibility: hidden\">&times;</span></li>");
                $(".close").css("visibility","visible");
            }
                  );
            console.log(tags);
        }
    });




    // Delete from tags array
    $("#tags").on('click','.close',function() {
        var index = $(this).attr('id');
        console.log(index);
        tags.splice(index,1);
        $(this).parent().closest('li').remove();
        console.log(tags);
    });
    
    
    $('#applyButton').click(function() {
        var obj = new Object();
        obj.name = $("#productName").val();
        obj.description = $("#productDescription").val();
        obj.category = $("#productCategory").val();
        obj.tags = tags.join(", ");

        console.log(obj);
        sendUpdate(obj);
    });

    // Cancel reload page 
    $('#cancelButton').click(function() {
        location.reload();
    });

    var findLocation = function() {
        if(gps!=null && gps[0]!="" && gps[1]!="") return ;
        if ("geolocation" in navigator){  
            navigator.geolocation.getCurrentPosition(function(position){
                gps[0] = position.coords.latitude;
                gps[1] = position.coords.longitude;
                // shopsUpdate(1);
            }, function() {
                console.log("Don't allow location");
            });
        }else{
            console.log("Browser doesn't support geolocation!");
        }

    };
    
    
    function sendUpdate(obj) {
        $.ajax({
            type: "PUT",
            dataType: "json",
            headers: {'X-OBSERVATORY-AUTH' : token},
            url: "https://localhost:8765/observatory/api/products/"+productID,
            data: obj,
            success: function(data){
                console.log("Success");
                var obj = JSON.parse(JSON.stringify(data));
                console.log(obj);
                location.reload();
            },
            error: function(err){
                console.log(err);
                console.log("product.js: Error sendUpdate");
                alert("Error update");
            }
        });
    }
});

