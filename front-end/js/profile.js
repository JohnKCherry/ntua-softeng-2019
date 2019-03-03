

var id;
var fullname;
var username;
var email;
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
            $("#pName").append("<span class =\"h4\">" + obj.fullname + "  </span><p>   </p>");
            $("#pUsername").append("<span class =\"h4\">" + obj.username + "</span><p>   </p>");
            $("#pEmail").append("<span class =\"h4\">" + obj.email + "  </span><p>   </p>");
            fullname=obj.fullname;
			username=obj.username;
			email=obj.email;

        },
        error: function(){
            console.log("profile Error");
            $("body").load("404.html");
            return false;
            // window.location.href = "404.html?error=1";
        }
        });
		
	    $("#editButton").click(function(){
		$("#pName").replaceWith($('<div id=\"pName\" class=\"h2\"><u>Full Name:</u> <input type=\"text\" id=\"pName2\" class="h4" value="' + fullname + '"></input></div>'));
		$("#editButton").css("visibility","hidden");
		$("#applyButton").css("visibility","visible");
		$("#editButton1").css("visibility","hidden");
		$("#editButton2").css("visibility","hidden");
		
		
	    });
		
		$("#editButton1").click(function(){
		$("#pUsername").replaceWith($('<div id=\"pUsername\" class=\"h2\"><u>Username:</u> <input type=\"text\" id=\"pUsername2\" class="h4" value="' + username + '"></input></div>'));
		$("#editButton1").css("visibility","hidden");
		$("#applyButton1").css("visibility","visible");
		$("#editButton").css("visibility","hidden");
		$("#editButton2").css("visibility","hidden");
		
		
	    });
		
		$("#editButton2").click(function(){
		$("#pEmail").replaceWith($('<div id=\"pEmail\" class=\"h2\"><u>Email:</u> <input type=\"text\" id=\"pEmail2\" class="h4" value="' + email + '"></input></div>'));
		$("#editButton2").css("visibility","hidden");
		$("#applyButton2").css("visibility","visible");
		$("#editButton").css("visibility","hidden");
		$("#editButton1").css("visibility","hidden");
		
		
	    });
		
		$("#applyButton").click(function(){
		 fullname = $("#pName2").val();
		 console.log(fullname);
		 sendUpdate(fullname);
		
		});
			
	    $("#applyButton1").click(function(){
	     username= $("#pUsername2").val();
		 console.log(username);
		 sendUpdate1(username);
		
		});
		
		$("#applyButton2").click(function(){
		 email = $("#pEmail2").val();
		 id=3;
		 console.log(email);
		 sendUpdate2(email);
		
		});
		
		function sendUpdate(fullname) {
        $.ajax({
            type: "PATCH",
            dataType: "json",
			headers: {'X-OBSERVATORY-AUTH' : token},
            url: "https://localhost:8765/observatory/api/profile",
            data: fullname,
            success: function(data){
                console.log("Success");
                var obj = JSON.parse(JSON.stringify(data));
                console.log(obj);
                location.reload();
            },
            error: function(err){
                console.log(err);
                console.log("PATCH Error");
                alert("Error update");
                location.reload();
            }
        });
		}
		
		function sendUpdate1(username) {
        $.ajax({
            type: "PATCH",
            dataType: "json",
			headers: {'X-OBSERVATORY-AUTH' : token},
            url: "https://localhost:8765/observatory/api/profile",
            data: username,
            success: function(data){
                console.log("Success");
                var username = JSON.parse(JSON.stringify(data));
                console.log(obj);
                location.reload();
            },
            error: function(err){
                console.log(err);
                console.log("PATCH Error");
                alert("Error update");
                location.reload();
            }
        });
		}
		
		
		function sendUpdate2(email) {
        $.ajax({
            type: "PATCH",
            dataType: "json",
			headers: {'X-OBSERVATORY-AUTH' : token},
            url: "https://localhost:8765/observatory/api/profile",
            data: email,
            success: function(data){
                console.log("Success");
                var email = JSON.parse(JSON.stringify(data));
                console.log(email);
                location.reload();
            },
            error: function(err){
                console.log(err);
                console.log("PATCH Error");
                alert("Error update");
                location.reload();
            }
        });
		
		
    }
		
		
		





});