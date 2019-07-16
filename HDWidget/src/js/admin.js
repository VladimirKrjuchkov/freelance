
admin = {
    registerOper: function(){
        var req = {
            "login": byClass(byId('regPage'), 'login')[0].value,
            "password": byClass(byId('regPage'), 'password')[0].value
        }
        ajax("/api/oper/register", function (result) {
            var result = JSON.parse(result);
            if (result.ok) {
                byId("admin-name").innerHTML = "Вы вошли как " + result.message;
                addClass(byId("regPage"), "hide");
                rmClass(byId("firstPage"), "hide");
                wssConnector = StompOverSock.getInstance(true);
                window.setTimeout(function () {
                    wssConnector.send("/method/createAccount", JSON.stringify({
                        'message': '',
                        'sessionId': getCookie("sessionIdOper")
                    }), {});
                    wssConnector.subscribe("/user/queue/input/requests", Handlers.inputRequestsHandler);
                }, 100);
            }
        }, JSON.stringify(req), function (result) {
            var result = JSON.parse(result);
            alert("Во время регистрации произошла ошибка!");
            console.log("Во время регистрации произошла ошибка!" + result);
        }, "POST", "application/json");
    },

    sendMessage: function(id, message) {
        console.log("message to send : " + message);
        wssConnector.send("/method/fromAdmin", JSON.stringify({
            'message': message,
            'sessionId': getCookie('sessionIdOper'),
            'clientId': id
        }), {});
        pushMessage(message);
    },
}