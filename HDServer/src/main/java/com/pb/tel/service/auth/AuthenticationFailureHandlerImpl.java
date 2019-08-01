package com.pb.tel.service.auth;

import com.pb.tel.utils.Utils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 26.07.19.
 */
public class AuthenticationFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger log = Logger.getLogger(AuthenticationFailureHandlerImpl.class.getCanonicalName());

    private String defaultFailureUrl;

    public AuthenticationFailureHandlerImpl(String defaultFailureUrl){
        super(defaultFailureUrl+"/admin.html");
        this.defaultFailureUrl = defaultFailureUrl+"/admin.html";
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)throws IOException, ServletException {
        log.info("onAuthenticationFailure !!!! #$# response = "+response);
        log.info("onAuthenticationFailure !!!! #$# exception.getMessage() = "+exception.getMessage());
//		String sidBi = "";
//		UserAccount userAccount = null;
//		String code = AuthFormActionCodes.showBanks.toString();
//		String message = URLEncoder.encode("Глобальна помилка аутентифікації !", "UTF-8");
//		String errorUrl = "/DataAccessService/error";
//		com.pb.bi.service.auth.AuthenticationException authException;
//		if(exception instanceof com.pb.bi.service.auth.AuthenticationException){
//			authException = (com.pb.bi.service.auth.AuthenticationException)exception;
//			if(authException.getExtension() instanceof AuthFormActionCodes){
//				AuthFormActionCodes authFormActionCodes = (AuthFormActionCodes)authException.getExtension();
//				if(authFormActionCodes == AuthFormActionCodes.showBanks ){
//					sidBi = request.getParameter("sidBi");
//					errorUrl = authFormActionCodes.getShowUrl();
//				}
//				if(!isEmpty(sidBi))
//					userAccount = userAccountStore.getValue(sidBi);
//				code = authFormActionCodes.name();
//			}
//			if(authException.getExtension() instanceof LogicException){
//				code = ((LogicException)authException.getExtension()).getCode();
//			}
//			message = authException.getMessage();
//		}
//		if(userAccount==null)
//			userAccount = new UserAccount(sidBi = UUID.randomUUID().toString());
//		userAccount.setMes(Util.getErrorAnswer(code, message));
//		userAccountStore.putValue(sidBi, userAccount, Util.getDateAfterSeconds(30));
//		errorUrl = errorUrl+"?sidBi="+sidBi;
//		if(userAccount.getAuthType()==AuthType.agentOnly && com.pb.util.bpn.VerifyUtil.in(userAccount.getClientId(), ""))
//			request.getRequestDispatcher(errorUrl).forward(request, response);
//		else
//			response.sendRedirect(errorUrl);

        log.log(Level.SEVERE, "Was finished authentication failer handler. Reset exac and sidCheck cookie.  Redirect to : "+defaultFailureUrl, exception);
        //response.sendRedirect(defaultFailureUrl);
        Utils.setCookie(response, "exac", "", null/*"/PplsService/"*/, null/*MessageUtil.getProperty("server.domain")*/, true, 0);
        Utils.setCookie(response, "sidCheck", "", null, null, false, 0);
        Utils.setCookie(response, "errorMessage", URLEncoder.encode(exception.getMessage(), "UTF-8"), null, null, false, 5);
        super.onAuthenticationFailure(request, response, exception);
        //response.sendError(HttpStatus.UNAUTHORIZED.value(),	HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

}
