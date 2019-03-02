$(document).ready(function() {

    $("#homeBar").val("");
    $("#searchBtn").click(function(){
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
    }); 
});
