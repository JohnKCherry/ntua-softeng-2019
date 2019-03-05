var token = window.sessionStorage.getItem("token");

var no_prices = [0,0];

var no_users = [0];
var no_products = [0];
var no_shops = [0];

$(document).ready(function () {

    document.body.style.backgroundColor = "mintcream";

    console.log("ready");

    console.log("Token ");
    console.log(token);
    if (token != null) {
        $("#loginBtn").text(window.sessionStorage.getItem("username"));
        $("#loginBtn").attr("href", "");
        $("#editButton").css("visibility", "visible");
    }
    else {
        $("#loginBtn").show();
    }

    noOfProducts();
    noOfAcivefPrices();
    noOfAcivefCrowdsources();
    noOfActiveProducts();
    noOfActiveShops();
    noOfAdmins();
    noOfBanUsers();
    noOfPastPrices();
    noOfShops();
    noOfUsers();
    noOfWithdrawnShops();
    noOfWithdrawnProducts();
    
    document.getElementById("totalPrices").append(parseInt(no_prices[0])+parseInt(no_prices[1]));

    getUsers();
    getproducts();
    getshops();

    $('#selectType :button').click( function(){
        var classT = '.' + $(this).attr('id');
        var status = $(this).css('color');
        if(status === "rgb(169, 169, 169)"){
            $(classT).hide();
            $(this).css('color','lightgray')
            $(this).css('background-color','darkgray')
        }
        else{
            $(classT).show();
            $(this).css('color','darkgray')
            $(this).css('background-color','lightgray')
        }
    });


    
    $('[id=editUserButton]').click( function() {
        var id= $(this).closest("div").attr("id");
        console.log(id);
        var choice = $("#uchoise"+id + " option:selected").text();

        updateAuthToken(id,choice);
    })

    $('[id=editProdButton]').click( function() {
        
        
        var id= $(this).closest("div").attr("id");
        var choice = $("#pchoise"+id + " option:selected").text();
        updateProduct(id,choice);
    });

    $('[id=editShopButton]').click( function() {
        
        
        var id= $(this).closest("div").attr("id");
        var choice = $("#pchoise"+id + " option:selected").text();
        updateShop(id,choice);
    });

})
    
function noOfProducts() {

    $.ajax({
        type: "GET",
        dataType: "json",
        headers: { 'X-OBSERVATORY-AUTH': token },
        url: "https://localhost:8765/observatory/api/numberofproducts",
        success: function (data) {
            //console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            document.getElementById("noOfProducts").append(obj.value);
            no_products[0] = obj.value;

        },
        error: function () {
            console.log("Error");
            return false;

        }
    });

}

function noOfAcivefPrices(activePrice) {

    $.ajax({
        type: "GET",
        async: false,
        dataType: "json",
        headers: { 'X-OBSERVATORY-AUTH': token },
        url: "https://localhost:8765/observatory/api/numberofactiveprices",
        success: function (data) {
            //console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            document.getElementById("noOfAciveofPrices").append(obj.value);
            no_prices[0] = obj.value;
        },
        error: function () {
            console.log("Error");
            return false;

        }
    });
}

function noOfAcivefCrowdsources() {
    $.ajax({
        type: "GET",
        dataType: "json",
        headers: { 'X-OBSERVATORY-AUTH': token },
        url: "https://localhost:8765/observatory/api/numberofactiveusers",
        success: function (data) {
            //console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            document.getElementById("noOfAciveofCrowdsourcers").append(obj.value);


        },
        error: function () {
            console.log("Error");
            return false;

        }
    });

}

function noOfActiveProducts() {
    $.ajax({
        type: "GET",
        dataType: "json",
        headers: { 'X-OBSERVATORY-AUTH': token },
        url: "https://localhost:8765/observatory/api/numberofactiveproducts",
        success: function (data) {
            //console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            document.getElementById("noOfActiveProducts").append(obj.value);


        },
        error: function () {
            console.log("Error");
            return false;

        }
    });
}

function noOfActiveShops() {
    $.ajax({
        type: "GET",
        dataType: "json",
        headers: { 'X-OBSERVATORY-AUTH': token },
        url: "https://localhost:8765/observatory/api/numberofactiveproducts",
        success: function (data) {
            //console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            document.getElementById("noOfActiveShops").append(obj.value);


        },
        error: function () {
            console.log("Error");
            return false;

        }
    });
}

function noOfAdmins() {
    $.ajax({
        type: "GET",
        dataType: "json",
        headers: { 'X-OBSERVATORY-AUTH': token },
        url: "https://localhost:8765/observatory/api/numberofadmins",
        success: function (data) {
            //console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            document.getElementById("noOfAdmins").append(obj.value);


        },
        error: function () {
            console.log("Error");
            return false;

        }
    });
}

function noOfBanUsers() {
    $.ajax({
        type: "GET",
        dataType: "json",
        headers: { 'X-OBSERVATORY-AUTH': token },
        url: "https://localhost:8765/observatory/api/numberofbanusers",
        success: function (data) {
            //console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            document.getElementById("noOfBanUsers").append(obj.value);


        },
        error: function () {
            console.log("Error");
            return false;

        }
    });
}

function noOfPastPrices() {
    $.ajax({
        type: "GET",
        async: false,
        dataType: "json",
        headers: { 'X-OBSERVATORY-AUTH': token },
        url: "https://localhost:8765/observatory/api/numberofpastprices",
        success: function (data) {
            //console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            document.getElementById("noOfPastPrices").append(obj.value);

            no_prices[1] = obj.value;

        },
        error: function () {
            console.log("Error");
            return false;

        }
    });
}

function noOfShops() {
    $.ajax({
        type: "GET",
        dataType: "json",
        headers: { 'X-OBSERVATORY-AUTH': token },
        url: "https://localhost:8765/observatory/api/numberofshops",
        success: function (data) {
            //console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            document.getElementById("noOfShops").append(obj.value);
            no_shops[0] = obj.value;

        },
        error: function () {
            console.log("Error");
            return false;

        }
    });
}

function noOfUsers() {
    $.ajax({
        type: "GET",
        async : false,
        dataType: "json",
        headers: { 'X-OBSERVATORY-AUTH': token },
        url: "https://localhost:8765/observatory/api/numberofusers",
        success: function (data) {
            //console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            document.getElementById("noOfUsers").append(obj.value);
            no_users[0] = obj.value


        },
        error: function () {
            console.log("Error");
            return false;

        }
    });
}

function noOfWithdrawnShops() {
    $.ajax({
        type: "GET",
        dataType: "json",
        headers: { 'X-OBSERVATORY-AUTH': token },
        url: "https://localhost:8765/observatory/api/numberofwithdrawnshops",
        success: function (data) {
            //console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            document.getElementById("noOfWithdrawnShops").append(obj.value);


        },
        error: function () {
            console.log("Error");
            return false;

        }
    });
}

function noOfWithdrawnProducts() {
    $.ajax({
        type: "GET",
        dataType: "json",
        headers: { 'X-OBSERVATORY-AUTH': token },
        url: "https://localhost:8765/observatory/api/numberofwithdrawnproducts",
        success: function (data) {
            //console.log(data);
            var obj = JSON.parse(JSON.stringify(data));
            document.getElementById("noOfWithdrawnProducts").append(obj.value);


        },
        error: function () {
            console.log("Error");
            return false;

        }
    });
}

function getUsers(){
    $.ajax({
        type:"GET",
        async:false,
        dataType : "json",
        url: "https://localhost:8765/observatory/api/users?start=0&count="+ no_users[0] +"&status=ALL&sort=username|ASC",
        success: function (data) {
            var cnt=0;
            var obj = JSON.parse(JSON.stringify(data));
            obj.users.forEach(t => {

                //console.log(t.username + " " + t.fullname + " " + t.status);

                var table = document.getElementById("user-list");
                var row = table.insertRow(table.length);
                row.className = (t.status).replace(' ','_');
                var cell1 = row.insertCell(0);
                var cell2 = row.insertCell(1);
                var cell3 = row.insertCell(2);
                var cell4 = row.insertCell(3);

                cell1.innerHTML = t.username;
                cell2.innerHTML = t.fullname;
                cell3.innerHTML = t.status;
                var message1="";
                var message2="";
                var message3="";
                
                if((t.status).replace(' ',"_") === "Blocked_Crowdsourcer") message1 = "selected";
                if((t.status).replace(' ',"_") === "Active_Crowdsourcer") message2 = "selected";
                if((t.status).replace(' ',"_") === "Administrator") message3 = "selected";

                cell4.innerHTML = "<div id=\"" + t.id + "\"><select id=\"uchoise" + t.id + "\"> <option " +  message1 + ">1</option> <option " + message2+  " + >2</option><option " + message3 + ">3</option></select><button id=\"editUserButton\"><i class=\"fas fa-user-edit\"></i></button></div>";
            })

        },
        error: function () {
            console.log("Error");
            return false;

        }
    });

}

function getproducts(){
    $.ajax({
        type:"GET",
        async:false,
        dataType : "json",
        url: "https://localhost:8765/observatory/api/products?start=0&count="+ no_products[0] +"&status=ALL&sort=id|ASC",
        success: function (data) {
            var cnt=0;
            var obj = JSON.parse(JSON.stringify(data));


            //console.log(obj);
            obj.products.forEach(t => {

                //console.log(t);
                
                var table = document.getElementById("products-list");
                var row = table.insertRow(table.length);
                //row.className = (t.status).replace(' ','_');
                var cell1 = row.insertCell(0);
                var cell2 = row.insertCell(1);
                var cell3 = row.insertCell(2);
                var cell4 = row.insertCell(3);
                var cell5 = row.insertCell(4);

                cell1.innerHTML = t.id;
                cell2.innerHTML = t.name;
                cell3.innerHTML = t.category;
                var message1="";
                var message2="";
                
                if(!t.withdrawn) message1 = "selected";
                else message2="selected";

                cell4.innerHTML = "<div id=\"" + t.id + "\"><select id=\"pchoise" + t.id + "\"> <option " +  message1 + ">Active</option> <option " + message2+  " + >Withdrawn</option></select><button id=\"editProdButton\"><i class=\"fas fa-user-edit\"></i></button></div>";
                cell5.innerHTML = "<button id="+ t.id +" onclick=deleteProduct("+t.id+")><i class=\"far fa-trash-alt\"></i></button>";
                
            })

        },
        error: function () {
            console.log("Error");
            return false;

        }
    });

}

function getshops(){
    $.ajax({
        type:"GET",
        async:false,
        dataType : "json",
        url: "https://localhost:8765/observatory/api/shops?start=0&count="+ no_shops[0] +"&status=ALL&sort=id|ASC",
        success: function (data) {
            var cnt=0;
            var obj = JSON.parse(JSON.stringify(data));


            //console.log(obj);
            obj.shops.forEach(t => {

                console.log(t);
                
                
                var table = document.getElementById("shop-list");
                var row = table.insertRow(table.length);
                //row.className = (t.status).replace(' ','_');
                var cell1 = row.insertCell(0);
                var cell2 = row.insertCell(1);
                var cell3 = row.insertCell(2);
                var cell4 = row.insertCell(3);
                var cell5 = row.insertCell(4);

                cell1.innerHTML = t.id;
                cell2.innerHTML = t.name;
                cell3.innerHTML = t.address;
                var message1="";
                var message2="";
                
                if(!t.withdrawn) message1 = "selected";
                else message2="selected";

                cell4.innerHTML = "<div id=\"" + t.id + "\"><select id=\"pchoise" + t.id + "\"> <option " +  message1 + ">Active</option> <option " + message2+  " + >Withdrawn</option></select><button id=\"editShopButton\"><i class=\"fas fa-user-edit\"></i></button></div>";
                cell5.innerHTML = "<button id="+ t.id +" onclick=deleteShop("+t.id+")><i class=\"far fa-trash-alt\"></i></button>"; 

            })

        },
        error: function () {
            console.log("Error");
            return false;

        }
    });

}




function openUser(){

   var a = document.getElementById("user-form");
   var x = document.getElementById("open-user-form-button");
   if(a.style.display == "none"){
        a.style.display = "block";
        x.style.backgroundColor = "darkgray";
        x.style.color = "lightgray";
   }
   else{
        a.style.display = "none";
        x.style.backgroundColor = "lightgray";
        x.style.color = "darkgray";
   }
}

function showProducts(){
    var a = document.getElementById("product-form");
    var x = document.getElementById("open-product-form-button");
    if(a.style.display == "none"){
         a.style.display = "block";
         x.style.backgroundColor = "darkgray";
         x.style.color = "lightgray";
    }
    else{
         a.style.display = "none";
         x.style.backgroundColor = "lightgray";
         x.style.color = "darkgray";
    }
 }

function showShops(){
    var a = document.getElementById("shop-form");
    var x = document.getElementById("open-shop-form-button");
    if(a.style.display == "none"){
         a.style.display = "block";
         x.style.backgroundColor = "darkgray";
         x.style.color = "lightgray";
    }
    else{
         a.style.display = "none";
         x.style.backgroundColor = "lightgray";
         x.style.color = "darkgray";
    }
}


function updateAuthToken(id,choice){
    var obj = new Object();
    obj.user_id = id;
    obj.authorization = choice;
    $.ajax({
        type: "PATCH",
        dataType: "json",
        headers: {'X-OBSERVATORY-AUTH' : token},
        url: "https://localhost:8765/observatory/api/profile",
        data : obj,
        success: function(data){
            var obj = JSON.parse(JSON.stringify(data));
            console.log("Success");
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

function updateProduct(id,choice){
    var obj = new Object();
    //obj.id= parseInt(id)
    if(choice === "Active") obj.withdrawn = "ACTIVE";
    else obj.status = "WITHDRAWN";
    console.log(obj);
    $.ajax({
        type: "PATCH",
        dataType: "json",
        headers: {'X-OBSERVATORY-AUTH' : token},
        url: "https://localhost:8765/observatory/api/products/"+id,
        data: obj,
        success: function(data){
            console.log("Success");
            var obj = JSON.parse(JSON.stringify(data));
            console.log(obj);
            location.reload();
        },
        error: function(err){
            console.log(err);
            console.log("product.js: Error sendUpdate");
            alert("Error update")
        }
    });
}

function updateShop(id,choice){
    var obj = new Object();
    //obj.id= parseInt(id)
    if(choice === "Active") obj.withdrawn = "ACTIVE";
    else obj.status = "WITHDRAWN";
    console.log(obj);
    $.ajax({
        type: "PATCH",
        dataType: "json",
        headers: {'X-OBSERVATORY-AUTH' : token},
        url: "https://localhost:8765/observatory/api/shops/"+id,
        data: obj,
        success: function(data){
            console.log("Success");
            var obj = JSON.parse(JSON.stringify(data));
            console.log(obj);
            location.reload();
        },
        error: function(err){
            console.log(err);
            console.log("product.js: Error sendUpdate");
            alert("Error update")
        }
    });
}
    
function deleteProduct(id){
    $.ajax({
        type:"DELETE",
        headers: {'X-OBSERVATORY-AUTH' : token},
        url : "https://localhost:8765/observatory/api/products/"+id,
        success:function(){
            console.log("Success");
            location.reload();
        },
        error: function() {
            console.log("Error");
            location.reload();
        }
    });
}

function deleteShop(id){
    $.ajax({
        type:"DELETE",
        headers: {'X-OBSERVATORY-AUTH' : token},
        url : "https://localhost:8765/observatory/api/shops/"+id,
        success:function(){
            console.log("Success");
            location.reload();
        },
        error: function() {
            console.log("Error");
            location.reload();
        }
    });
}