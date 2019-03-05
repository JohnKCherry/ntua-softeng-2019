$(document).ready(function() {

    //default value product
    $('select option[value="1"]').attr("selected",true);

    function formSubmit(){

        var sel = $("#selector option:selected").val();
        console.log(sel);
        var query = $("#homeBar").val();
        query = query.split(' ').join('+');
        console.log(query);
        if ( sel == 1 ){
            console.log("Product " + query);
            window.location.href = "products.html?query="+query;
        }
        else if ( sel == 2) {
            console.log("Shop " + query);
            window.location.href = "shops.html?query="+query;
        }
        else if ( sel == 3) {
            console.log("Product Tag " + query);
            window.location.href = " prices.html?pTag="+query; //to change
        }
        else if ( sel == 4) {
            console.log("Shop Tag " + query);
            window.location.href = " prices.html?sTag="+query; //to change
        }
        return ;
    }

    $("#homeBar").val("");
    $("#homeForm").on('submit', function(){
        console.log("home.js: Submit form");
        formSubmit();

        return false;
    });


    $("#searchBtn").click(function(){
        formSubmit();
    }); 

    var productID;
    var shopID;
    var price;
    var productName;
    var productImage;
    var shopName;
    var timestamp;

    $(document).on('click', '#newsFeedBtn',function() {
        console.log("home.js: Patithike to modal latest prices");
        $("#errorModal").empty();
        $("#modalBody").empty();

        // time for get request
        $.ajax({
            type: "GET",
            dataType: "json",
            url: "https://localhost:8765/observatory/api/feed",
            success: function(data){
                var obj = JSON.parse(JSON.stringify(data));
                console.log("home.js: Success get");
                console.log(obj);
                var feed = obj.feed;
                console.log(feed);
                if(feed.length==0) {
                    console.log("home.js: No prices no products");
                    $("#errorModal").text("Nothing to show");
                }
                else {
                    $.each(feed, function(key,value){
                        productID = value.product_id;
                        shopID = value.shop_id;
                        price = value.price;
                        timestamp = value.timestamp;
                        console.log("home.js: Tha psaksw gia to product id " + productID);
                        $.ajax({
                            type: "GET",
                            async: false,
                            dataType: "json",
                            url: "https://localhost:8765/observatory/api/productswithimage/"+productID,
                            success: function(data){
                                console.log("home.js: Elava to product ");
                                console.log(data);
                                var obj = JSON.parse(JSON.stringify(data));
                                productName = obj.name;
                                console.log(productName);

                                if(obj.image != null) {
                                    // Convert binaryData to image
                                    let binary = new Uint8Array(obj.image.binaryData);
                                    let blob = new Blob([binary]);
                                    let img = new Image();

                                    productImage = URL.createObjectURL(blob);
                                }
                                else {
                                    productImage = "imgs/product.jpg";
                                }
                                $.ajax({
                                    type: "GET",
                                    async: false,
                                    dataType: "json",
                                    url: "https://localhost:8765/observatory/api/shops/"+shopID,
                                    success: function(data){
                                        console.log("home.js: Success get shop with id " + shopID);
                                        var obj = JSON.parse(JSON.stringify(data));
                                        shopName = obj.name;
                                        console.log(shopName);
                                        $("#modalBody").append("<div class=\"card mb-3\"><div class=\"text-center\"><img class=\"card-img-top newsF\" src=\""+productImage+"\" alt=\"Product Image\"></div><div class=\"card-body\"><a href=\"product.html?id="+productID+"\"><h5 class=\"card-title\">"+productName+"</h5></a><p class=\card-text\">Στο κατάστημα <a href=\"shop.html?id="+shopID+"\">"+shopName+"</a></p><h4>"+price+" &euro;</h4><p class=\"card-text\"><small class=\"text-muted\">"+timestamp+"</small></p></div></div>");
                                    },
                                    error: function(){
                                        console.log("Product.js :Shop with id " + shopID + " not found !");
                                        return ;
                                    }
                                });
                            },
                            error: function(err){
                                console.log("home.js : Error product with id " + productID + " not found !");
                                console.log(err);
                                return false;
                            }
                        });
                    });
                }

            },
            error: function(){
                console.log("home.js: Error Feed");
                $("#errorModal").text("Nothing to show");
                return ;
            }
        });
    });

    $('#newsFeed').on('hidden.bs.modal', function () {
        console.log("home.js: Modal eklseise");
    })

    /*
       <div class="card mb-3">
       <div class="text-center">
       <img class="card-img-top newsF" src="imgs/product.jpg" alt="Card image cap">
       </div>
       <div class="card-body">
       <h5 class="card-title">Card title</h5>
       <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
       <p class="card-text"><small class="text-muted">Last updated 3 mins ago</small></p>
       </div>
       </div>
       */
});
