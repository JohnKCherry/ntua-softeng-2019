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




    var id;
    var name;
    var address;
    var tags;

    var start = 0;
    var count = 20;
    var sort = "id";
    var order = 1;
    $("#order").val("1");
    var orderStr = "ASC";
    var status = 1;
    $("#status").val("1");
    var statusStr = "ALL";


    function getShops(start,count,sort,order,status,clear) {
        // get shops general info

        if(clear) $(".card-deck").empty();
        orderStr = (order==1) ? "ASC" : "DESC";
        if (status == 1) statusStr = "ALL";
        else if (status == 2) statusStr = "ACTIVE";
        var url = "https://localhost:8765/observatory/api/shops?start="+start
        +"&count="+count
        +"&sort="+sort
        +"|"+orderStr
        +"&status="+statusStr;

        console.log(url);
        $.ajax({
            type: "GET",
            dataType: "json",
            url: url,
            success: function(data){
                var obj = JSON.parse(JSON.stringify(data));
                console.log(obj);
                var shops = obj.shops;
                $.each(shops, function(key,value){
                    id = value.id;
                    name = value.name;
                    address = value.address;
                    // create html
                    $(".card-deck").append("<div class=\"col-sm-6 col-md-4 col-lg-3\"><div class=\"card mb-4\"><img class=\"card-img-top img-fluid\" src=\"imgs/shop.png\" alt=\"Product Image\"><div class=\"card-body\"><a href=\"shop.html?id="+id+"\" class=\"card-title\">"+name+"</a><p class=\"card-text\">"+address+"</p></div></div></div>");
                });
            },
            error: function(){
                console.log("Shops.js : Error get shops !!");
                $("body").load("404.html");
                return false;
            }
        });
    }

    getShops(start,count,sort,order,statusStr,1);

    // event listener order
    // order change reload products
    $("#order").change(function() {
        order = $("#order").val();
        getShops(0,12,sort,order,status,1);
    });

    // status event listener
    $("#status").change(function() {
        status = $("#status").val();
        console.log("Status changed to " + status);
       // getShops(0,12,sort,order,status,1);
    });

    // function take height for all browsers
    function getDocHeight() {
        var D = document;
        return Math.max(
            D.body.scrollHeight, D.documentElement.scrollHeight,
            D.body.offsetHeight, D.documentElement.offsetHeight,
            D.body.clientHeight, D.documentElement.clientHeight
        );
    }

    // when scroll down load more products
    // trigger getProducts earlier
    $(window).scroll(function() {
        if($(window).scrollTop() + $(window).height() > getDocHeight() - 100) {
            start = start+11;
            getShops(start,12,sort,order,status,0);
        }
    });
});
