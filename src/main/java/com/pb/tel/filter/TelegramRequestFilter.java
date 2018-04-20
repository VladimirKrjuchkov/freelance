package com.pb.tel.filter;

import com.pb.util.zvv.PropertiesUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 02.03.18.
 */
public class TelegramRequestFilter implements Filter{

    private final Logger log = Logger.getLogger(TelegramRequestFilter.class.getCanonicalName());

    private final String telegramBotToken = PropertiesUtil.getProperty("telegram_bot_token");

    public void init(FilterConfig arg0) throws ServletException {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestToken = request.getParameter("token");
        HttpServletResponse httpResponse = (HttpServletResponse)response;

        if(!telegramBotToken.equals(requestToken)){
            log.log(Level.INFO, "Access denied for " + requestToken);
            httpResponse.sendError(403);
        }else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {}

}
