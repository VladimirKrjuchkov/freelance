// Onload configuration
addEvent(window, "load", function () {
    var wssConnector = StompOverSock.getInstance(true).send("/method/allWssReq", JSON.stringify("{Hello Vova!!!))}"), {});
});