// Onload configuration

var adminHistory;
var wssConnector;

addEvent(window, "load", function () {
    wssConnector = StompOverSock.getInstance(true);
    window.setTimeout(function(){
        // wssConnector.subscribe("/user/queue/answer/toAdmin", this.adminListener);
        adminHistory = (localStorage.getItem("adminHistory") == null)?[]:localStorage.getItem("adminHistory").split(",");
        byClass(byId('firstPage'), 'chat')[0].innerHTML = adminHistory.join("\n");
    }, 100);
});

function adminListener(message){
    var text = JSON.parse(message).message;
    adminHistory.push(text);
    localStorage.setItem("adminHistory", adminHistory);
    byClass(byId('firstPage'), 'chat')[0].innerHTML = adminHistory.join("\n");
}

function sendMessage(message){
    console.log("message to send : " + message);
    wssConnector.send("/method/fromAdmin", JSON.stringify({'message' : message}), {});
    adminHistory.push(message);
    localStorage.setItem("adminHistory", adminHistory);
    byClass(byId('firstPage'), 'chat')[0].innerHTML = adminHistory.join("\n");
    byClass(byId('firstPage'), 'message')[0].value = "";
}