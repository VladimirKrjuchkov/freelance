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











/**************************************************До этой полосы все что мне нужно будет на клиенте, остальное уйдет в HelpDesk*************************************************/
    // inputRequestsHandler: function(input){
    //     console.log(input);
    //     input = JSON.parse(input.body);
    //     if(input.requestType == "CALL_OPER"){
    //         dialogs.appendChild(buildNode("DIV", {id: input.clientId, className: "pure-form"},
    //                            [buildNode("DIV", {className: 'pure-form'}, "Вы подключены к клиенту " + input.clientId),
    //                             buildNode("TEXTAREA", {className: 'chat'}),
    //                             buildNode("BR"),
    //                             buildNode("BR"),
    //                             buildNode("INPUT", {className: 'message', type: 'text', placeholder: "Введите сообщение"}),
    //                             buildNode("BR"),
    //                             buildNode("BR"),
    //                             buildNode("BUTTON", {className: 'pure-u-1 pure-button card-0 primary'}, "Отправить", {click: function(){admin.sendMessage(input.clientId, byClass(byId(input.clientId), 'message')[0].value);}})]))
    //         rmClass(byId("dialogs"), "hide");
    //
    //     }else {
    //         admin.pullMessage(input.message, input.sessionId);
    //     }
    // },
    //
    // inputRequestsHandlerUser: function(input){
    //     console.log(input);
    //     input = JSON.parse(input.body);
    //     client.pullMessage(input.message, getCookie("operId"));
    // },
    //
    // resultHandler : function(message){
    //     var response = JSON.parse(message.body);
    //     if(!response.ok){
    //         alert(response.message)
    //     }else{
    //         chatHistory.push(response.message);
    //         localStorage.setItem("chatHistory", chatHistory);
    //         byClass(byId('firstPage'), 'chat')[0].innerHTML = chatHistory.join("\n");
    //     }
    //     // сделать грамотный обработчик для неотправленных сообщений!!!
    // }
};