
$(document).ready(function(){


    function login(event) {

        event.preventDefault()
        event.stopPropagation()
        //Fetch form to apply custom Bootstrap validation
        var form = $("#formLogin")

        if (form[0].checkValidity() === false) {
            event.preventDefault()
            event.stopPropagation()
        }

        console.log("PErasan ton elegxo");
        //  event.preventDefault()
        form.addClass('was-validated');

        console.log("username: " + $("#usernameLog").val() + " password " +$("#password").val());
        $.ajax({
            type: "POST",
            dataType: "json",
            url: "https://localhost:8765/observatory/api/login",
            data:{"username":$("#usernameLog").val(),"password":$("#password").val()},
            success: function(data){
                console.log("Success");
                var obj = JSON.parse(JSON.stringify(data));
                var token = obj.token;
                console.log(token);
                window.sessionStorage.setItem("token", token);
                window.sessionStorage.setItem("username", $("#usernameLog").val());
                var b = window.sessionStorage.getItem("token");
                console.log(b);
                location.reload();
            },
            error: function(err){
                console.log("Login.js : Error to login");
                console.log(err);
                $("#error").text("Wrong username or password");
                $("#formLogin")[0].reset();
            }
        });
    };

    $("#formLogin").keypress(function(event) {
        if(event.keyCode==13) {
            console.log("Enter");
            login(event);
        }
    }); 

    $("#btnLogin").on('click',function(event) {
        console.log("Login.js: Patithika");
        login(event);
    });

});
