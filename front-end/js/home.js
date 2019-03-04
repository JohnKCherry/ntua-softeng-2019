$(document).ready(function() {

    function formSubmit(){
        var sel = $("#selector option:selected").text();
        var query = $("#homeBar").val();
        console.log(query);
        if ( sel == "Product" ){
            console.log("Product " + query);
            window.location.href = "products.html?query="+query;
        }
        else if ( sel == "Shop") {
            console.log("Shop " + query);
            window.location.href = "shops.html?query="+query;
        }
        else if ( sel == "Price") {
            console.log("Price " + query);
            window.location.href = " Homepage.html"; //to change
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

});
