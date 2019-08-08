package com.pb.tel.filter.oauth;

import com.pb.tel.utils.Utils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by vladimir on 29.07.19.
 */
public abstract class AbstractAuthenticationFilter implements Filter, InitializingBean {

    private static final Logger log = Logger.getLogger(AbstractAuthenticationFilter.class.getCanonicalName());

    protected AuthenticationManager authenticationManager;

    protected AuthenticationEntryPoint authenticationEntryPoint;

    protected AuthenticationFailureHandler failureHandler;

    protected TokenExtractor tokenExtractor;

    protected List<String> urlsAllowedGetRequestType = Collections.emptyList();

    protected List<String> allowedIp = Collections.emptyList();

    protected FilterProcessUrlRequestMatcher filterProcessUrlRequestMatcher;

    protected String filterProcessesUrl;


    protected AbstractAuthenticationFilter(){
        setFilterProcessesUrl("^.*/checked/.*$");
    }

    protected AbstractAuthenticationFilter(String filterProcessesUrl){
        setFilterProcessesUrl(filterProcessesUrl);
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    public void setTokenExtractor(TokenExtractor tokenExtractor) {
        this.tokenExtractor = tokenExtractor;
    }

    public void setUrlsAllowedGetRequestType(List<String> urlsAllowedGetRequestType) {
        this.urlsAllowedGetRequestType = urlsAllowedGetRequestType;
    }

    public void setAllowedIp(List<String> allowedIp) {
        this.allowedIp = allowedIp;
    }

    public void setFilterProcessUrlRequestMatcher(FilterProcessUrlRequestMatcher filterProcessUrlRequestMatcher) {
        this.filterProcessUrlRequestMatcher = filterProcessUrlRequestMatcher;
    }

    public void setFilterProcessesUrl(String filterProcessesUrl) {
        this.filterProcessesUrl = filterProcessesUrl;
        this.filterProcessUrlRequestMatcher = new FilterProcessUrlRequestMatcher(filterProcessesUrl);
    }

    public void afterPropertiesSet() {
        Assert.state(authenticationManager != null, "AuthenticationManager is required");
        Assert.state(authenticationEntryPoint != null, "AuthenticationEntryPoint is required");
        Assert.state(tokenExtractor != null, "TokenExtractor is required");
    }

    protected boolean isAloowedRequestForGetRequestType(HttpServletRequest request ){
        boolean result = false;
        if(urlsAllowedGetRequestType.isEmpty())
            return result;
        String requestUrl = Utils.getQueryUrl(request);
        for(String urlAllowedGetRequestType : urlsAllowedGetRequestType){
            if(requestUrl.contains(urlAllowedGetRequestType)){
                result = true;
                break;
            }
        }
        return result;
    }

    protected boolean isAloowedIp(HttpServletRequest request ){
        if(allowedIp.isEmpty())
            return true;
        String ip = request.getHeader("x-real-ip");
        if(Utils.isEmpty(ip))
            return true;
        for(String allowedIpAddr : allowedIp){
            if(ip.contains(allowedIpAddr))
                return true;
        }
        return false;
    }


    public abstract void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,ServletException;


    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }


    protected static final class FilterProcessUrlRequestMatcher implements RequestMatcher {

        private final String filterProcessesUrl;
        private final Pattern matcher;

        private FilterProcessUrlRequestMatcher(String filterProcessesUrl) {
            Assert.hasLength(filterProcessesUrl, "filterProcessesUrl must be specified");
            this.filterProcessesUrl = filterProcessesUrl;
            this.matcher = Pattern.compile(filterProcessesUrl);
        }

        public boolean matches(HttpServletRequest request) {
            String uri = request.getRequestURI();
            int pathParamIndex = uri.indexOf(';');

            if (pathParamIndex > 0) {
                uri = uri.substring(0, pathParamIndex);
            }
            log.fine("uri = "+uri);

            return matcher.matcher(uri).matches();
        }
    }
}

