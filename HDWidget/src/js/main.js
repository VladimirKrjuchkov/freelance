// Onload configuration
addEvent(window, "load", function () {
    StompOverSock.getInstance(true);
});