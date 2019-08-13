package com.pb.tel.service.handlers;

import com.pb.tel.service.auth.TokenStoreExtended;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.service.websocket.StompPrincipal;
import com.pb.tel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.logging.Logger;

/**
 * Created by vladimir on 12.08.19.
 */

@Service("wsHandler")
public class WSHandler {

    private static final Logger log = Logger.getLogger(WSHandler.class.getCanonicalName());

    @Autowired
    public TokenStoreExtended tokenStore;

    public OAuth2Authentication checkAuthExpire(Principal principal)throws LogicException {
        OAuth2AccessToken acessToken = tokenStore.getAccessToken((OAuth2Authentication) ((StompPrincipal)principal).getPrincipal());
        if(acessToken==null) {
            log.info("*** *** *** acessToken == null!!! *** *** ***");
            throw Utils.getLogicException("auth.AUTH08");
        }
        return tokenStore.readAuthentication(acessToken);
    }
}
