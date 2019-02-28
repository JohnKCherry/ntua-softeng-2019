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
$(window).bind("pageshow", function() {
    var $input = $('#refresh');

    $input.val() == 'yes' ? location.reload(true) : $input.val('yes');
    // update hidden input field
});
$(document).ready(function(){
    console.log("ready");

    var shopID = getUrlParameter('id');
    if (shopID == null) shopID = 4;
    var map = null;
    var shopArray = new Array();


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
            $("#shopAddress").append("<span class=\"h6\">"+obj.address+"</span>");
            $("#shopType").append("<span class=\"h6\">"+obj.category+"</span>");
            $("#shopWebsite").append("<span class=\"h6\">"+obj.website+"</span>");
            $("#shopTel").append("<span class=\"h6\">"+obj.phone+"</span>");
            var tags = obj.tags;
            $.each(tags, function(key,value){
                $("#tags").append("<li class=\"list-inline-item\"><span class=\"h6\">"+value+"</span></li>");
            });


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
	
	$("#buttonWebsite").click(function(){
		$("#shopWebsite").replaceWith($('<textarea id=\"shopWebsite\" class=\'edit\' rows=\"1\" cols=\"30\">'+
		$("#shopWebsite").text() +
		'</textarea>'));
	});
	$("#buttonTel").click(function(){
		$("#shopTel").replaceWith($('<textarea id=\"shopTel\" class=\'edit\' rows=\"1\" cols=\"30\">'+
		$("#shopTel").text() +
		'</textarea>'));
	});
	
	$("#buttonLocation").click(function(){
		$("#shopLocation").replaceWith($('<textarea id=\"shopLocation\" class=\'edit\' rows=\"1\" cols=\"30\">'+
		$("#shopLocation").text() +
		'</textarea>'));
	});
	
	$("#buttonName").click(function(){
		$("#shopName").replaceWith($('<textarea id=\"shopName\" class=\'edit\' rows=\"1\" cols=\"30\">'+
		$("#shopName").text() +
		'</textarea>'));
	});
	
	
	$("#buttonType").click(function(){
		$("#shopType").replaceWith($('<textarea id=\"shopType\" class=\'edit\' rows=\"1\" cols=\"30\">'+
		$("#shopType").text() +
		'</textarea>'));
	});
	
	$("#buttonTags").click(function(){
		$("#shopWebsite").replaceWith($('<textarea id=\"shopTags\" class=\'edit\' rows=\"1\" cols=\"30\">'+
		$("#shopTags").text() +
		'</textarea>'));
	});
	

    setMap(shopID);





});







