var token = window.sessionStorage.getItem("token");
$(document).ready(function(){
    $("#successForm").empty();
    $("#form")[0].reset();
    console.log("ready");

    console.log("Token ");
    console.log(token);
    if (token != null) {
        $("#loginBtn").text(window.sessionStorage.getItem("username"));
        $("#loginBtn").attr("href","");
        $("#editButton").css("visibility","visible");
        $("#errorForm").empty();
    }
    else {
        console.log("Not connected");
        $("#loginBtn").trigger('click');
        $("#submitButton").prop('disabled',true);
        $("#errorForm").text("You need to login to add new price"); 
    }

    var productID;
    var productName;
    var shopID;
    var shopName;
    var endpoint = "";
    var price;


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


        var url = "https://localhost:8765/observatory/api/"+endpoint+"/"+query+"?start=&count=&sort=name|ASC&status=ALL";

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
        $("#successForm").empty();
        console.log("Addprice.js: Pliktrologw product");
        query = $("#productBar").val();
        var ret = query.split(' ').join('+');
        console.log(ret);
        if (query != "" ) getData(ret,0);
        else $("#productMenu").css('display','none');

    });


    $("#productMenu").on('click', '.dropdown-item',function() {
        console.log("Clickara to span product");
        $("#productMenu").css('display','none');
        productID = $(this).attr('id');
        productName = $(this).text();
        console.log(productName + " " + productID);
        $("#productBar").val(productName);
    });

    //listener search bar shop send request
    $("#shopBar").on("keyup", function() {
        $("#successForm").empty();
        console.log("Addprice.js: Pliktrologw shop");
        query = $("#shopBar").val();
        var ret = query.split(' ').join('+');
        console.log(ret);
        if (query != "" ) getData(ret,1);
        else $("#shopMenu").css('display','none');

    });


    $("#shopMenu").on('click', '.dropdown-item',function() {
        console.log("Clickara to span shop");
        $("#shopMenu").css('display','none');
        shopID = $(this).attr('id');
        shopName = $(this).text();
        console.log(shopName + " " + shopID);
        $("#shopBar").val(shopName);
    });

    // submit form
    $("#form").submit(function() {
        console.log("Form submit");

        price = $("#price").val();
        dateFrom = $("#dateFrom").val();
        dateTo = $("#dateTo").val();

       
        console.log("productID = " + productID);
        console.log("productName = " + productName);
        console.log("shopID = " + shopID);
        console.log("shopName = " + shopName);
        console.log("price = " + price);
        console.log("dateFrom = " + dateFrom);
        console.log("dateTo = " + dateTo);

        if (productName == "" || shopName == "" || price == "" || dateFrom =="" || dateTo == "" || $("#productBar").val()=="" || $("#shopBar").val()==""){
            $("#errorForm").text("Please fill all fields"); 
        }
        else { 
            $("#errorForm").empty(); 

            // compare dates
            //
            if (dateFrom > dateTo) {
                $("#errorForm").text("Date From must be earlier than Date To");
                return false;
            }
            else {
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    headers: {'X-OBSERVATORY-AUTH' : token},
                    url: "https://localhost:8765/observatory/api/prices",
                    data:{"price":price,"dateFrom":dateFrom,"dateTo":dateTo,"productId":productID,"shopId":shopID},
                    success: function(data){
                        console.log("Success add price");
                        var obj = JSON.parse(JSON.stringify(data));
                        console.log(obj);
                        $("#successForm").text("Success add price for " + productName);
                        $("#form")[0].reset();
                    },
                    error: function(err){
                        console.log("addprice.js: Error add price");
                        $("#errorForm").text("Error add price"); 
                        console.log(err);
                    }
                });
            }

        }

        return false;   //prevent default
    });
});
