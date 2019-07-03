// Onload configuration

var chatHistory;
var wssConnector;

addEvent(window, "load", function () {
    wssConnector = StompOverSock.getInstance(true);
    window.setTimeout(function(){
        wssConnector.subscribe("/user/queue/answer/sendResult", this.resultHandler);
        chatHistory = (localStorage.getItem("chatHistory") == null)?[]:localStorage.getItem("chatHistory").split(",");
        byClass(byId('firstPage'), 'chat')[0].innerHTML = chatHistory.join("\n");
    }, 1000);
});

function sendMessage(message){
    if(!getCookie("operId")){
        if(confirm("Подключить оператора?")){
            wssConnector.send("/method/callOper", JSON.stringify({'message' : message}), {});
        }
    }else{
        console.log("message to send : " + message);
        wssConnector.send("/method/fromUser", JSON.stringify({'message' : message}), {});
        chatHistory.push(message);
        localStorage.setItem("chatHistory", chatHistory);
        byClass(byId('firstPage'), 'chat')[0].innerHTML = chatHistory.join("\n");
        byClass(byId('firstPage'), 'message')[0].value = "";
    }
}

function resultHandler(message){
    var response = JSON.parse(message);
    if(!response.ok){
        alert(response.message)
    }else{
        chatHistory.push(response.message);
        localStorage.setItem("chatHistory", chatHistory);
        byClass(byId('firstPage'), 'chat')[0].innerHTML = chatHistory.join("\n");
    }
    // сделать грамотный обработчик для неотправленных сообщений!!!
}