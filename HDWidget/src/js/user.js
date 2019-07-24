user = {
    enter: function(){
        var req = {
            "login": byClass(byId('regPage'), 'login')[0].value,
        }
        wssConnector.send("/method/user/enter", JSON.stringify(req), {});
    },

    callOper: function() {
        wssConnector.send("/method/oper/call", "", {});
    },

    sendMessage: function(message) {
        console.log("message to send : " + message);
        wssConnector.send("/method/user/message", JSON.stringify({
            'message': message,
        }), {});

    },

    userCheck: function(){
        console.log("check user");
        wssConnector.send("/method/user/check","", {});
    }

}