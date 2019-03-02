



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
            $("#pName").append("<span class =\"h5\">" + obj.fullname + "</span><p>   </p>");
            $("#pUsername").append("<span class =\"h5\">" + obj.username + "</span><p>   </p>");
            $("#pEmail").append("<span class =\"h5\">" + obj.email + "</span><p>   </p>");
            

        },
        error: function(){
            console.log("profile Error");
            $("body").load("404.html");
            return false;
            // window.location.href = "404.html?error=1";
        }
        });
		
	    $("#editButton").click(function(){
		$("#newTag").val("");
		
		$("#editButton").css("visibility","hidden");
		$("#applyButton").css("visibility","visible");
		
		$("#shopLocation").replaceWith($('<div class=\"h4\">Physical Location: <input type=\"text\" id=\"shopLocation\" class="h4" value="' + Location + '"></input></div>'));
		$("#shopName").replaceWith($('<div class=\"h2\">Name: <input type=\"text\" id=\"shopName\"  class="h4" value="' + name + '"></input></div>'));
		$("#shopType").replaceWith($('<div class=\"h4\">Categories: <input type=\"text\" id=\"shopType\" class="h4" value="' + type + '"></input></div>'));
		
		
		
		
	    });





});