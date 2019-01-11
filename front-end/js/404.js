$(document).ready(function(){

    var error = getUrlParameter('error');
    var errorStr;
    if ( error == 1 )  errorStr = "Product Not Found";
    else if ( error == 2 ) errorStr = "Shops Not Found";
    else if ( error == 3 ) errorStr = "Map Not Found";
    else errorStr = "Unknown Error";
    $("#error").text(errorStr);

});
var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = window.location.search.substring(1),
    sURLVariables = sPageURL.split('&'),
    sParameterName,
    i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
};
