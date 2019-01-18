
        $.ajax({
            type: "POST",
            dataType: "json",
            headers: {'X-OBSERVATORY-AUTH' : '25276e4fd3b907a8a11989dc00df23df9fe0a1ef7a6f1c1e6655dbdb88f00c5adaniel'},
            url: "http://localhost:8765/observatory/api/profile",
            success: function(data){
                console.log("Success");
                var obj = JSON.parse(JSON.stringify(data));
                console.log(obj);
            },
            error: function(){
                console.log("Login.js : Error to login");
            }
        });
