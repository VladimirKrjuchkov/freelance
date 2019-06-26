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
import com.pb.tel.service.websocket.data.WebSocketResponse;
import com.pb.tel.utils.DateTimeUtils;
import com.pb.tel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Timer;
import java.util.logging.Logger;


public class WebSocketServer {

	private static final Logger log = Logger.getLogger(WebSocketServer.class.getCanonicalName());    
    private static final Gson gson = new Gson();
    private static final long ONE_WEEK = DateTimeUtils.ONE_WEEK;
    private static final Timer pingTimer = new Timer(true);
    private static final long PING_TIMER = DateTimeUtils.SECONDS_30;
    private final boolean timerEnabled = Utils.property.getProperty("webSocket.timer","true").equals("true");
    public static Long historyControlTime;
    
    @Autowired
	private SimpMessagingTemplate template;
    
    private static SimpMessagingTemplate templateStatic;

    public WebSocketServer(SimpMessagingTemplate template) {
    	this.template = template;
    	templateStatic = template;
        historyControlTime = Long.valueOf(Utils.property.getProperty("history.control.time", "1477312984000"));
    }


    public static WebSocketResponse notification(WebSocketRequest request)throws LogicException {
        WebSocketResponse response = new WebSocketResponse();
        return response;
    }

 
//    public void onMessage(String message) {
//        try {
//            if (message != null) {
//                log.debug("wss message:{}", message);
//                MessageType mm =gson.fromJson(message, MessageType.class);                
//                notification(message);                
//            }
//        }catch (JsonSyntaxException jse){
//            log.warn("incorrect json request: "+message);
//        } catch (Exception ex) {
//            log.error("error on message process", ex);
//        }
//    }

//    @Override
//    public void onError(WebSocket conn, Exception ex) {
//        String message = ex.getMessage();
//        if(message!=null && !message.contains("Connection reset by peer")){
//            log.error("websocket error:" + conn, ex);
//        }
//    }

    public static void sendMessage(String message) {}


    public static void sendReset(String event, String userLogin, Long companyId){
        log.info("sendReset for login: "+userLogin);
        WebSocketResponse response = new WebSocketResponse();
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
    	templateStatic.convertAndSendToUser(userLogin, "/queue/answer/allWssResponse", message);
        return true;
    }

    public static void sendMassReset() {
//        List<String> allUsers = UserDAO.getInstance().list();
//        allUsers.forEach(u->{
//            UserDTO user = UserDTO.load(u);
//            if(user!=null){
//                sendReset("RESET",user.getLogin(),user.getCompanyId());
//                try {
//                    TimeUnit.MILLISECONDS.sleep(500);
//                } catch (InterruptedException e) {
//                	log.log(Level.WARNING, "interrupted exception : ", e);
//                }
//            }
//        });
    }

    public static void sendDeleteMessage(String userLogin) {
        WebSocketResponse response = new WebSocketResponse();
        sendMessage(userLogin, gson.toJson(response));
    }

    
    public static void logoutUser(String sessionId){
//        String sessionId = session.getSessionId();
//        String userLogin = session.getUser().getLogin();
//        List<WebSocket> sessionSockets = userSessionSockets
//                .computeIfAbsent(userLogin, k -> new HashMap<>())
//                .computeIfAbsent(sessionId, k -> new ArrayList<>());
//        Iterator<WebSocket> iterator = sessionSockets.iterator();
//        while(iterator.hasNext()){
//            WebSocket socket = iterator.next();
//            socket.close();
//            iterator.remove();
//        }
    }

//    private static class PingPongTimerTask extends TimerTask{
//
//        private SoftReference<WebSocket> socketReference;
//
//        private PingPongTimerTask(WebSocket socketReference){
//            this.socketReference = new SoftReference<>(socketReference);
//        }
//
//        @Override
//        public void run() {
//            WebSocket webSocket = socketReference.get();
//            try {
//                if (webSocket != null && webSocket.isOpen()) {
//                    WebSocketResponse response = new WebSocketResponse();
//                    response.status = "OK";
//                    webSocket.send(gson.toJson(response));
//                } else {
//                    this.cancel();
//                }
//            }catch (WebsocketNotConnectedException wnce){
//                if(webSocket!=null){
//                    try{webSocket.close();}catch (Exception ex){log.warn("pingpongtimer error:",ex);}
//                }
//            }catch (Exception ex){
//                log.warn("pingpongtimer error:",ex);
//            }
//        }
//    }
}