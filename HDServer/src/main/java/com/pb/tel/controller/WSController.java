package com.pb.tel.controller;

import com.pb.tel.data.AdminAccount;
import com.pb.tel.data.enumerators.RequestType;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.service.websocket.data.WebSocketRequest;
import com.pb.tel.service.websocket.data.WebSocketResponse;
import com.pb.tel.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 01.07.19.
 */
@Controller
public class WSController {

    private static final Logger log = Logger.getLogger(WSController.class.getCanonicalName());

    @Autowired
    private Environment environment;

    @Resource(name="adminStorage")
    private Storage<String, AdminAccount> adminStorage;

    @Resource(name="freeOpers")
    private List<String> freeOpers;

    @MessageMapping("/fromUser")
    @SendToUser("/queue/answer/sendResult")
    public void allWssReq(WebSocketRequest request) throws LogicException {
//        WebSocketServer.sendMessage(adminLogin, request.message);
    }

    @MessageMapping("/callOper")
    @SendToUser("/queue/answer/sendResult")
    public WebSocketResponse callOper(WebSocketRequest request) throws Exception {

        log.info("*** *** *** INSIDE *** *** ***");
        if(freeOpers.size() <= 0){
            return WebSocketResponse.getErrorResponse("Нет свободных операторов попробуйте позже", RequestType.CALL_OPER);
        }
        String operSession = freeOpers.remove(0);
        AdminAccount adminAccount = adminStorage.getValue(operSession);
//        Utils.setCookie(response, "operId", operSession, null, environment.getProperty("main.domain"), true, Integer.valueOf(environment.getProperty("session.expiry")));
        log.info("*** *** *** BEFORE SEND ANSWER *** *** ***");
        return WebSocketResponse.getErrorResponse("Здравствуйте, меня звать " + adminAccount.getLogin() + ", какой у вас вопрос?", RequestType.CALL_OPER);

//        WebSocketServer.sendMessage(adminLogin, request.message);
    }

    @MessageExceptionHandler(LogicException.class)
    @SendToUser("/queue/answer/errors")
    public void handleLogicException(LogicException e) {
        log.log(Level.SEVERE, "ERROR(LogicException):", e);
    }

    @MessageExceptionHandler
    //@SendToUser("/queue/errors")
    public void handleException(Throwable e) {
        log.log(Level.SEVERE, "ERROR(Throwable):", e);
    }

}

