package com.pb.tel.controller;

import com.pb.tel.data.AdminAccount;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.service.websocket.data.WebSocketRequest;
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
        log.info("*** *** *** request: " + request);
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

