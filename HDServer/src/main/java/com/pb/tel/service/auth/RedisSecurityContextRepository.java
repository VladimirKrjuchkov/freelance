package com.pb.tel.service.auth;

import com.pb.tel.data.UserAccount;
import com.pb.tel.storage.Storage;
import com.pb.tel.utils.MessageUtil;
import com.pb.tel.utils.Utils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by vladimir on 26.07.19.
 */
public class RedisSecurityContextRepository implements SecurityContextRepository {

    private static final Logger log = Logger.getLogger(RedisSecurityContextRepository.class.getCanonicalName());

    public static final String HD_SECURITY_CONTEXT_KEY = "PPLSID";
    private String hdSecurityContextKey = HD_SECURITY_CONTEXT_KEY;

    @Resource(name="dataStorage")
    private Storage dataStorage;

    private boolean useJSESSIONID = Boolean.valueOf(MessageUtil.getProperty("useJSESSIONID"));

    {
        if(useJSESSIONID)
            hdSecurityContextKey = "JSESSIONID";
    }

    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder){
        HttpServletRequest request = requestResponseHolder.getRequest();

        log.fine("loadContext in RedisSecurityContextRepository");

        SecurityContext context = readSecurityContextFromRequest(request);

        if (context == null){
            log.fine("No SecurityContext was available from the Redis. A new one will be created.");
            context = generateNewContext();
        }
        return context;

    }

    private SecurityContext readSecurityContextFromRequest(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        SecurityContext context = null;
        if(cookies!=null)
            for(Cookie cookie : cookies){
                if(hdSecurityContextKey.equals(cookie.getName())){
                    log.fine("Search SecurityContext for key : "+cookie.getValue());
                    Object userAuthentication = dataStorage.getValue(cookie.getValue());
                    if(userAuthentication!=null){
                        context = new SecurityContextImpl();
                        context.setAuthentication((PreAuthenticatedAuthenticationToken)userAuthentication);
                        log.fine("Has loaded SecurityContext from redis : "+context);
                        break;
                    }
                }
            }
        return context;
    }

    protected SecurityContext generateNewContext() {
        return SecurityContextHolder.createEmptyContext();
    }


    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response){
        log.fine("saveContext in RedisSecurityContextRepository");
        Authentication authentication = context.getAuthentication();
        if(authentication != null)
            log.info("authentication: "+authentication.getName()+ "    getClass: "+authentication.getClass().getName());
        if(authentication != null && authentication.getCredentials()!=null){
            log.info("authentication.getCredentials(): "+authentication.getCredentials());
            log.info("authentication.getCredentials() instanceof UserAccount: "+(authentication.getCredentials() instanceof UserAccount));
            log.info("authentication.getCredentials().getClass().getName(): "+authentication.getCredentials().getClass().getName());
        }
        if(authentication == null || authentication.getCredentials()==null ||
                !(authentication instanceof PreAuthenticatedAuthenticationToken) || !(authentication.getCredentials() instanceof UserAccount)){
            log.fine("SecurityContext is empty or contents are anonymous - context will not be stored in RedisStorage. context :"+context);
            return;
        }

        String cookieValue = null;
        log.fine("useJSESSIONID : "+useJSESSIONID);
        HttpSession httpSession = request.getSession(false);
        if(useJSESSIONID){
            log.fine("httpSession : "+httpSession);
            if(httpSession!=null)
                cookieValue = httpSession.getId();
            log.fine("cookieValue1 : "+cookieValue);
        }
        else
            cookieValue = (String)request.getAttribute("sessionCookieValue");
        log.fine("cookieValue res : "+cookieValue);
        if(cookieValue==null)
            return;

        UserAccount userAccount = (UserAccount)authentication.getCredentials();
        Date expire = Utils.getDateAfterSeconds(15);
        if(userAccount.isRegistrationUser())
            expire = userAccount.getMaxPossibleSessionExpire();
        log.fine("SecurityContext before stored to RedisStorage.  cookieValue: "+cookieValue+"  context: '" + context + "'");
        dataStorage.putValue(cookieValue, authentication, expire);
        log.fine("SecurityContext stored to RedisStorage: '" + context + "'");
    }


    public boolean containsContext(HttpServletRequest request){
        log.fine("containsContext in RedisSecurityContextRepository");
        Cookie [] cookies = request.getCookies();
        if(cookies!=null)
            for(Cookie cookie : cookies){
                if(hdSecurityContextKey.equals(cookie.getName()))
                    return dataStorage.contains(cookie.getValue());
            }
        return false;
    }
}
