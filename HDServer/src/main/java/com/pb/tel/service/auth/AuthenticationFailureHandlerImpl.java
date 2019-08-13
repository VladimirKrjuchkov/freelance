package com.pb.tel.service.auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 26.07.19.
 */
public class
AuthenticationFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger log = Logger.getLogger(AuthenticationFailureHandlerImpl.class.getCanonicalName());

    private String defaultFailureUrl;

    public AuthenticationFailureHandlerImpl(String defaultFailureUrl){
        super(defaultFailureUrl);
        this.defaultFailureUrl = defaultFailureUrl;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)throws IOException, ServletException {
        log.info("onAuthenticationFailure::response: " + response);
        log.info("onAuthenticationFailure::message: " + exception.getMessage());
        log.log(Level.SEVERE, "Authentication failure. Redirect to : "+defaultFailureUrl, exception);
        super.onAuthenticationFailure(request, response, exception);
    }

}
