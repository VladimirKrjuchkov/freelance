package com.pb.tel.controller;

import com.pb.tel.data.AdminAccount;
import com.pb.tel.data.HttpResponse;
import com.pb.tel.data.RegisterRequest;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.enumerators.RequestType;
import com.pb.tel.service.websocket.WebSocketServer;
import com.pb.tel.service.websocket.data.WebSocketRequest;
import com.pb.tel.storage.Storage;
import com.pb.tel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


@Controller
public class HttpController {

    @Autowired
    private Environment environment;

    @Resource(name="adminStorage")
    private Storage<String, AdminAccount> adminStorage;

    @Resource(name="userStorage")
    private Storage<String, UserAccount> userStorage;

    @Resource(name="freeOpers")
    private List<String> freeOpers;

    private final Logger log = Logger.getLogger(HttpController.class.getCanonicalName());


    @RequestMapping(value = "/oper/register", method = RequestMethod.POST)
    @ResponseBody
    public HttpResponse adminRegister(@RequestBody RegisterRequest request, HttpServletResponse response) throws Exception{// <<<--- вместо всей этой дряни надо прикрутить basic- авторизацию!!!
        log.info("start registration, login: " + request.getLogin());
        AdminAccount adminAccount = new AdminAccount(UUID.randomUUID().toString());
        adminAccount.setLogin(request.getLogin());
        adminStorage.putValue(adminAccount.getSessionId(), adminAccount, Utils.getDateAfterSeconds(Integer.valueOf(environment.getProperty("session.expiry"))));
        freeOpers.add(adminAccount.getSessionId());
        Utils.setCookie(response, "sessionIdOper", adminAccount.getSessionId(), null, environment.getProperty("main.domain"), false, Integer.valueOf(environment.getProperty("session.expiry")));
        return HttpResponse.getSuccessResponse("Вы успешно зарегестрировались");
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public HttpResponse userRegister(HttpServletResponse response) throws Exception{// <<<--- вместо всей этой дряни надо прикрутить basic- авторизацию!!!
        log.info("start registration, user");
        UserAccount userAccount = new UserAccount(UUID.randomUUID().toString());
        userStorage.putValue(userAccount.getSessionId(), userAccount, Utils.getDateAfterSeconds(Integer.valueOf(environment.getProperty("session.expiry"))));
        Utils.setCookie(response, "sessionIdUser", userAccount.getSessionId(), null, environment.getProperty("main.domain"), false, Integer.valueOf(environment.getProperty("session.expiry")));
        return HttpResponse.getSuccessResponse("Вы успешно вошли в чат");
    }

    @RequestMapping(value = "/oper/call", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse callOper(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(freeOpers.size() <= 0){
            return HttpResponse.getErrorResponse("Нет свободных операторов попробуйте позже");
        }
        String operSession = freeOpers.remove(0);
        AdminAccount adminAccount = adminStorage.getValue(operSession);
        UserAccount userAccount = userStorage.getValue(Utils.getCookie(request, "sessionIdUser"));
        userAccount.setOperSocketId(adminAccount.getSocketId());
        userAccount.setOperSession(operSession);
        Utils.setCookie(response, "operId", operSession, null, environment.getProperty("main.domain"), false, Integer.valueOf(environment.getProperty("session.expiry")));
        WebSocketServer.sendMessage(userAccount.getOperSocketId(), new WebSocketRequest("Вы подключены к пользователю " + userAccount.getSessionId(), RequestType.CALL_OPER, userAccount.getSessionId()));
        return HttpResponse.getSuccessResponse("Здравствуйте, меня звать " + adminAccount.getLogin() + ", какой у вас вопрос?");
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public HttpResponse exceptionHandler(Exception e, HttpServletResponse response){
        log.log(Level.SEVERE, "HttpController :: exceptionHandler", e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return HttpResponse.getErrorResponse(e.getMessage());
    }

}
