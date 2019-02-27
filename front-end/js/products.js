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

    var id;
    var name;
    var description;
    var category;
    var tags;
    var image;
    var imgSrc;

    var start = 0;
    var count = 12;
    var sort = "id";
    var order = 1;
    var orderStr = "ASC";
    var status = "ALL";

    function getProducts(start,count,sort,order) {
        // get product general info

        orderStr = (order==1) ? "ASC" : "DESC";
        var url = "https://localhost:8765/observatory/api/productswithimage?start="+start
        +"&count="+count
        +"&sort="+sort
        +"|"+orderStr
        +"&status="+status;

        console.log(url);
        $.ajax({
            type: "GET",
            dataType: "json",
            url: url,
            success: function(data){
                var obj = JSON.parse(JSON.stringify(data));
                console.log(obj);
                var products = obj.products;

                $.each(products, function(key,value){
                    console.log(value);
                    id = value.id;
                    name = value.name;
                    description = value.description;
                    category = value.category;
                    image = value.image;
                    // create image
                    if(image != null) {
                        // Convert binaryData to image
                        let binary = new Uint8Array(image.binaryData);
                        let blob = new Blob([binary]);
                        let img = new Image();
                        imgSrc = URL.createObjectURL(blob);
                    }
                    else imgSrc="";
                    // create html
                    $(".card-deck").append("<div class=\"col-sm-6 col-md-4 col-lg-3\"><div class=\"card mb-4\"><img class=\"card-img-top img-fluid\" src=\""+imgSrc+"\" alt=\"Product Image\"><div class=\"card-body\"><a href=\"product.html?id="+id+"\" class=\"card-title\">"+name+"</a><p class=\"card-text\">"+description+"</p></div><div class=\"card-footer\"><small class=\"text-muted\">"+category+"</small></div></div></div></div>"
                    );
                });
            },
            error: function(){
                console.log("Products.js : Error get products !!");
                $("body").load("404.html");
                return false;
                // window.location.href = "404.html?error=1";
            }
        });
    }

    getProducts(start,count,sort,order);

});

