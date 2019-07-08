Handlers = {

    inputRequestsHandler: function(input){
        console.log(input);
        var chatHistory = (localStorage.getItem("chatHistory") == null)?[]:localStorage.getItem("chatHistory").split(endMarker);
        chatHistory.push(input.body);
        localStorage.setItem("chatHistory", chatHistory);
        byClass(byId('firstPage'), 'chat')[0].innerHTML = chatHistory.join("\n");
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