package com.pb.tel.service.auth;

import com.pb.tel.data.Mes;
import com.pb.tel.data.Roles;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.enumerators.AuthType;
import com.pb.tel.service.Reference;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.service.handlers.AccountHandler;
import com.pb.tel.utils.MessageUtil;
import com.pb.tel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.RedirectUrlBuilder;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 12.08.19.
 */
public class LoginUrlAuthenticationEntryPoint extends org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint{

    private static final Logger log = Logger.getLogger(LoginUrlAuthenticationEntryPoint.class.getCanonicalName());

    private static final String CLIENT_ID = "client_id";
    private static final String REDIRECT_URL = "redirect_uri";
    private static final String AGENT_CHECK = "agentCheck";

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Resource
    private AccountHandler accountHandler;

    @Autowired
    private ClientDetailsServiceInterface clientDetailsService;

    @Resource(name="h2hAuthorization")
    private H2HAuthorization h2hAuthorization;

    @Autowired
    private TokenStoreExtended tokenStore;

    private TokenClientIdExtractor tokenClientIdExtractor;

    public LoginUrlAuthenticationEntryPoint(String loginFormUrl, String sidParametrName) {
        super(loginFormUrl);
        tokenClientIdExtractor = new TokenClientIdExtractor(sidParametrName);
    }


    protected Object[] determineUrl(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException){
        boolean h2HLogin = false;
        ClientDetails clientDetails = null;
        String superUrlString = super.determineUrlToUseForThisRequest(request, response, authException);
        log.info("superUrlString: "+superUrlString);
        String clientId = request.getParameter(CLIENT_ID);
        UserAccount userAccount;
        try{
            if (!StringUtils.hasText(clientId) )
                throw Utils.getLogicException("auth.AUTH07", CLIENT_ID);

            Authentication auth = tokenClientIdExtractor.extract(request);
            if(auth!=null) {// Если пользователь уже авторизован и поступил запрос на авторизацию под другим агентом, то убиваем предидущую авторизацию и заходим по новым агентом
                if(clientId.equals(auth.getCredentials()))
                    return new Object[]{superUrlString, null, null};
                else {
                    tokenStore.removeAccessToken((String)auth.getPrincipal());
                    Utils.setCookie(response, Reference.sidParametrName, "", null, null, true, 0);
                }
            }

            clientDetails = (ClientDetails)clientDetailsService.loadClientByClientId(clientId);

            if(Boolean.valueOf(request.getParameter(AGENT_CHECK)) && Roles.convertAuthorityToRoles(clientDetails.getAuthorities()).contains(Roles.ROLE_H2H))
                h2HLogin = true;

            log.info("h2HLogin: "+h2HLogin);
            if(!h2HLogin && (request.getParameterValues(REDIRECT_URL) == null || request.getParameterValues(REDIRECT_URL).length != 1))
                throw Utils.getLogicException("auth.AUTH07", REDIRECT_URL);

            log.info("clientDetail:"+clientDetails);
            userAccount = accountHandler.generateUserAccount();
            return new Object[]{superUrlString, userAccount, clientDetails};
        }
        catch (LogicException e) {
            log.log(Level.WARNING, "", e);
            superUrlString = MessageUtil.getProperty("entrancelink"+"/error.html");
            userAccount = exceptionHandler(request, e.getCode(), e.getText());
        }
        catch (Exception e) {
            log.log(Level.SEVERE, "", e);
            superUrlString = MessageUtil.getProperty("entrancelink"+"/error.html");
            userAccount = exceptionHandler(request, "auth.AUTH22", MessageUtil.getMessage("auth.AUTH22"));
        }
        return new Object[]{superUrlString, userAccount, clientDetails};
    }

    private UserAccount exceptionHandler(HttpServletRequest request, String code, String desc){
        UserAccount userAccount = new UserAccount();
        if(Boolean.valueOf(request.getParameter(AGENT_CHECK)))
            userAccount.setAuthType(AuthType.agentOnly);
        userAccount.setMes(Mes.createErrorMes(code, desc));
        return userAccount;
    }

    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        String redirectUrl = null;

        if (isUseForward()) {

            if (isForceHttps() && "http".equals(request.getScheme())) {
                // First redirect the current request to HTTPS.
                // When that request is received, the forward to the login page will be
                // used.
                redirectUrl = buildHttpsRedirectUrlForRequest(request);
            }

            log.info("redirectUrl FOR forward: " + redirectUrl);

            if (redirectUrl == null) {
                String loginForm = (String)determineUrl(request, response, authException)[0];

                log.info("Server side forward to: " + loginForm);

                RequestDispatcher dispatcher = request.getRequestDispatcher(loginForm);

                dispatcher.forward(request, response);

                return;
            }
        }
        else {
            // redirect to login page. Use https if forceHttps true
            Object[] result = determineUrl(request, response, authException);
            redirectUrl = (String)result[0];
            UserAccount userAccount = (UserAccount)result[1];
            ClientDetails clientDetail = (ClientDetails)result[2];
            if(userAccount!=null && userAccount.getMes()!=null && userAccount.getMes().getState()== Mes.MesState.err && userAccount.getAuthType()!=AuthType.agentOnly)
                Utils.setCookie(response, "errorMessage", URLEncoder.encode(userAccount.getMes().getDesc(), "UTF-8"), null, null, false, 5);

            if(clientDetail!=null && userAccount.getAuthType()==AuthType.agentOnly){
                if(!(userAccount.getMes()!=null && userAccount.getMes().getState()== Mes.MesState.err))
                    userAccount = h2hAuthorization.createAuthorizationCode(userAccount);
                request.setAttribute("h2hAuthorizationCodeResult", userAccount.getMes());
                userAccount.setMes(null);
                redirectUrl = "/oauth/getAuthorizationCode";
                RequestDispatcher dispatcher = request.getRequestDispatcher(redirectUrl);
                dispatcher.forward(request, response);
                return;
            }

            if (!UrlUtils.isAbsoluteUrl(redirectUrl))
                redirectUrl = makeAbsoluteUrl(request, response, redirectUrl);

            //redirectUrl = buildRedirectUrlToLoginPage(request, response, authException);

        }

        log.info("redirectUrl FOR redirect: " + redirectUrl);

        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }




    private String makeAbsoluteUrl(HttpServletRequest request, HttpServletResponse response, String loginForm){

        int serverPort = getPortResolver().getServerPort(request);
        String scheme = request.getScheme();

        RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();

        urlBuilder.setScheme(scheme);
        urlBuilder.setServerName(request.getServerName());
        urlBuilder.setPort(serverPort);
        urlBuilder.setContextPath(request.getContextPath());
        urlBuilder.setPathInfo(loginForm);

        if (isForceHttps() && "http".equals(scheme)) {
            Integer httpsPort = getPortMapper().lookupHttpsPort(Integer.valueOf(serverPort));

            if (httpsPort != null) {
                // Overwrite scheme and port in the redirect URL
                urlBuilder.setScheme("https");
                urlBuilder.setPort(httpsPort.intValue());
            }
            else {
                log.warning("Unable to redirect to HTTPS as no port mapping found for HTTP port "+ serverPort);
            }
        }
        return urlBuilder.getUrl();
    }
}

