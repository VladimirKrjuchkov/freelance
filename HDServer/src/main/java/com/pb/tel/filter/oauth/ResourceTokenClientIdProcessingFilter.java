package com.pb.tel.filter.oauth;

import com.pb.tel.service.auth.TokenClientIdExtractor;
import com.pb.tel.utils.MessageUtil;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetailsSource;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.util.Assert;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by vladimir on 29.07.19.
 */
public class ResourceTokenClientIdProcessingFilter extends AbstractAuthenticationFilter {

    private final Logger log = Logger.getLogger(ResourceTokenClientIdProcessingFilter.class.getCanonicalName());

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new OAuth2AuthenticationDetailsSource();

    public ResourceTokenClientIdProcessingFilter(){
        super();
        tokenExtractor = new TokenClientIdExtractor();
        authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
    }

    public ResourceTokenClientIdProcessingFilter(String filterProcessesUrl){
        super(filterProcessesUrl);
        tokenExtractor = new TokenClientIdExtractor();
        authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
    }

    /**
     * @param authenticationDetailsSource
     *            The AuthenticationDetailsSource to use
     */
    public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest,?> authenticationDetailsSource) {
        Assert.notNull(authenticationDetailsSource, "AuthenticationDetailsSource required");
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        try {
            if(!isAloowedIp(request))
                throw OAuth2Exception.create(OAuth2Exception.UNAUTHORIZED_CLIENT, MessageUtil.getMessage("AUTH31"));

            if(filterProcessUrlRequestMatcher.matches(request)){
                Authentication authentication = tokenExtractor.extract(request);
                log.info("authentication = "+authentication);

                if (authentication == null) {
                    log.fine("No token or client_id in request.");
                    throw OAuth2Exception.create(OAuth2Exception.UNAUTHORIZED_CLIENT, "No token or client_id in request.");
                }
                else {
                    request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, authentication.getPrincipal());
                    request.setAttribute("OAuth2AuthenticationDetails.CLIENT_ID_VALUE", authentication.getCredentials());
                    if (authentication instanceof AbstractAuthenticationToken) {
                        AbstractAuthenticationToken needsDetails = (AbstractAuthenticationToken) authentication;
                        needsDetails.setDetails(authenticationDetailsSource.buildDetails(request));
                    }
                    log.fine("Start Authentication");

                    Authentication authResult = authenticationManager.authenticate(authentication);

                    log.fine("Authentication success: " + authResult);

                    SecurityContextHolder.getContext().setAuthentication(authResult);
                }
            }
        }
        catch (OAuth2Exception failed) {
            SecurityContextHolder.clearContext();
            log.fine("Authentication request failed: " + failed);
            authenticationEntryPoint.commence(request, response, new InsufficientAuthenticationException(failed.getMessage(), failed));
            return;
        }

        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

}
