package com.pb.tel.service.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

/**
 * Created by vladimir on 08.07.19.
 */
public class CustomHandshakeHandler extends DefaultHandshakeHandler{
    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        return new StompPrincipal(UUID.randomUUID().toString(), request.getPrincipal());
    }
}
