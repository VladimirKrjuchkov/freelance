Handlers = {

    inputRequestsHandler: function(input){
        console.log(input);
        input = JSON.parse(input.body);
        if(input.requestType == "CALL_OPER"){
            dialogs.appendChild(buildNode("DIV", {id: input.clientId, className: "pure-form"},
                               [buildNode("TEXTAREA", {className: 'chat'}),
                                buildNode("BR"),
                                buildNode("BR"),
                                buildNode("INPUT", {className: 'message', type: 'text', placeholder: "Введите сообщение"}),
                                buildNode("BR"),
                                buildNode("BR"),
                                // buildNode("BUTTON", {className: 'pure-u-1 pure-button card-0 primary'}, "Отправить", {click:sendMessage(byClass(byId("firstPage"), "message")[0].value)})]))
                                buildNode("BUTTON", {className: 'pure-u-1 pure-button card-0 primary'}, "Отправить", {click: sendMessage(input.clientId)})]))
            rmClass(byId("dialogs"), "hide");

        }else {
            pullMessage(input.message);
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