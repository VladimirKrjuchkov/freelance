package com.pb.tel.service.auth;

import com.pb.tel.utils.Utils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * Created by vladimir on 29.07.19.
 */
public class TokenClientIdExtractor implements TokenExtractor {

    private final Logger log = Logger.getLogger(TokenClientIdExtractor.class.getCanonicalName());
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_ID_TYPE = "Id";

    private String authParamName = "Authorization";

    public TokenClientIdExtractor(){}

    public TokenClientIdExtractor(String authParamName){
        log.info("authParamName: "+authParamName);
        this.authParamName = authParamName;
    }


    @Override
    public Authentication extract(HttpServletRequest request) {
        String authValues[] = extractTokenClienId(request);
        String tokenValue = authValues[0];
        String client_id = authValues[1];

        if (tokenValue != null && client_id != null) {
            PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(tokenValue, client_id);
            return authentication;
        }
        return null;
    }

    public String[] extractTokenClienId(HttpServletRequest request) {
        String authHeaderValues[] = extractHeaderTokenClienId(request);
        String token = authHeaderValues[0];
        String client_id = authHeaderValues[1];
        String authValues[] = {token, client_id};
        return authValues;
    }

    /**
     * Extract the OAuth bearer token from a header.
     *
     * @param request The request.
     * @return The token, or null if no OAuth authorization header was supplied.
     */
    protected String[] extractHeaderTokenClienId(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(authParamName);
        String token = null;
        String clientId = null;

        if(headers!=null && headers.hasMoreElements()){
            while (headers.hasMoreElements()) { // typically there is only one (most servers enforce that)
                if(token!=null || clientId!=null)
                    break;
                String value = headers.nextElement();
                log.fine("header value for "+authParamName+" = "+value);
                String valueLowerCase = value.toLowerCase();
                int tokenTypePosition = valueLowerCase.indexOf(OAuth2AccessToken.BEARER_TYPE.toLowerCase());
                int clientIdTypePosition = valueLowerCase.indexOf(CLIENT_ID_TYPE.toLowerCase());
                token = parseAuthHeaderValue(tokenTypePosition, OAuth2AccessToken.BEARER_TYPE, value);
                clientId = parseAuthHeaderValue(clientIdTypePosition, CLIENT_ID_TYPE, value);
            }
        }
        else{
            log.info("Parse cookie for "+authParamName);
            Cookie[] cookies = request.getCookies();
            if(!Utils.isEmpty(cookies))
                for(Cookie cookie : cookies){
                    if(authParamName.equals(cookie.getName())){
                        String value = cookie.getValue();
                        String valueLowerCase = value.toLowerCase();
                        int tokenTypePosition = valueLowerCase.indexOf(OAuth2AccessToken.BEARER_TYPE.toLowerCase());
                        int clientIdTypePosition = valueLowerCase.indexOf(CLIENT_ID_TYPE.toLowerCase());
                        token = parseAuthHeaderValue(tokenTypePosition, OAuth2AccessToken.BEARER_TYPE, value);
                        clientId = parseAuthHeaderValue(clientIdTypePosition, CLIENT_ID_TYPE, value);
                        if(token!=null && clientId!=null)
                            break;
                    }
                }
        }
        String authHeaderValues[] = {token, clientId};
        return authHeaderValues;
    }

    private String parseAuthHeaderValue(int authParamTypePosition, String authParamType, String headerValue){
        String authParam = null;
        if(authParamTypePosition!=-1){
            authParam = headerValue.substring(authParamTypePosition+authParamType.length()).trim();
            int commaIndex = authParam.indexOf(',');
            if (commaIndex > 0) {
                authParam = authParam.substring(0, commaIndex).trim();
            }
        }
        return authParam;
    }

}


