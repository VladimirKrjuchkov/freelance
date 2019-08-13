package com.pb.tel.controller;

import com.pb.tel.data.ConnectedAccount;
import com.pb.tel.data.Operator;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.enumerators.RequestType;
import com.pb.tel.data.enumerators.Status;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.service.handlers.AdminHandler;
import com.pb.tel.service.handlers.WSHandler;
import com.pb.tel.service.websocket.WebSocketServer;
import com.pb.tel.service.websocket.data.WebSocketRequest;
import com.pb.tel.storage.Storage;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 01.07.19.
 */
@Controller
public class WSController {

    private static final Logger log = Logger.getLogger(WSController.class.getCanonicalName());

    @Resource
    private AdminHandler adminHandler;

    @Resource
    private WSHandler wsHandler;

    @Resource(name="dataStorage")
    private Storage<String, UserAccount> accountStorage;


    @MessageMapping("/oper/sayHellow")
    @SendToUser("/queue/answer/user/message")
    public void sayHellow(Principal principal) throws LogicException {
        log.info("*** *** *** Hellow Im oper *** *** ***");
        log.info("*** *** *** principal: " + principal);
        Authentication freshAuthentication = wsHandler.checkAuthExpire(principal);
        log.info("*** *** *** freshAuthentication: " + freshAuthentication);
    }

    @MessageMapping("/oper/call")
    @SendToUser("/queue/answer/oper/call")
    public WebSocketRequest operCall(SimpMessageHeaderAccessor sha) throws LogicException {
        String sessionId = sha.getUser().getName();
        log.info("start call oper for user: " + sessionId);
        UserAccount userAccount = accountStorage.getValue(sessionId);
        if(userAccount.getStatus() == Status.CONNECTED &&
                userAccount.getConnectedAccounts() != null &&
                accountStorage.getValue(userAccount.getConnectedOperSessionId()) != null &&
                accountStorage.getValue(userAccount.getConnectedOperSessionId()).getStatus() == Status.ONLINE){
            return WebSocketRequest.getErrorRequest("Вы уже подключены к оператору");
        }
        UserAccount operAccount = adminHandler.getMostFreeOper();
        if(operAccount == null){
            return WebSocketRequest.getErrorRequest("Нет свободных операторов попробуйте позже");
        }
        operAccount.addConnectedAccount(new ConnectedAccount(sessionId, ((Operator)userAccount.getUser()).getLogin()));
        userAccount.addConnectedAccount(new ConnectedAccount(operAccount.getSessionId(), ((Operator)operAccount.getUser()).getLogin()));
        userAccount.setStatus(Status.CONNECTED);
        WebSocketRequest webSocketRequest = WebSocketRequest.getSuccessRequest("Вы подключены к пользователю " + userAccount.getSessionId(), operAccount);
        webSocketRequest.setRequestType(RequestType.CALL_OPER);
        WebSocketServer.sendMessage(userAccount.getConnectedOperSessionId(), webSocketRequest);
        return WebSocketRequest.getSuccessRequest("Здравствуйте, меня звать " + ((Operator)operAccount.getUser()).getLogin() + ", какой у вас вопрос?", operAccount);
    }

    @MessageMapping("/user/message")
    @SendToUser("/queue/answer/user/message")
    public WebSocketRequest fromUser(SimpMessageHeaderAccessor sha, WebSocketRequest request) throws LogicException {
        log.info("from user request: " + request);
        String sessionId = sha.getUser().getName();
        UserAccount userAccount = accountStorage.getValue(sessionId);
        request.setUserAccount(userAccount);
        WebSocketServer.sendMessage(userAccount.getConnectedOperSessionId(), request);
        return WebSocketRequest.getSuccessRequest(request.getMessage(), userAccount);
    }

    @MessageMapping("/oper/message")
    @SendToUser("/queue/answer/oper/message")
    public WebSocketRequest fromOper(SimpMessageHeaderAccessor sha, WebSocketRequest request) throws LogicException {
        log.info("from user request: " + request);
        String sessionId = sha.getUser().getName();
        UserAccount operAccount = accountStorage.getValue(sessionId);
        if(operAccount.getConnectedAccounts().get(request.getUserSessionId()) == null){
            return WebSocketRequest.getErrorRequest("Вы не можете отправлять сообщения этому пользователю");
        }
        request.setUserAccount(operAccount);
        WebSocketServer.sendMessage(request.getUserSessionId(), request);
        WebSocketRequest response = WebSocketRequest.getSuccessRequest(request.getMessage(), operAccount);
        response.setUserSessionId(request.getUserSessionId());
        return response;
    }


    @MessageExceptionHandler(LogicException.class)
    @SendToUser("/queue/answer/errors")
    public void handleLogicException(LogicException e) {
        log.log(Level.SEVERE, "ERROR(LogicException):", e);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/answer/errors")
    public void handleException(Throwable e) {
        log.log(Level.SEVERE, "ERROR(Throwable):", e);
    }

}

