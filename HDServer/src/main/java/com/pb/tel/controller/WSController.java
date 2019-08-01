package com.pb.tel.controller;

import com.pb.tel.data.ConnectedAccount;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.enumerators.RequestType;
import com.pb.tel.data.enumerators.Role;
import com.pb.tel.data.enumerators.Status;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.service.handlers.AdminHandler;
import com.pb.tel.service.handlers.UserHandler;
import com.pb.tel.service.websocket.WebSocketServer;
import com.pb.tel.service.websocket.data.WebSocketRequest;
import com.pb.tel.storage.Storage;
import com.pb.tel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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

    @Resource
    private AdminHandler adminHandler;

    @Resource
    private UserHandler userHandler;

    @Resource(name="registredOpers")
    private List<UserAccount> registredOpers;

    @Resource(name="dataStorage")
    private Storage<String, UserAccount> accountStorage;

    @MessageMapping("/user/enter")
    @SendToUser("/queue/answer/user/enter")
    public WebSocketRequest userEnter(SimpMessageHeaderAccessor sha, UserAccount userAccount) throws LogicException {
        String sessionId = sha.getUser().getName();
        log.info("start registration for user: " + sessionId);
        UserAccount account = accountStorage.getValue(sessionId);
        WebSocketRequest webSocketRequest = WebSocketRequest.getSuccessRequest();
        if(account == null){
            account = userAccount;
            account.setSessionId(sessionId);
            account.setStatus(Status.DISCONNECTED);
            account.setRole(Role.USER);

        }else if(account.getStatus() == Status.CONNECTED &&
                account.getConnectedAccounts() != null &&
                 !account.getConnectedAccounts().isEmpty() &&
                 accountStorage.getValue(account.getConnectedOperSessionId()) != null &&
                 accountStorage.getValue(account.getConnectedOperSessionId()).getStatus() == Status.ONLINE){
            webSocketRequest.setMessage("Вы подключены к оператору " + accountStorage.getValue(account.getConnectedOperSessionId()).getUser().getLogin());
        }
        accountStorage.putValue(account.getSessionId(), account, Utils.getDateAfterSeconds(Integer.valueOf(environment.getProperty("session.expiry"))));
        webSocketRequest.setUserAccount(account);
        webSocketRequest.setSessionExp(System.currentTimeMillis() + Integer.valueOf(environment.getProperty("session.expiry")));
        return webSocketRequest;
    }

    @MessageMapping("/user/check")
    @SendToUser("/queue/answer/user/check")
    public WebSocketRequest userCheck(SimpMessageHeaderAccessor sha) throws LogicException {
        String sessionId = sha.getUser().getName();
        UserAccount userAccount = accountStorage.getValue(sessionId);
        accountStorage.putValue(userAccount.getSessionId(), userAccount, Utils.getDateAfterSeconds(Integer.valueOf(environment.getProperty("session.expiry"))));
        WebSocketRequest webSocketRequest = WebSocketRequest.getSuccessRequest(userAccount);
        webSocketRequest.setSessionExp(System.currentTimeMillis() + Integer.valueOf(environment.getProperty("session.expiry")));
        return WebSocketRequest.getSuccessRequest(userAccount);
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
        operAccount.addConnectedAccount(new ConnectedAccount(sessionId, userAccount.getUser().getLogin()));
        userAccount.addConnectedAccount(new ConnectedAccount(operAccount.getSessionId(), operAccount.getUser().getLogin()));
        userAccount.setStatus(Status.CONNECTED);
        WebSocketRequest webSocketRequest = WebSocketRequest.getSuccessRequest("Вы подключены к пользователю " + userAccount.getSessionId(), operAccount);
        webSocketRequest.setRequestType(RequestType.CALL_OPER);
        WebSocketServer.sendMessage(userAccount.getConnectedOperSessionId(), webSocketRequest);
        return WebSocketRequest.getSuccessRequest("Здравствуйте, меня звать " + operAccount.getUser().getLogin() + ", какой у вас вопрос?", operAccount);
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

    @MessageMapping("/oper/enter")//<<<--- перед этим всем надо реализовать проверку логина/пароля при помощи OAuth2
    @SendToUser("/queue/answer/oper/enter")
    public WebSocketRequest operEnter(SimpMessageHeaderAccessor sha, UserAccount operAccount) throws LogicException {
        String sessionId = sha.getUser().getName();
        log.info("start registration for oper: " + sessionId);
        UserAccount account = (UserAccount) accountStorage.getValue(sessionId);
        WebSocketRequest webSocketRequest = WebSocketRequest.getSuccessRequest();
        if(account == null){
            account = operAccount;
            account.setSessionId(sessionId);
            account.setStatus(Status.ONLINE);
        }
        registredOpers.add((UserAccount) account);
        accountStorage.putValue(account.getSessionId(), account, Utils.getDateAfterSeconds(Integer.valueOf(environment.getProperty("session.expiry"))));
        webSocketRequest.setUserAccount(account);
        webSocketRequest.setSessionExp(System.currentTimeMillis() + Integer.valueOf(environment.getProperty("session.expiry")));
        return webSocketRequest;
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

