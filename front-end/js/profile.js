



var token = window.sessionStorage.getItem("token");
$(document).ready(function(){
        console.log("ready");
	
	    console.log("Token ");
        console.log(token);
		
		if (token != null) {
            $("#loginBtn").text(window.sessionStorage.getItem("username"));
            $("#loginBtn").attr("href","");
            $("#editButton").css("visibility","visible");
        }
        else {
             $("#loginBtn").show();
		}
		
		
		
		//GET INFO
		$.ajax({
        type: "POST",
        dataType: "json",
		headers: {'X-OBSERVATORY-AUTH' : token},
        url: "https://localhost:8765/observatory/api/profile",
		data: {token},
        success: function(data){
            console.log("GOT PROFILE DETAILS");
            var obj = JSON.parse(JSON.stringify(data));           
            $("#pName").append("<span class =\"h5\">" + obj.fullname + "</span>");
            $("#pUsername").append("<span class =\"h5\">" + obj.username + "</span>");
            $("#pEmail").append("<span class =\"h5\">" + obj.email + "</span>");
            

        },
        error: function(){
            console.log("profile Error");
            $("body").load("404.html");
            return false;
            // window.location.href = "404.html?error=1";
        }
        });
		
	





});