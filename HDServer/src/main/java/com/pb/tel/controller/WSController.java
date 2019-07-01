package com.pb.tel.controller;

import com.pb.tel.service.exception.LogicException;
import com.pb.tel.service.websocket.WebSocketServer;
import com.pb.tel.service.websocket.data.WebSocketRequest;
import com.pb.tel.service.websocket.data.WebSocketResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.logging.Logger;

/**
 * Created by vladimir on 01.07.19.
 */
@Controller
public class WSController {

    private static final Logger log = Logger.getLogger(WSController.class.getCanonicalName());

    @MessageMapping("/allWssReq")
    @SendToUser("/queue/answer/allWssResponse")
    public WebSocketResponse allWssReq(WebSocketRequest request, Principal principal) throws LogicException {
        log.info("*** *** *** INSIDE WSS *** *** ***");
        log.info("*** *** *** request.message = " + request.message);
        WebSocketResponse response = WebSocketServer.notification(request);
        return response;
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

