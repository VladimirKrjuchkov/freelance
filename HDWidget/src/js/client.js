client = {
    registerUser: function() {
        ajax("/api/user/register", function (result) {
            var result = JSON.parse(result);
            if (result.ok) {
                rmClass(byId("call-oper-button"), "hide");
            } else {
                alert(result.message);
            }
        }, null, function (result) {
            var result = JSON.parse(result);
            alert("Во время регистрации произошла ошибка!");
            console.log("Во время регистрации произошла ошибка!" + result);
        }, "POST", "application/json");
    },

    sendMessage: function(message) {
        console.log("message to send : " + message);
        wssConnector.send("/method/fromUser", JSON.stringify({
            'message': message,
            'sessionId': getCookie('sessionIdUser')
        }), {});
        client.pushMessage(message, getCookie('operId'));
    },

    pullMessage: function(message, roomId){
        if(!message){
            message = "";
        }
        chatHistory = (localStorage.getItem("chatHistory_" + roomId) == null)?[]:localStorage.getItem("chatHistory_" + roomId).split(endMarker);
        if(message != "") {
            chatHistory.push(getDateLable() + "   " + message);
        }
        localStorage.setItem("chatHistory_" + roomId, chatHistory.join(endMarker));
        byClass(byId("firstPage"), 'chat')[0].innerHTML = chatHistory.join("\n").replace(/--endMesMark/g, "");
    },

    callOper: function() {
        if (!getCookie("operId") && !getCookie("sessionIdOper")) {
            ajax("/api/oper/call", function (result) {
                result = JSON.parse(result);
                if (!result.ok) {
                    alert(result.message)
                } else {
                    addClass(byId("call-oper-button"), "hide");
                    rmClass(byId("firstPage"), "hide");
                    wssConnector = StompOverSock.getInstance(true);
                    window.setTimeout(function () {
                        wssConnector.send("/method/createUserAccount", JSON.stringify({
                            'message': '',
                            'sessionId': getCookie("sessionIdUser")
                        }), {});
                        wssConnector.subscribe("/user/queue/input/requests", Handlers.inputRequestsHandlerUser);
                        client.pushMessage(result.message, getCookie('operId'));
                    }, 1000);
                }
            }, null, function (result) {
                var result = JSON.parse(result);
                alert("Во время вызова оператора произошлда ошибка!");
                console.log("Во время вызова оператора произошлда ошибка!" + result);
            }, "GET");

        } else {
            alert("Вы уже подключены к оператору");
        }
    },

    pushMessage: function(message, roomId){
        if(!message){
            message = "";
        }
        chatHistory = (localStorage.getItem("chatHistory_" + roomId) == null)?[]:localStorage.getItem("chatHistory_" + roomId).split(endMarker);
        if(message != "") {
            chatHistory.push(getDateLable() + "   " + message);
        }
        localStorage.setItem("chatHistory_" + roomId, chatHistory.join(endMarker));
        byClass(byId('firstPage'), 'chat')[0].innerHTML = chatHistory.join("\n").replace(/--endMesMark/g, "");
        byClass(byId('firstPage'), 'message')[0].value = "";
    }
}