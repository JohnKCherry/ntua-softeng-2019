var token = window.sessionStorage.getItem("token");

$(document).ready(function(){

    console.log("Add product ready");
    console.log("Token:");
    console.log(token);

    if (token != null) {
        $("#loginBtn").text(window.sessionStorage.getItem("username"));
        $("#loginBtn").attr("href","");
        $("#errorForm").empty();
    }
    else {
        console.log("Not connected.");
        $("#loginBtn").trigger('click');
        $("#submitBtn").prop('disabled',true);
        $("#errorForm").text("You need to login to add new product");
    }

    function addProduct(event) {

        event.preventDefault()
        event.stopPropagation()
        //Fetch form to apply custom Bootstrap validation
        var form = $("#formAddProduct")

        if (form[0].checkValidity() === false) {
            event.preventDefault()
            event.stopPropagation()
        }

        console.log("Check Passed");
        form.addClass('was-validated');

        console.log($("#productname").val());
        console.log($("#description").val());
        console.log($("#category").val());
        console.log($("#tags").val());
        console.log($("#productImage").val());

        if ($("#productImage").val() == "") {
            $.ajax({
                type: "POST",
                dataType: "json",
                headers: {'X-OBSERVATORY-AUTH' : token},
                url: "https://localhost:8765/observatory/api/products",
                data:{"name":$("#productname").val(), "description":$("#description").val(), "category":$("#category").val(), "tags":$("#tags").val()},
                success: function(data){
                    console.log("Success");
                    var obj = JSON.parse(JSON.stringify(data));
                },
                error: function(){
                    $("error").text("Error to add a new product");
                    $("#formAddProduct")[0].reset();
                    console.log("add-product.js : Error to add product");
                }
            });
        }
        else {
            var data = new FormData();
            data.append("name", $("#productname").val());
            data.append("description", $("#description").val());
            data.append("category", $("#category").val());
            data.append("tags", $("#tags").val());
            data.append("fileToUpload", jQuery("#productImage")[0].files[0]);
            $.ajax({
                type: "POST",
                dataType: "json",
                headers: {'X-OBSERVATORY-AUTH' : token},
                url: "https://localhost:8765/observatory/api/productswithimage",
                data: data,
                cache: false,
                contentType: false,
                processData: false,
                success: function(data){
                    console.log("Success");
                    var obj = JSON.parse(JSON.stringify(data));
                },
                error: function(){
                    $("error").text("Error to add a new product with image");
                    $("#formAddProduct")[0].reset();
                    console.log("add-product.js : Error to add product with image");
                }
            });
        }
    };

    $("#formAddProduct").keypress(function(event) {
        if(event.keyCode==13) {
            console.log("Enter");
            addProduct(event);
        }
    });

    $("#btnAddProduct").click(function(event) {

        addProduct(event);
    });

});
