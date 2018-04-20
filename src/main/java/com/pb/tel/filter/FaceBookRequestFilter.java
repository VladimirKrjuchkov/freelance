package com.pb.tel.filter;

import com.pb.tel.data.facebook.FaceBookRequest;
import com.pb.util.zvv.PropertiesUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 11.04.18.
 */
public class FaceBookRequestFilter implements Filter{

    private final Logger log = Logger.getLogger(TelegramRequestFilter.class.getCanonicalName());

    private final String facebookUpdateToken = PropertiesUtil.getProperty("facebook_update_token");

    public void init(FilterConfig arg0) throws ServletException {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestToken = request.getParameter("hub.verify_token");
        HttpServletResponse httpResponse = (HttpServletResponse)response;

        if(!facebookUpdateToken.equals(requestToken)){
            log.log(Level.INFO, "Access denied for " + requestToken);
            httpResponse.sendError(403);
        }else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {}
}
