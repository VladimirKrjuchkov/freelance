// Onload configuration

var adminHistory;
var wssConnector;

addEvent(window, "load", function () {
    if(getCookie("sessionIdOper")){
        addClass(byId("regPage"), "hide");
        rmClass(byId("firstPage"), "hide");
    }
});

function registerOper(){
    var req = {
        "login" : byClass(byId('regPage'), 'login')[0].value,
        "password" : byClass(byId('regPage'), 'password')[0].value
    }
    ajax("/api/oper/register",function(result){
        var result = JSON.parse(result);
        if(result.ok) {
            addClass(byId("regPage"), "hide");
            rmClass(byId("firstPage"), "hide");
            wssConnector = StompOverSock.getInstance(true);
            window.setTimeout(function () {
                wssConnector.send("/method/createAccount", JSON.stringify({'message' : '', 'sessionId' : getCookie("sessionIdOper")}), {});
                wssConnector.subscribe("/user/queue/input/requests", Handlers.inputRequestsHandler);
            }, 100);
        }
        alert(result.message);
    },JSON.stringify(req),function(result){
        var result = JSON.parse(result);
        alert("Во время регистрации произошла ошибка!");
        console.log("Во время регистрации произошла ошибка!" + result);
    },"POST", "application/json");
}