var wssConnector;
addEvent(window, "load", function () {
    if(getCookie("config")) {
        user.buildUserInfoNode(getCookie("config"));

    }else{
        alert("Не обнаружено настроек пользователя, используем стандартный сценарий")
    }
    // wssConnector = getCookie("wssConnector");
    // if(!wssConnector) {
    //     wssConnector = StompOverSock.getInstance(true);
    //     wssConnector.subscribe("/user/queue/answer/user/enter", Handlers.userEnterHandler);
    //     wssConnector.subscribe("/user/queue/answer/user/check", Handlers.userCheckHandler);
    //     wssConnector.subscribe("/user/queue/answer/user/message", Handlers.messagingResultHandler);
    //     wssConnector.subscribe("/user/queue/answer/oper/call", Handlers.operCallHandler);
    //     wssConnector.subscribe("/user/queue/answer/input/requests", Handlers.inputMessagesHandler);
    //
    // }else{
    //     user.userCheck();
    // }
});