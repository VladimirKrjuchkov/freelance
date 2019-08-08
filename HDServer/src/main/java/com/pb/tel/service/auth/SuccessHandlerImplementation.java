package com.pb.tel.service.auth;

import com.pb.tel.data.Operator;
import com.pb.tel.data.UserAccount;
import com.pb.tel.service.Reference;
import com.pb.tel.service.handlers.UserHandler;
import com.pb.tel.storage.Storage;
import com.pb.tel.utils.MessageUtil;
import com.pb.tel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private ClientDetailsServiceInterface clientDetailsService;

    @Autowired
    private Storage sessionStorage ;

//    @Autowired
//    private LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint;

//	@Autowired
//	private Storage<String, Object> dataStorage;

    @Autowired
    public UserHandler userHandler;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        ClientDetails mainAgent = null;
//        UserAccount userAccount;
//        String workStationUserId = Utils.exrtactCookieValue(request, "exac");
//        log.info("workStationUserId: "+workStationUserId);
//        if(Utils.isEmpty(workStationUserId)){//Это может быть если он не вошел более чем за getMaxInSecondPossibleSessionExpire секунд. Тогда мы просто запускаем его к нам на страничку
//            mainAgent = clientDetailsService.getAdminClientDetails();
//            userAccount = loginUrlAuthenticationEntryPoint.prepareEntrance(request, response, mainAgent, false);
//        }
//        else{
//            userAccount = (UserAccount)sessionStorage.removeValue(workStationUserId);
//            if(userAccount==null){
//                //Это может быть если он не вошел более чем за getMaxInSecondPossibleSessionExpire секунд. Такого быть вообще не должно т.к. кука тоже уже должна умереть.
//                //Если таки так случилось - мы просто запускаем его к нам на страничку
//                log.info("storage haven't userAccount  !!!");
//                mainAgent = clientDetailsService.getAdminClientDetails();
//                userAccount = loginUrlAuthenticationEntryPoint.prepareEntrance(request, response, mainAgent, false);
////    			getRedirectStrategy().sendRedirect(request, response, MessageUtil.getProperty("entranceLink"));
////    			return;
//            }
//        }
//        userAccount.setUsername((String)authentication.getPrincipal());
//        userAccount.setPassword("userPassword");//(String)authentication.getCredentials()
//        userAccount.setAuthority(authentication.getAuthorities());
//        log.info("authentication.isAuthenticated(): "+authentication.isAuthenticated());
//        if(!authentication.isAuthenticated())
//            userAccount.setRegistrationUser(true);
//        UserAccount userAccount = new UserAccount();
//        String storageKey = UUID.randomUUID().toString();
//        userAccount.setStorageKey(storageKey);
//        String workStationUserId = UUID.randomUUID().toString();
//        log.info("workStationUserId: "+workStationUserId);
//        userAccount.setWorkStationUserId(workStationUserId);
//        Utils.setCookie(response, "exac", workStationUserId, null/*"/PplsService/"*/, null/*MessageUtil.getProperty("mainDomain")*/, true, userAccount.getMaxInSecondPossibleSessionExpire()+60);
//        Utils.setCookie(response, "sidCheck", userAccount.getSidBi(), null, null, false, userAccount.getMaxInSecondPossibleSessionExpire()+60);
//        sessionStorage.putValue(storageKey, userAccount, userAccount.getMaxPossibleSessionExpire());
        String storageKey = Utils.exrtactCookieValue(request, "storageKey");
        UserAccount userAccount = (UserAccount)sessionStorage.getValue(storageKey);
//        log.info("*** *** *** 1 :" + ((com.pb.tel.service.auth.ClientDetailsService)clientDetailsService).getStoredClients().size());
//        ClientDetails clientDetails = ((com.pb.tel.service.auth.ClientDetailsService)clientDetailsService).loadClientByClientId(((User)(userAccount.getUser())).getLogin());
//        Collection<GrantedAuthority> agentAuthority = clientDetails.getAuthorities();
        Collection<GrantedAuthority> agentAuthority = (Collection<GrantedAuthority>) userAccount.getUser().getAuthorities();
        Authentication userAauthentication = Reference.authorizeUser((org.springframework.security.core.userdetails.UserDetails)authentication.getCredentials(), userAccount, Reference.getCombinetAuthorities(agentAuthority, userAccount.getAuthority()), authentication.getDetails());
        String autorizeUrl = userAccount.getAutorizeUrl();
        if(authentication.isAuthenticated()){
            autorizeUrl = MessageUtil.getProperty("main.address") + "/startwork?name=" + ((Operator)(userAccount.getUser())).getLogin();
            String userAccessToken = createToken(userAccount, userAauthentication, agentAuthority);
            Utils.setCookie(response, Reference.sidParametrName, Reference.buildSid(userAccessToken, userAccount.getClientId()), null, null, true, userAccount.getMaxInSecondPossibleSessionExpire()+10);
        }else{
            userAccount.setAutorizeUrl(autorizeUrl);
            if(!authentication.isAuthenticated())
                sessionStorage.putValue(storageKey, userAccount, userAccount.getMaxPossibleSessionExpire());
        }
        getRedirectStrategy().sendRedirect(request, response, autorizeUrl);
//        entryRegisterProcess(null, request, response, authentication, userAccount, storageKey, false);
    }


//    public void entryRegisterProcess(ClientDetails mainAgent, HttpServletRequest request, HttpServletResponse response, Authentication authentication, UserAccount userAccount, String workStationUserId, boolean finalRegister )throws IOException, ServletException  {
//        ClientDetails clientDetails = ((ClientDetailsService)clientDetailsService).loadClientByClientId(userAccount.getClientId());
//        Collection<GrantedAuthority> agentAuthority = clientDetails.getAuthorities();
//        mainAgent = clientDetails;
//        Authentication userAauthentication = Reference.authorizeUser((org.springframework.security.core.userdetails.UserDetails)authentication.getCredentials(), userAccount, Reference.getCombinetAuthorities(agentAuthority, userAccount.getAuthority()), authentication.getDetails());
//        String autorizeUrl = userAccount.getAutorizeUrl();
//
//        log.info("mainAgent: "+mainAgent);
//        log.info("userAccount: "+userAccount);
//
//        if(!useJSESSIONID){
//            String cookieValue = UUID.randomUUID().toString();
//            Utils.setCookie(response, RedisSecurityContextRepository.HD_SECURITY_CONTEXT_KEY, cookieValue, null, null, true, 5);
//            request.setAttribute("sessionCookieValue", cookieValue);
//        }
//
//        if(authentication.isAuthenticated()){
//            autorizeUrl = MessageUtil.getProperty("entranceLink") + "/startwork";
//            String userAccessToken = createToken(userAccount, userAauthentication, agentAuthority);
//            Utils.setCookie(response, Reference.sidParametrName, Reference.buildSid(userAccessToken, userAccount.getClientId()), null, null, true, userAccount.getMaxInSecondPossibleSessionExpire()+10);
//            if(mainAgent==null) { //Значит это авторизация для агентов паперлесс(а-ля Полтава Энерго)
//                createAndSetToContextAuthenticationForAgent(clientDetails, userAccount, userAauthentication, request);
//                autorizeUrl = MessageUtil.getProperty("entranceLink") + "/oauth/agent?redirect_uri="+userAccount.getAutorizeUrl();
//            }
//            else
//            if(!finalRegister)
//                SecurityContextHolder.clearContext();
//        }
//        else
//        if(mainAgent!=null) {
//            HttpSession httpSession = request.getSession(false);
//            if(httpSession==null)
//                httpSession = request.getSession(true);
//            autorizeUrl = MessageUtil.getProperty("entranceLink") + "/PplsService/oauth/authorize?response_type=code&client_id="+mainAgent.getClientId()+"&redirect_uri="+MessageUtil.getProperty("entranceLink") + "/startwork";
//        }
//
//        userAccount.setAutorizeUrl(autorizeUrl);
//        if(!authentication.isAuthenticated())
//            sessionStorage.putValue(workStationUserId, userAccount, userAccount.getMaxPossibleSessionExpire());
//
//        getRedirectStrategy().sendRedirect(request, response, autorizeUrl);
//
//    }


    private void createAndSetToContextAuthenticationForAgent(ClientDetails clientDetails, UserAccount userAccount, Authentication authentication, HttpServletRequest request){
//        org.springframework.security.core.userdetails.UserDetails agentUserDetails = clientDetails.getTechUser();//userHandler.loadUserByEmail(clientDetails.getTechUser().getLogin());
//        UsernamePasswordAuthenticationToken agentAuthentication = new UsernamePasswordAuthenticationToken(agentUserDetails.getUsername(), agentUserDetails, agentUserDetails.getAuthorities());
        UsernamePasswordAuthenticationToken agentAuthentication = new UsernamePasswordAuthenticationToken((((Operator)(userAccount.getUser())).getUsername()), (Operator)(userAccount.getUser()), (((Operator)(userAccount.getUser())).getAuthorities()));
        agentAuthentication.setDetails(authentication.getDetails());

//        UserAccount agentUserAccount = loginUrlAuthenticationEntryPoint.generateUserAccount(request, clientDetails);
//        agentUserAccount.setWorkStationUserId(userAccount.getWorkStationUserId());
//        agentUserAccount.fillAgentUserAccaunt(clientDetails);
//        agentUserAccount.setUser(UserDAO.getInstance().getUser(agentUserAccount.getUsername()));
//        agentUserAccount.setCustomerUserAccount(userAccount);

        Reference.authorizeUser((org.springframework.security.core.userdetails.UserDetails)agentAuthentication.getCredentials(), userAccount, Reference.getCombinetAuthorities(clientDetails.getAuthorities(), userAccount.getAuthority()), authentication.getDetails());
    }

    private String createToken(UserAccount userAccount, Authentication authentication, Collection<GrantedAuthority> agentAuthority){
        try{
            Map<String, String> params = new HashMap<String, String>();
            //params.put("client_id", Util.getParametrFromUrl("client_id", userAccount.getAutorizeUrl()));
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
