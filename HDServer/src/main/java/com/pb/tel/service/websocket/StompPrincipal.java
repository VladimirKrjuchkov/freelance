package com.pb.tel.service.websocket;

import java.security.Principal;

/**
 * Created by vladimir on 08.07.19.
 */
class StompPrincipal implements Principal {
    String name;

    StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
