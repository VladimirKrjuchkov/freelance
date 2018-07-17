package com.pb.tel.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pb.tel.data.channels.ChannelsRequest;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 09.07.18.
 */
public class ChannelsRequestFilter implements Filter {

    private final Logger log = Logger.getLogger(ChannelsRequestFilter.class.getCanonicalName());

    private static ObjectMapper jacksonObjectMapper = new ObjectMapper();

    public void init(FilterConfig arg0) throws ServletException {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ChannelsRequest channelsRequest = jacksonObjectMapper.readValue(request.getInputStream(), ChannelsRequest.class);
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        if("operatorStatus".equalsIgnoreCase(channelsRequest.getAction())){
            httpResponse.sendError(405);
        }else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {}
}
