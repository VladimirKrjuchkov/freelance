Handlers = {

    userEnterHandler: function(input){
        input = JSON.parse(input.body);
        if(input.ok){
            var status = input.account.status;
            if(status == "DISCONNECTED") {
                rmClass(byId("call-oper-button"), "hide");
            }else{
                pullMessage(input.message, input.account.sessionId, "firstPage");
            }
            byId("user-name").innerHTML = "Вы вошли как " + input.account.login;
            addClass(byId("regPage"), "hide");
            rmClass(byId("firstPage"), "hide");
            setCookie("wssConnector", wssConnector, input.sessionExp);
        }else{
            alert(input.message);
        }
    },

    operCallHandler: function(input){
        input = JSON.parse(input.body);
        if(input.ok){
            pullMessage(input.message, input.account.sessionId, "firstPage");
            addClass(byId("call-oper-button"), "hide");
        }else{
            alert(input.message);
        }
    },

    messagingResultHandler: function(input){
        input = JSON.parse(input.body);
        pushMessage(input.message, Object.values(input.account.connectedAccounts)[0].sessionId, "firstPage");
    },

    inputMessagesHandler: function (input) {
        input = JSON.parse(input.body);
        pullMessage(input.message, input.account.sessionId, "firstPage");
    },

    userCheckHandler: function(input){
        input = JSON.parse(input.body);
        if(input.account){
            setCookie("wssConnector", wssConnector, input.sessionExp);
            addClass(byId("regPage"), "hide");
            rmClass(byId("firstPage"), "hide");
        }
    }
};