Handlers = {

    inputRequestsHandler: function(input){
        console.log(input);
        input = JSON.parse(input.body);
        if(input.requestType == "CALL_OPER"){
            dialogs.appendChild(buildNode("DIV", {id: input.clientId, className: "pure-form"},
                               [buildNode("DIV", {className: 'pure-form'}, "Вы подключены к клиенту " + input.clientId),
                                buildNode("TEXTAREA", {className: 'chat'}),
                                buildNode("BR"),
                                buildNode("BR"),
                                buildNode("INPUT", {className: 'message', type: 'text', placeholder: "Введите сообщение"}),
                                buildNode("BR"),
                                buildNode("BR"),
                                buildNode("BUTTON", {className: 'pure-u-1 pure-button card-0 primary'}, "Отправить", {click: function(){admin.sendMessage(input.clientId, byClass(byId(input.clientId), 'message')[0].value);}})]))
            rmClass(byId("dialogs"), "hide");

        }else {
            pullMessage(input.message, input.sessionId);
        }
    },

    resultHandler : function(message){
        var response = JSON.parse(message.body);
        if(!response.ok){
            alert(response.message)
        }else{
            chatHistory.push(response.message);
            localStorage.setItem("chatHistory", chatHistory);
            byClass(byId('firstPage'), 'chat')[0].innerHTML = chatHistory.join("\n");
        }
        // сделать грамотный обработчик для неотправленных сообщений!!!
    }
};