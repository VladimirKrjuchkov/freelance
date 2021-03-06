/*
 * Copyright (c) 2017. iDoc LLC
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     (1) Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *     (2) Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 *     (3)The name of the author may not be used to
 *     endorse or promote products derived from this software without
 *     specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.pb.tel.service.websocket;

import com.google.gson.Gson;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.service.websocket.data.WebSocketRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.logging.Logger;


public class WebSocketServer {

	private static final Logger log = Logger.getLogger(WebSocketServer.class.getCanonicalName());    
    private static final Gson gson = new Gson();

    @Autowired
	private SimpMessagingTemplate template;

    private static SimpMessagingTemplate templateStatic;

    public WebSocketServer(SimpMessagingTemplate template) {
    	this.template = template;
    	templateStatic = template;
    }


    public static WebSocketRequest notification(WebSocketRequest request)throws LogicException {
        WebSocketRequest response = new WebSocketRequest();
        return response;
    }


    public static void sendReset(String event, String userLogin, Long companyId){
        log.info("sendReset for login: "+userLogin);
        WebSocketRequest response = new WebSocketRequest();
//        response.method = WebSocketResponse.Method.RESET;
//        response.timestamp = new Date().getTime();
//        response.data = ResourceDAO.getInstance().search(null, userLogin, null, companyId);
//        response.userLogin = userLogin;
//        response.eventType = event;
        sendMessage(userLogin, gson.toJson(response));
    }

    public static boolean sendMessage(String userLogin, String message) {
    	log.info("SEND WEBSOCKET MESSAGE  !");
    	log.info("Login: "+userLogin);
    	log.info("message: "+message);    	
    	templateStatic.convertAndSendToUser(userLogin, "/queue/answer/input/requests", message);
        return true;
    }

    public static boolean sendMessage(String userLogin, WebSocketRequest webSocketRequest) {
        log.info("SEND WEBSOCKET MESSAGE  !");
        log.info("Login: "+userLogin);
        log.info("webSocketRequest: "+webSocketRequest);
        templateStatic.convertAndSendToUser(userLogin, "/queue/answer/input/requests", webSocketRequest);
        return true;
    }

}