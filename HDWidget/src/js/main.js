// Onload configuration

var chatHistory;
var wssConnector;

addEvent(window, "load", function () {
    wssConnector = StompOverSock.getInstance(true);
    window.setTimeout(function(){
        wssConnector.subscribe("/user/queue/answer/sendResult", this.messagingResultHandler);
        chatHistory = (localStorage.getItem("chatHistory") == null)?[]:localStorage.getItem("chatHistory").split(",");
        byClass(byId('firstPage'), 'chat')[0].innerHTML = chatHistory.join("\n");
    }, 100);
});

function sendMessage(message){
    console.log("message to send : " + message);
    wssConnector.send("/method/fromUser", JSON.stringify({'message' : message}), {});
    chatHistory.push(message);
    localStorage.setItem("chatHistory", chatHistory);
    byClass(byId('firstPage'), 'chat')[0].innerHTML = chatHistory.join("\n");
    byClass(byId('firstPage'), 'message')[0].value = "";
}

function messagingResultHandler(message){
    // сделать грамотный обработчик для неотправленных сообщений!!!
}