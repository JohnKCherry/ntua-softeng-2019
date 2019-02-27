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

    var shopID = getUrlParameter('id');
    if (shopID == null) shopID = 4;
    var map = null;
    var shopArray = new Array();
	var name;
	var website;
	var telephone;
	var tags;
	var type;
	var Location;


    //GET info
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "http://localhost:8765/observatory/api/shops/"+shopID,
        success: function(data){
            console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            shopArray = [obj.name,obj.lat,obj.lng];           
            $("#shopName").text(obj.name);
            $("#shopLocation").append("<span class=\"h6\">"+obj.address+"</span>");
            $("#shopType").append("<span class=\"h6\">"+obj.category+"</span>");
            $("#shopWebsite").append("<span class=\"h6\">"+obj.website+"</span>");
            $("#shopTel").append("<span class=\"h6\">"+obj.phone+"</span>");
            tags = obj.tags;
            $.each(tags, function(key,value){
                $("#tags").append("<li class=\"list-inline-item\"><span class=\"text-sm-left\">"+value+"</span><span id=\"' + key + '\" style = \"visibility: hidden\">&times;<\span></li>");
            });
			name=obj.name;
			website=obj.website;
			telephone=obj.phone;
			type=obj.category;
			Location=obj.address;
			


        },
        error: function(){
            console.log("shop.js : Error shop with id " + shopID + " not found !");
            $("body").load("404.html");
            return false;
            // window.location.href = "404.html?error=1";
        }
    });


    function getLocation(id) {

        $.ajax({
            type: "GET",
            async: false,
            dataType: "json",
            url: "http://localhost:8765/observatory/api/shops/"+id,
            success: function(data){
                var obj = JSON.parse(JSON.stringify(data));
                shopArray = [obj.name,obj.lat,obj.lng];
                console.log(shopArray[0]);
                console.log(shopArray[1]);
                console.log(shopArray[2]);
         //       return shopArray;

            },
            error: function(){
                console.log("shop.js :Shop with id " + id + " not found !");
                $("#map").text("Error Map");
                return ;
            }
        });

    }
	
	



    function setMap(shopID) {
        getLocation(shopID);
        // Athens View
        if ( map != null ) {
            map.off();
            map.remove();
        }
        console.log(shopArray[0]);
        console.log(shopArray[1]);
        console.log(shopArray[2]);
        map = L.map('map').setView([37.592724,23.441932], 8);

        mapLink = 
            '<a href="http://openstreetmap.org">OpenStreetMap</a>';
        L.tileLayer(
            'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; ' + mapLink + ' Contributors',
            maxZoom: 18,
        }).addTo(map);

        marker = new L.marker([shopArray[1],shopArray[2]])
        .bindPopup(shopArray[0])
        .addTo(map);
    }
	
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
	
	$("#editButton").click(function(){
		$("#newTag").val("");
		
		$("#editButton").css("visibility","hidden");
		$("#applyButton").css("visibility","visible");
		$("#cancelButton").css("visibility","visible");
		$(".close").css("visibility","visible");
		$("#addButton").css("visibility", "visible");
		$("#newTag").css("visibility","visible");
		$("#shopWebsite").replaceWith($('<p id=\"shopWebsite\" class="h3">Website: <input type=\"text\" id=\"shopWebsite2\" name=\"shopWebsite2\" class="h4" value="' + website + '"><br></input></p>'));
		$("#shopTel").replaceWith($('<p id=\"shopTel\" class="h4">Telephone: <input type=\"text\" id=\"shopTel2\" name=\"shopTel2\" class="h4" value="' + telephone + '"><br></input></p>'));
		$("#shopLocation").replaceWith($('<p id=\"shopLocation\" class="h4">Physical Location: <input type=\"text\" id=\"shopLocation2\" name=\"shopLocation2\" class="h4" value="' + Location + '"><br></input></p>'));
		$("#shopName").replaceWith($('<p id=\"shopName\" class="h2">Name: <input type=\"text\" id=\"shopName2\" name=\"shopName2\" class="h4" value="' + name + '"><br></input></p>'));
		$("#shopType").replaceWith($('<p id=\"shopType\" class="h4">Categories: <input type=\"text\" id=\"shopType2\" name=\"shopType2\" class="h4" value="' + type + '"><br></input></p>'));
		$("#tagsHeader").replaceWith($('<p id=\"tags\" class="h4">Tags: <input type=\"text\" id=\"tags2\" name=\"tags2\" class="h4" value="' + tags + '"><br></input></p>'));
		
		
		
	});
	$("#cancelButton").click(function(){
		$("#shopWebsite").replaceWith($('<p id="shopWebsite" class="h3">Website: <span class=\"h6\">' + website + '</span></p>'));
		$("#shopTel").replaceWith($('<p id="shopTel" class="h4">Telephone: <span class=\"h6\">' + telephone + '</span></p>'));
		$("#shopLocation").replaceWith($('<p id="shopLocation" class="h4">Physical Location: <span class=\"h6\">' + Location + '</span></p>'));
		$("#shopName").replaceWith($('<p id="shopName" class="h2">' + name + '</p>'));
		$("#shopType").replaceWith($('<p id="shopType" class="h4">Categories: <span class=\"h6\">' + type + '</span></p>'));
		$("#tags").replaceWith($('<span id="tagsHeader" class="h5" >Tags: <ul id="tags" class="list-inline">'+ tags + '</ul></span>'));
		$("#editButton").css("visibility","visible");
		$("#applyButton").css("visibility","hidden");
		$("#cancelButton").css("visibility","hidden");
		
	});
	
	$("applyButton").click(function(){
		var obj = new Object();
		obj.name = $("shopName2").val();
		obj.tel = $("shopTel2").val();
		obj.website = $("shopWebsite2").val();
		obj.address = $("shopLocation2").val();
		obj.type = $("shopType2").val();
		obj.tags = tags.join(", ");
		console.log(obj);
		sendUpdate(obj);
	});
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
                location.reload();
            }
        });
    }
		
	
	

    setMap(shopID);





});







