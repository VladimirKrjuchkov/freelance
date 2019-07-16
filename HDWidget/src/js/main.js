var wssConnector;
addEvent(window, "load", function () {
    if (!getCookie("sessionIdUser")) {
        client.registerUser()
    }else{
        rmClass(byId("call-oper-button"), "hide");
    }
});