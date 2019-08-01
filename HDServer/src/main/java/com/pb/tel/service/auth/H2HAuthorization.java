package com.pb.tel.service.auth;

import com.pb.tel.data.Mes;
import com.pb.tel.data.UserAccount;
import com.pb.tel.service.Reference;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.utils.MessageUtil;
import com.pb.tel.utils.Utils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by vladimir on 26.07.19.
 */
@Component("h2hAuthorization")
public class H2HAuthorization {

    private final static Logger log = Logger.getLogger(H2HAuthorization.class.getCanonicalName());


    @Resource(name="authorizationCodeServices")
    private AuthorizationCodeServices authorizationCodeServices;

    public UserAccount createAuthorizationCode(UserAccount userAccount, ClientDetails clientDetails){
        log.info("techUser: "+clientDetails.getTechUser().getLogin()+"   authorities: "+clientDetails.getTechUser().getAuthorities());
        userAccount.fillAgentUserAccaunt(clientDetails);
        Authentication authentication = Reference.authorizeUser(clientDetails.getTechUser(), userAccount, Reference.getCombinetAuthorities(clientDetails.getAuthorities(), userAccount.getAuthority()), null);
        try{
            String code = createAuthorizationCode(userAccount, authentication);
            userAccount.setMes(new Mes(code, null));
        }
        catch (LogicException e) {
            userAccount.setMes(Mes.createErrorMes(e.getCode(), e.getText()));
        }
        return userAccount;
    }

    private String createAuthorizationCode(UserAccount userAccount, Authentication authentication)throws LogicException{
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

