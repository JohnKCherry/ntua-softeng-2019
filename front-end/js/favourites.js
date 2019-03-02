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
        console.log("Not connected");
        // $("#loginBtn").show();
//        $("#loginBtn").trigger('click');
        $("#loginModal").modal('show');
    }

    var id;
    var name;
    var description;
    var category;
    var tags;
    var image;
    var imgSrc;


    function getFavourites() {
        // get product general info

        $(".card-deck").empty();

        if (token == null) return false;
        $.ajax({
            type: "GET",
            dataType: "json",
            headers: {'X-OBSERVATORY-AUTH' : token},
            url: "https://localhost:8765/observatory/api/favourites",
            success: function(data){
                var obj = JSON.parse(JSON.stringify(data));
                console.log(obj);
                var products = obj.products;

                if (products.length == 0) {
                    console.log("Not have favourites");
                    $("#heading").text("Empty favourites");
                    return false;
                }
                $("#heading").text("Your feavourites");
                $.each(products, function(key,value){
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
                    else imgSrc="imgs/product.jpg";
                    // create html
                    $(".card-deck").append("<div class=\"col-sm-6 col-md-4 col-lg-3\"><div class=\"card mb-4\"><img class=\"card-img-top img-fluid\" src=\""+imgSrc+"\" alt=\"Product Image\"><div class=\"card-body\"><a href=\"product.html?id="+id+"\" class=\"card-title\">"+name+"</a><br /><a class=\"text-secondary collapsed card-link\" data-toggle=\"collapse\" href=\"#collapse"+id+"\">Read Description</a><div id=\"collapse"+id+"\" class=\"collapse\"><p class=\"card-text\">"+description+"</p></div></div><div class=\"card-footer\"><small class=\"text-muted\">"+category+"</small></div></div></div></div>"
                                          );
                });
            },
            error: function(){
                console.log("Products.js : Error get favourites !!");
                $("#heading").text("Error get favourites");
                $("body").load("404.html");
                return false;
            }
        });
    }

    getFavourites();
});

