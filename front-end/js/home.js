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

});
