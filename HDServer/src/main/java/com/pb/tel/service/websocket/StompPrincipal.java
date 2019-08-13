package com.pb.tel.service.websocket;

import java.security.Principal;

/**
 * Created by vladimir on 08.07.19.
 */
public class StompPrincipal implements Principal {

    String name;

    Principal principal;

    public StompPrincipal(String name, Principal principal) {
        this.name = name;
        this.principal = principal;
    }

    @Override
    public String getName() {
        return name;
    }

    public Principal getPrincipal() {
        return principal;
    }
}
