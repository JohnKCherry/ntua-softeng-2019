$(document).ready(function(){


    // post login
    var login = function($payload) {
        console.log($payload.username);
    }


    $("#btnLogin").click(function(event) {

        //Fetch form to apply custom Bootstrap validation
        var form = $("#formLogin")

        if (form[0].checkValidity() === false) {
            event.preventDefault()
            event.stopPropagation()
        }

        console.log("PErasan ton elegxo");
      //  event.preventDefault()
        form.addClass('was-validated');

        $.ajax({
            type: "POST",
            dataType: "json",
            url: "http://localhost:8765/observatory/api/login",
            data:{"username":$("#username").val(),"password":$("#password").val()},
            success: function(data){
                console.log("Success");
                var obj = JSON.parse(JSON.stringify(data));
                var token = obj.token;
                console.log(token);
                window.localStorage.setItem("token", token);
                window.localStorage.setItem("username", $("#username").val());
                var b = window.localStorage.getItem("token");
                console.log(b);
             //   location.reload();
            },
            error: function(){
                console.log("Login.js : Error to login");
            }
        });
    });
});
