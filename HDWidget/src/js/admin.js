// Onload configuration

var adminHistory;
var wssConnector;

function register(){
    var req = {
        "login" : byClass(byId('regPage'), 'login')[0].value,
        "password" : byClass(byId('regPage'), 'password')[0].value
    }
    ajax("/api/admin/register",function(){
        alert("Вы успешно зарегестрировались!");
        addClass(byId("regPage"), "hide");
        rmClass(byId("firstPage"), "hide");
        wssConnector = StompOverSock.getInstance(true);
        window.setTimeout(function(){
            wssConnector.subscribe("/queue/input/requests", this.inputRequestsHandler);
        }, 100);
    },JSON.stringify(req),function(e){
        alert("Во время регистрации произошла ошибка!");
        console.log(e);
    },"POST", "application/json");
}

function inputRequestsHandler(){
    confirm("принять входящее соединение?");
}