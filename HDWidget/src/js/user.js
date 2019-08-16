user = {
    enter: function () {
        var req = {
            "login": byClass(byId('regPage'), 'login')[0].value,
        }
        wssConnector.send("/method/user/enter", JSON.stringify(req), {});
    },

    callOper: function () {
        wssConnector.send("/method/oper/call", "", {});
    },

    sendMessage: function (message) {
        console.log("message to send : " + message);
        wssConnector.send("/method/user/message", JSON.stringify({
            'message': message,
        }), {});

    },

    userCheck: function () {
        console.log("check user");
        wssConnector.send("/method/user/check", "", {});
    },

    buildUserInfoNode: function (config) {
        config = JSON.parse(JSON.parse(config));
        var regPage = byId("regPage");
        if (config.lastName) {
            regPage.appendChild(buildNode("DIV", {className: 'title'}, "Фамилия:"));
            regPage.appendChild(buildNode("INPUT", {className: 'last_name', type: 'text', placeholder: "Введите вашу фамилию"}));
        }
        if (config.firstName) {
            regPage.appendChild(buildNode("DIV", {className: 'title'}, "Имя:"));
            regPage.appendChild(buildNode("INPUT", {className: 'first_name', type: 'text', placeholder: "Введите ваше имя"}));
        }
        if (config.middleName) {
            regPage.appendChild(buildNode("DIV", {className: 'title'}, "Отчество:"));
            regPage.appendChild(buildNode("INPUT", {className: 'middle_name', type: 'text', placeholder: "Введите ваше отчество"}));
        }
        regPage.appendChild(buildNode("BR")),
        regPage.appendChild(buildNode("BR")),
        regPage.appendChild(buildNode("BUTTON", {className: 'pure-u-1 pure-button card-0 primary', type: 'submit'}, "Начать чат"));
    }
}