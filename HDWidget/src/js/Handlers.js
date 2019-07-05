Handlers = {

    inputRequestsHandler: function(){
        confirm("принять входящее соединение?");
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