
$(document).ready(function(){


    console.log("Signup ready");
    function signup(event) {

        event.preventDefault()
        event.stopPropagation()
        //Fetch form to apply custom Bootstrap validation
        var form = $("#formSignUp")

        if (form[0].checkValidity() === false) {
            event.preventDefault()
            event.stopPropagation()
        }

        console.log("PErasan ton elegxo");
        //  event.preventDefault()
        form.addClass('was-validated');

        console.log($("#fullname").val());
        console.log($("#username").val());
        console.log($("#email").val());
        console.log($("#password").val());
        $.ajax({
            type: "POST",
            dataType: "json",
            url: "https://localhost:8765/observatory/api/signup",
            data:{"fullname":$("#fullname").val(),"username":$("#username").val(),"email":$("#email").val(),"password":$("#password").val()},
            success: function(data){
                console.log("Success");
                var obj = JSON.parse(JSON.stringify(data));
                var token = obj.token;
                console.log(token);
                window.sessionStorage.setItem("token", token);
                window.sessionStorage.setItem("username", $("#username").val());
                $(".modal-dialog").hide();
            //    $("#signup").html("<div class=\"h2\">Welcome "+$("#username").val()+"</div>");
                //redirect after 1 seconds
                $("body").load("success.html");
                window.setTimeout(function() {
                    window.location.href = 'Homepage.html';}, 1000);
                    //window.location.href="product.html";
            },
            error: function(){
                $("#error").text("Error to create new account");
                $("#formSignUp")[0].reset();
                console.log("signup.js : Error to singup");
            }
        });
    };

    $("#formSignUp").keypress(function(event) {
        if(event.keyCode==13) {
            console.log("Enter");
            signup(event);
        }
    }); 

    $("#btnSignUp").click(function(event) {

        signup(event);
    });

});
