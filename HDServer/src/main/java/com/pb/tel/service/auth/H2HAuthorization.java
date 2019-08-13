package com.pb.tel.service.auth;

import com.pb.tel.data.Mes;
import com.pb.tel.data.UserAccount;
import com.pb.tel.service.Reference;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.utils.MessageUtil;
import com.pb.tel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by vladimir on 12.08.19.
 */
@Component("h2hAuthorization")
public class H2HAuthorization {

    private final static Logger log = Logger.getLogger(H2HAuthorization.class.getCanonicalName());


    @Resource(name="authorizationCodeServices")
    //@Lazy
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private ClientDetailsServiceInterface clientDetailsService;

    public UserAccount createAuthorizationCode(UserAccount userAccount){
        userAccount.setSessionId(UUID.randomUUID().toString());
        userAccount.setPassword(userAccount.getSessionId());
        userAccount.setAuthority(clientDetailsService.loadClientByClientId(userAccount.getClientId()).getAuthorities());
        UserDetails clientDetails = new User(userAccount.getSessionId(), userAccount.getPassword(), userAccount.getAuthority());
        Collection<GrantedAuthority> agentAuthority = (Collection<GrantedAuthority>) userAccount.getUser().getAuthorities();
        Authentication authentication = Reference.authorizeUser(clientDetails, userAccount, agentAuthority, null);
        try{
            String code = createAuthorizationCode(userAccount, authentication);
            userAccount.setMes(new Mes(code, null));
        }
        catch (LogicException e) {
            userAccount.setMes(Mes.createErrorMes(e.getCode(), e.getText()));
        }
        return userAccount;
    }

    private String createAuthorizationCode(UserAccount userAccount, Authentication authentication)throws LogicException {
        Map<String, String> params = requestParamsToMap(userAccount.getAutorizeUrl());
        String response_type = params.get("response_type");
        if(response_type==null || !"code".equals(response_type))
            throw Utils.getLogicException("auth.AUTH22", MessageUtil.getMessage("auth.AUTH19"));
        Set<String> responseTypes = new HashSet<>();
        responseTypes.add(response_type);
        OAuth2Request oAuthRequest = new OAuth2Request(params, userAccount.getClientId(), userAccount.getAuthority(), true, null, null, params.get("redirect_uri"), responseTypes, null);// params.get("redirect_uri")
        OAuth2Authentication combinedAuth = new OAuth2Authentication(oAuthRequest, authentication);
        return authorizationCodeServices.createAuthorizationCode(combinedAuth);
    }

    private Map<String, String> requestParamsToMap(String requestString){
        Map<String, String> result = new HashMap<>();
        String start = requestString.substring(requestString.indexOf("?")+1);
        String[] byParamValues = start.split("&");
        for(String paramValue : byParamValues){
            String[] paramValues = paramValue.split("=");
            result.put(paramValues[0], paramValues[1]);
        }
        return result;
    }
}

