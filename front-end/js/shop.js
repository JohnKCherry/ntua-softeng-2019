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

    var shopID = getUrlParameter('id');
    if (shopID == null) shopID = 38;
    var map = null;
    var shopArray = new Array();
	var name;
	var tags;
	var Location;

    var lat;
    var lng;

    //GET info
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "https://localhost:8765/observatory/api/shops/"+shopID,
        success: function(data){
            console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            shopArray = [obj.name,obj.lat,obj.lng];           
            document.title = obj.name;
            $("#shopName").text(obj.name);
            $("#shopLocation").append("<span class=\"h6\">"+obj.address+"</span>");
            $("#shopType").append("<span class=\"h6\">"+obj.category+"</span>");
            tags = obj.tags;
            $.each(tags, function(key,value){
                $("#tags").append("<li class=\"list-inline-item\"><span class=\"text-sm-left\">" + value + "</span><span id=\"" + key + "\" class=\"close\" style = \"visibility: hidden\">&times;</span></li>");
            });
			name=obj.name;
			Location=obj.address;
			lat=obj.lat;
			lng=obj.lng
			


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
            url: "https://localhost:8765/observatory/api/shops/"+id,
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
                    $("#errorTag").text("Tag " + value + " already exists");
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
		$("#shopLocation").replaceWith($('<div class=\"h4\">Physical Location: <input type=\"text\" id=\"shopLocation\" class="h4" value="' + Location + '"></input></div>'));
		$("#shopName").replaceWith($('<div class=\"h2\">Name: <input type=\"text\" id=\"shopName\"  class="h4" value="' + name + '"></input></div>'));
		$("#shopType").replaceWith($('<div class=\"h4\">Categories: <input type=\"text\" id=\"shopType\" class="h4" value="' + type + '"></input></div>'));
		
		
		
		
	});
	
	
	
	$('#applyButton').click(function() {
		var obj = new Object();
		obj.name = $("#shopName").val();
		console.log($("#shopName").val());
        console.log(obj.name);
		obj.lat=lat;
		obj.lng=lng;
		obj.address = Location;
		obj.tags = tags.join(", ");
		console.log(obj);
		sendUpdate(obj);
	});
	
	
	$("#cancelButton").click(function(){
		location.reload();
		
	});
	
	
	function sendUpdate(obj) {
        $.ajax({
            type: "PUT",
            dataType: "json",
            headers: {'X-OBSERVATORY-AUTH' : token},
            url: "https://localhost:8765/observatory/api/shops/"+shopID,
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







