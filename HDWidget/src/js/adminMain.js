var wssConnector;

addEvent(window, "load", function () {
    if(getCookie("sessionIdOper")){
        addClass(byId("regPage"), "hide");
        byId("admin-name").innerHTML = "Вы вошли как " + getCookie("operName");
    }
});