$(document).ready(function(){

    $("#logout").on('click', function(){
        console.log("Logout.js: Kanw logout");
        var token = window.sessionStorage.getItem("token");
        if ( token == "" ) {
            console.log("Logout.js: Den eisai kan sindedemenos");
            return ;
        }
        $.ajax({
            type: "POST",
            dataType: "json",
            headers: {'X-OBSERVATORY-AUTH' : token},
            url: "https://localhost:8765/observatory/api/logout",
            success: function(data){
                console.log("Success");
                var obj = JSON.parse(JSON.stringify(data));
                console.log(obj);
                window.sessionStorage.removeItem("token", token);
                location.reload();
            },
            error: function(err){
                console.log("Logout.js : Error to logout");
                console.log(err);
            }
        });
    }
                   );
}
                 );
