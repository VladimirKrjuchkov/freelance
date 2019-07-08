// Onload configuration

var wssConnector;

addEvent(window, "load", function () {
    if(!getCookie("sessionIdUser")) {
        if (confirm("Войти в чат?")) {
            registerUser()
        }
    }else{
        rmClass(byId("firstPage"), "hide");
    }
});

function registerUser() {
    ajax("/api/user/register",function(result){
        var result = JSON.parse(result);
        if(result.ok) {
            rmClass(byId("firstPage"), "hide");
        }else {
            alert(result.message);
        }
    },null, function(result){
        var result = JSON.parse(result);
        alert("Во время регистрации произошла ошибка!");
        console.log("Во время регистрации произошла ошибка!" + result);
    },"POST", "application/json");
}

function sendMessage(message){
    if(!getCookie("operId")){
        if(confirm("Подключить оператора?")){
            callOper();
        }
    }else{
        console.log("message to send : " + message);
        wssConnector.send("/method/fromUser", JSON.stringify({'message' : message, 'sessionId' : getCookie('sessionIdUser')}), {});
        pushMessage(message);
    }
}

function callOper(){
    ajax("/api/oper/call",function(result){
        result = JSON.parse(result);
        if(!result.ok){
            alert(result.message)
        }else{
            wssConnector = StompOverSock.getInstance(true);
            window.setTimeout(function () {
                // wssConnector.subscribe("/user/queue/answer/sendResult", Handlers.resultHandler);
                pushMessage(result.message);
            }, 1000);
        }
    }, null, function(result){
        var result = JSON.parse(result);
        alert("Во время вызова оператора произошлда ошибка!");
        console.log("Во время вызова оператора произошлда ошибка!" + result);
    },"GET");
}