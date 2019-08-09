package com.pb.tel.service.auth;

import com.pb.tel.data.Operator;
import com.pb.tel.data.UserAccount;
import com.pb.tel.service.Reference;
import com.pb.tel.storage.Storage;
import com.pb.tel.utils.MessageUtil;
import com.pb.tel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 26.07.19.
 */
public class SuccessHandlerImplementation extends SavedRequestAwareAuthenticationSuccessHandler {//extends SavedRequestAwareAuthenticationSuccessHandler   AuthenticationSuccessHandler {

    private final static Logger log = Logger.getLogger(SuccessHandlerImplementation.class.getCanonicalName());

    @Resource(name="tokenService")
    private AuthorizationServerTokenServices tokenServices;

    @Value("${use.JSESSIONID}")
    private boolean useJSESSIONID;

    @Autowired
    private Storage sessionStorage ;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String storageKey = Utils.exrtactCookieValue(request, "storageKey");
        UserAccount userAccount = (UserAccount)sessionStorage.removeValue(storageKey);
        Collection<GrantedAuthority> agentAuthority = (Collection<GrantedAuthority>) userAccount.getUser().getAuthorities();
        Authentication userAauthentication = Reference.authorizeUser((org.springframework.security.core.userdetails.UserDetails)authentication.getCredentials(), userAccount, Reference.getCombinetAuthorities(agentAuthority, userAccount.getAuthority()), authentication.getDetails());
        String autorizeUrl = userAccount.getAutorizeUrl();
        if(authentication.isAuthenticated()){
            autorizeUrl = MessageUtil.getProperty("main.address") + "/startwork?name=" + ((Operator)(userAccount.getUser())).getLogin();
            String userAccessToken = createToken(userAccount, userAauthentication, agentAuthority);
            Utils.setCookie(response, Reference.sidParametrName, Reference.buildSid(userAccessToken, userAccount.getClientId()), null, null, true, userAccount.getMaxInSecondPossibleSessionExpire()+10);

        }else{
            sessionStorage.putValue(storageKey, userAccount, userAccount.getMaxPossibleSessionExpire());
        }
        getRedirectStrategy().sendRedirect(request, response, autorizeUrl);
    }

    private String createToken(UserAccount userAccount, Authentication authentication, Collection<GrantedAuthority> agentAuthority){
        try{
            Map<String, String> params = new HashMap<String, String>();
            params.put("client_id", userAccount.getClientId());
            OAuth2Request request = new OAuth2Request(params, userAccount.getClientId(), agentAuthority, true, null, null, null, null, null);
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(request, authentication);
            oAuth2Authentication.setDetails(true);
            tokenServices.createAccessToken(oAuth2Authentication);
            return userAccount.getAccessToken();
        }
        catch (Exception e) {
            log.log(Level.SEVERE, "", e);
        }
        return null;
    }
}
