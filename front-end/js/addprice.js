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
        console.log("Not connected");
        // $("#loginBtn").show();
        $("#loginBtn").trigger('click');
        // $("#loginBtn").show();
    }

    var productID;
    var productName;
    
    var shopID;
    var shopName;
    var endpoint = "";


    $("#productBar").val("");
    $("#shopBar").val("");
   

    // 0 = products
    // 1 = shops
    
    function getData(query,type) {
        // get product general info

        if (type == 0) {
            $("#productMenu").empty();
            endpoint = "productsbyname";
        }
        else {
            $("#shopMenu").empty();
            endpoint = "shopsbyname";
        }


        var url = "https://localhost:8765/observatory/api/"+endpoint+"/"+query+"?start=0&count=5&sort=id|ASC&status=ALL";

        console.log(url);
        $.ajax({
            type: "GET",
            dataType: "json",
            url: url,
            success: function(data){
                var obj = JSON.parse(JSON.stringify(data));
                console.log(obj);
                var list;
                var selector;
                if (type == 0) list = obj.products;
                else list = obj.products;   //must change to shops

                if (list.length != 0 && type==0) {
                    $("#productMenu").css('display','block');
                    selector = "#productMenu";
                }
                if (list.length != 0 && type==1) {
                    $("#shopMenu").css('display','block');
                    selector = "#shopMenu";
                }
                $.each(list, function(key,value){
                    $(selector).append("<span id=\""+value.id+"\"class=\"dropdown-item\">"+value.name+"</span>");
                }
                      )
            },
            error: function(){
                console.log("Addprice.js : Error get data type"+type);
                $("body").load("404.html");
                return false;
            }
        });
    }
   
    var query;
    //listener search bar product send request
    $("#productBar").on("keyup", function() {
        console.log("Addprice.js: Pliktrologw product");
        query = $("#productBar").val();
        console.log(query);
        if (query != "" ) getData(query,0);
        else $("#productMenu").css('display','none');
           
    });

    
    $("#productMenu").on('click', '.dropdown-item',function() {
        console.log("Clickara to span product");
        $("#productMenu").css('display','none');
        productID = $(this).attr('id');
        productName = $(this).text();
        console.log(productName);
        $("#productBar").val(productName);
    });
    
    //listener search bar shop send request
    $("#shopBar").on("keyup", function() {
        console.log("Addprice.js: Pliktrologw shop");
        query = $("#shopBar").val();
        console.log(query);
        if (query != "" ) getData(query,1);
        else $("#shopMenu").css('display','none');
           
    });

    
    $("#shopMenu").on('click', '.dropdown-item',function() {
        console.log("Clickara to span shop");
        $("#shopMenu").css('display','none');
        shopID = $(this).attr('id');
        shopName = $(this).text();
        console.log(shopName);
        $("#shopBar").val(shopName);
    });
});
