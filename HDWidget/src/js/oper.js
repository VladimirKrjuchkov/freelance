var wssConnector;
addEvent(window, "load", function () {
    wssConnector = getCookie("wssConnector");
    if(!wssConnector) {
        wssConnector = StompOverSock.getInstance(true);
        wssConnector.subscribe("/user/queue/answer/oper/enter", oper.operEnterHandler);
        wssConnector.subscribe("/user/queue/answer/oper/check", oper.userCheckHandler);
        wssConnector.subscribe("/user/queue/answer/oper/message", oper.messagingResultHandler);
        wssConnector.subscribe("/user/queue/answer/input/requests", oper.inputMessagesHandler);

    }else{
        oper.operCheck();
    }
});


oper = {
    enter: function(){
        var req = {
            "login": byClass(byId('regPage'), 'login')[0].value,
            "password": byClass(byId('regPage'), 'password')[0].value
        }
        wssConnector.send("/method/oper/enter", JSON.stringify(req), {});

    },

    messagingResultHandler: function(input){
        input = JSON.parse(input.body);
        if(input.ok) {
            pushMessage(input.message, input.userSessionId, input.userSessionId);

        }else{
            alert(input.message);
        }
    },

    inputMessagesHandler: function(input){
        input = JSON.parse(input.body);
        if(input.requestType == "CALL_OPER") {
            for (var i = 0; i < Object.values(input.account.connectedAccounts).length; i++) {
                var id = Object.values(input.account.connectedAccounts)[i];
                if(!byId(id.sessionId)) {
                    oper.buildDialogNode(id);
                }
            }
        }else{
            pullMessage(input.message, input.account.sessionId, input.account.sessionId);
        }
    },

    operCheck: function(){
        console.log("oper user");
        wssConnector.send("/method/oper/check","", {});
    },

    buildDialogNode: function(account){
        dialogs.appendChild(buildNode("DIV", {id: account.sessionId, className: "pure-form"},
            [buildNode("DIV", {className: 'pure-form'}, "Вы подключены к клиенту " + account.login),
            buildNode("TEXTAREA", {className: 'chat'}),
            buildNode("BR"),
            buildNode("BR"),
            buildNode("INPUT", {className: 'message', type: 'text', placeholder: "Введите сообщение"}),
            buildNode("BR"),
            buildNode("BR"),
            buildNode("BUTTON", {className: 'pure-u-1 pure-button card-0 primary'}, "Отправить", {click: function(){oper.sendMessage(account.sessionId, byClass(byId(account.sessionId), 'message')[0].value);}})]))
    },

    operEnterHandler: function(input){
        input = JSON.parse(input.body);
        byId("admin-name").innerHTML = "Вы вошли как " + input.account.login;
        addClass(byId("regPage"), "hide");
        rmClass(byId("dialogs"), "hide");
        setCookie("wssConnector", wssConnector, input.sessionExp);
        if(input.account.connectedUsers) {
            for (var i = 0; i < Object.keys(input.account.connectedUsers).length; i++) {
                oper.buildDialogNode(input, Object.keys(input.account.connectedUsers)[i]);
            }
        }
    },

    operCheckHandler: function(input){
        input = JSON.parse(input.body);
        if(input.account){
            setCookie("wssConnector", wssConnector, input.sessionExp);
            addClass(byId("regPage"), "hide");
            rmClass(byId("dialogs"), "hide");
            for(var i=0; i<Object.keys(input.account.connectedUsers).length; i++){
                oper.buildDialogNode(input, Object.keys(input.account.connectedUsers)[i]);
            }
        }
    },

    sendMessage: function(id, message) {
        console.log("message to send : " + message);
        wssConnector.send("/method/oper/message", JSON.stringify({
            'message': message,
            'userSessionId': id
        }), {});
    }
}