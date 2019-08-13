package com.pb.tel.filter.oauth;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by vladimir on 13.08.19.
 */
public class ClientCredentialsTokenEndpointFilter extends org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter{

    private final Logger log = Logger.getLogger(ClientCredentialsTokenEndpointFilter.class.getCanonicalName());

    private boolean allowOnlyPost = false;

    public ClientCredentialsTokenEndpointFilter(){
        super();
    }

    public ClientCredentialsTokenEndpointFilter(String path){
        super(path);
    }

    @Override
    public void setAllowOnlyPost(boolean allowOnlyPost) {
        super.setAllowOnlyPost(allowOnlyPost);
        this.allowOnlyPost = allowOnlyPost;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
    
        if (allowOnlyPost && !"POST".equalsIgnoreCase(request.getMethod())) {
            throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[] { "POST" });
        }

        String clientId = request.getParameter("client_id");
        String clientSecret = request.getParameter("client_secret");
        String secretComponent = request.getParameter("code");
        if(!StringUtils.hasText(secretComponent))
            secretComponent = request.getParameter("refresh_token");
        if(!StringUtils.hasText(secretComponent))
            throw new InvalidRequestException("Missing grant type");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication;
        }

        if (clientId == null) {
            throw new BadCredentialsException("No client credentials presented");
        }

        if (clientSecret == null) {
            clientSecret = "";
        }

        clientId = clientId.trim();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(clientId,	clientSecret);
        authRequest.setDetails(secretComponent);

        return this.getAuthenticationManager().authenticate(authRequest);

    }

}

