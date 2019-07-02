package com.pb.tel.controller;

import com.pb.tel.service.exception.LogicException;
import com.pb.tel.service.websocket.data.WebSocketRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.logging.Logger;

/**
 * Created by vladimir on 01.07.19.
 */
@Controller
public class WSController {

    private static final Logger log = Logger.getLogger(WSController.class.getCanonicalName());

    @MessageMapping("/fromUser")
    @SendToUser("/queue/answer/sendResult")
    public void allWssReq(WebSocketRequest request) throws LogicException {
        log.info("*** *** *** INSIDE WSS *** *** ***");
        log.info("*** *** *** request.message = " + request.message);
//        WebSocketServer.sendMessage(adminLogin, request.message);
    }

//    @MessageExceptionHandler(LogicException.class)
//    @SendToUser("/queue/answer/errors")
//    public Mes handleLogicException(LogicException e) {
//        log.info("iN errorHanlr LogicException e = "+e);
//        log.log(Level.WARNING, "iN errorHanler", e);
//        SecurityContextHolder.clearContext();
//        Mes result = Mes.createErrorMes(e);
//        log.info("\n##############  Finish wss request  ##############"+MessageHandler.finishMarker);
//        return result;
//    }
//
//    @MessageExceptionHandler
//    //@SendToUser("/queue/errors")
//    public void handleException(Throwable e) {
//        log.info("iN errorHanlr Exception e = "+e);
//        log.log(Level.SEVERE, "", e);
//        SecurityContextHolder.clearContext();
//        log.info("\n##############  Finish wss request  ##############"+MessageHandler.finishMarker);
//        //return Mes.createErrorMes("UNKN01", MessageUtil.getMessage("unkn.UNKN01"));
//    }

}

