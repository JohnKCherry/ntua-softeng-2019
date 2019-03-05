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



    var query = getUrlParameter('query');
    var ret;
    if (query == null) $("#searchBar").val("");
    else {
        $("#searchBar").val(query);
        ret = query.split(' ').join('+');
        console.log(ret);
    }

    var id;
    var name;
    var address;
    var tags;

    var start = 0;
    var count = 20;
    var sort = "name";
    var order = 1;
    $("#order").val("1");
    var orderStr = "ASC";
    var status = 1;
    $("#status").val("1");
    var statusStr = "ALL";
    var byName = 0;


    function getShops(start,count,sort,order,status,clear,byName) {
        // get shops general info



        $("#loadMe").modal({
            backdrop: "static", //remove ability to close modal with click
            keyboard: false, //remove option to close with keyboard
            show: true //Display loader!
        });
        //set timeout to be sure that will be hide
        setTimeout(function() {
            $("#loadMe").modal("hide");
        }, 1000);
        if(clear) $(".card-deck").empty();
        orderStr = (order==1) ? "ASC" : "DESC";
        if (status == 1) statusStr = "ALL";
        else if (status == 2) statusStr = "ACTIVE";

        // if byName == 1 get request to endpoint productsbynamewithimage
        // else productswithimage
        if (byName == 1) endpoint = "shopsbyname/"+ret;
        else endpoint = "shops";

        var url = "https://localhost:8765/observatory/api/"+endpoint+"?start="+start
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
                console.log(shops);
                $.each(shops, function(key,value){
                    id = value.id;
                    name = value.name;
                    address = value.address;
                    // create html
                    $(".card-deck").append("<div class=\"col-sm-6 col-md-4 col-lg-3\"><div class=\"card mb-4\"><div class=\"text-center\"><img class=\"card-img-top img-fluid\" src=\"imgs/shop.png\" alt=\"Shop Image\"></div><div class=\"card-body\"><a href=\"shop.html?id="+id+"\" class=\"card-title\">"+name+"</a><p class=\"card-text\">"+address+"</p></div></div></div>");
                });
                $("#loadMe").modal("hide");
            },
            error: function(){
                console.log("Shops.js : Error get shops !!");
                $("#loadMe").modal("hide");
                $("body").load("404.html");
                return false;
            }
        });
    }

    if ( query == null) byName = 0;
    else byName = 1;

    getShops(start,count,sort,order,statusStr,1,byName);

    /*
    //listener search bar send request
    $("#searchBar").on("keyup", function() {
        console.log("Products.js: Pliktrologw");
        query = $("#searchBar").val();
        console.log(query);
        if (query != "") {
            byName = 1;
            ret = query.split(' ').join('+');
            console.log(ret);
        }
        else byName = 0;

        getShops(0,12,sort,order,status,1,byName);

    });
*/
    function formSubmit() {
        query = $("#searchBar").val();
        if (query != "") {
            byName = 1;
            ret = query.split(' ').join('+');
            console.log(ret);
        }
        else byName = 0;
            
        getShops(0,12,sort,order,status,1,byName);
        
    }
    
    $("#bar").submit(function() {
        console.log("prices.js: Form submit");
        formSubmit();

        return false;
    });

    $("#searchBtn").on('click', function() {
        console.log("prices.js: Search Button clicked");
        formSubmit();
    });
    // event listener order
    // order change reload products
    $("#order").change(function() {
        order = $("#order").val();
        getShops(0,12,sort,order,status,1,byName);
    });

    // status event listener
    $("#status").change(function() {
        status = $("#status").val();
        console.log("Status changed to " + status);
        getShops(0,12,sort,order,status,1);
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
        if($(window).scrollTop() + $(window).height() == getDocHeight()) {
            start = start+11;
            getShops(start,12,sort,order,status,0,byName);
        }
    });
});

