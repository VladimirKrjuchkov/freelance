//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.pb.tel.utils.logging;

import com.pb.tel.utils.io.BufferedBodyHttpServletRequestWrapper;
import com.pb.tel.utils.io.BufferedHttpServletResponseWrapper;
import com.pb.tel.utils.StringUtil;
import com.pb.tel.utils.VerifyUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class HttpServletRequestResponseLoggingFilter implements Filter {
    public static final String HEADER_NAME_REAL_IP = "x-real-ip";
    public static final String HEADER_NAME_REAL_CONTENT_TYPE = "content-type";
    public static final String CONTENT_TYPE_MULTIPART = "multipart/form-data";
    private static final Logger log = Logger.getLogger(HttpServletRequestResponseLoggingFilter.class.getName());
    private final String passwordMask = "*****";
    private boolean hidePassword = false;
    private boolean hideData = false;
    private Pattern exclude;
    private Pattern skipResponseBodyByUrl;
    private Pattern skipResponseBodyByResponseContentType;

    public HttpServletRequestResponseLoggingFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        MessageHandler.setUseMessageSeparator(Boolean.valueOf(filterConfig.getInitParameter("useMessageSeparator")).booleanValue());
        this.hidePassword = Boolean.valueOf(filterConfig.getInitParameter("hidePassword")).booleanValue();
        this.hideData = Boolean.valueOf(filterConfig.getInitParameter("hideData")).booleanValue();
        this.exclude = this.extractPattern(filterConfig.getInitParameter("exclude-url-pattern"));
        this.skipResponseBodyByUrl = this.extractPattern(filterConfig.getInitParameter("skip-response-body-url-pattern"));
        this.skipResponseBodyByResponseContentType = this.extractPattern(filterConfig.getInitParameter("skip-response-body-response-content-type-pattern"));
    }

    private Pattern extractPattern(String regexp) {
        if(VerifyUtil.isNotEmpty(regexp)) {
            regexp = regexp.toLowerCase();

            try {
                return Pattern.compile(regexp);
            } catch (IllegalArgumentException var3) {
                log.log(Level.SEVERE, "for regexp: " + regexp, var3);
            }
        }

        return null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            long start = System.currentTimeMillis();
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            HttpServletResponse httpServletResponse = (HttpServletResponse)response;
            Map<String, String> requestMap = this.getTypesafeRequestMap(httpServletRequest);
            String contentType = request.getContentType();
            BufferedBodyHttpServletRequestWrapper bufferedReqest = new BufferedBodyHttpServletRequestWrapper(httpServletRequest);
            if(this.exclude == null || !this.exclude.matcher(httpServletRequest.getRequestURL().toString().toLowerCase()).matches()) {
                String rawBody;
                if(VerifyUtil.isNotEmpty(contentType) && contentType.toLowerCase().contains("multipart/form-data")) {
                    rawBody = "FILE:multipart/form-data";
                } else {
                    rawBody = bufferedReqest.getBody();
                }

                BufferedHttpServletResponseWrapper bufferedResponse = new BufferedHttpServletResponseWrapper(httpServletResponse);
                String realIp = httpServletRequest.getHeader("x-real-ip");
                String realHeaderContentType = httpServletRequest.getHeader("content-type");
                String remoteAddr = httpServletRequest.getRemoteAddr();
                String showRemoteAddr = (realIp != null?realIp + "->":"") + remoteAddr;
                String body = this.hidePassword? StringUtil.hidePassword(rawBody):rawBody;
                String pathInfo = httpServletRequest.getPathInfo();
                StringBuilder logMessage = (new StringBuilder("\nREST Request - ")).append("[HTTP METHOD:").append(httpServletRequest.getMethod()).append("] [REMOTE ADDRESS:").append(showRemoteAddr).append("] [REAL CONTENT TYPE:").append(realHeaderContentType).append("] [HEADERS:").append(this.headers(httpServletRequest)).append("] [REQUEST PARAMETERS:").append(requestMap).append("] [REQUEST URL:").append(httpServletRequest.getRequestURL()).append("] [PATH INFO:").append(pathInfo).append("]\n[REQUEST BODY:").append(body).append("] ");
                logMessage.append(" | logger time for request: " + (System.currentTimeMillis() - start));
                MessageHandler.initLogWriter("\n=================== START " + (pathInfo == null?httpServletRequest.getRequestURL():pathInfo) + " ===================");
                MessageHandler.addMessage(logMessage.toString() + "\n");
                chain.doFilter(bufferedReqest, bufferedResponse);
                long restime = System.currentTimeMillis();
                StringBuilder logTmp = (new StringBuilder("[RESPONSE (content-type:" + bufferedResponse.getContentType() + ") at ")).append(System.currentTimeMillis() - start).append(" ms:").append(Boolean.valueOf(bufferedResponse.containsHeader("LoggingFile")).booleanValue()?" response = FILE ":(this.hideData?StringUtil.hideData(this.responseContent(httpServletRequest, bufferedResponse), bufferedResponse.getContentType()):this.responseContent(httpServletRequest, bufferedResponse))).append("]");
                logMessage.append("\n").append(logTmp).append(" | logger time for response: " + (System.currentTimeMillis() - restime));
                log.fine(logMessage.toString());
                MessageHandler.addMessage(logTmp.toString());
                return;
            }

            chain.doFilter(request, response);
        } catch (IOException var27) {
            log.log(Level.SEVERE, "", var27);
            throw var27;
        } catch (ServletException var28) {
            log.log(Level.SEVERE, "", var28);
            throw var28;
        } finally {
            if(MessageHandler.isUseMessageSeparator()) {
                log.fine("<10 finishMarker 01>");
            }

        }

    }

    private String responseContent(HttpServletRequest httpServletRequest, BufferedHttpServletResponseWrapper bufferedResponse) {
        return this.skipResponseBodyByUrl != null && this.skipResponseBodyByUrl.matcher(httpServletRequest.getRequestURL().toString().toLowerCase()).matches()?"skip-body by url":(this.skipResponseBodyByResponseContentType != null && VerifyUtil.isNotEmpty(bufferedResponse.getContentType()) && this.skipResponseBodyByResponseContentType.matcher(bufferedResponse.getContentType()).matches()?"skip-body by response content-type: " + bufferedResponse.getContentType():bufferedResponse.getContent());
    }

    private String headers(HttpServletRequest httpServletRequest) {
        Enumeration<?> headerNames = httpServletRequest.getHeaderNames();
        if(headerNames == null) {
            return "";
        } else {
            StringBuilder result = new StringBuilder();

            while(true) {
                while(true) {
                    Object eachName;
                    do {
                        if(!headerNames.hasMoreElements()) {
                            return result.toString();
                        }

                        eachName = headerNames.nextElement();
                    } while(!(eachName instanceof String));

                    result.append(" ").append(eachName).append("=");
                    if(this.hidePasswordDetermine((String)eachName)) {
                        result.append("*****");
                    } else {
                        Enumeration<?> headerEnumeration = httpServletRequest.getHeaders((String)eachName);

                        Object eachValue;
                        for(int count = 0; headerEnumeration.hasMoreElements(); result.append((String)eachValue)) {
                            eachValue = headerEnumeration.nextElement();
                            ++count;
                            if(count > 1) {
                                result.append(" ").append(eachName).append("=");
                            }
                        }
                    }
                }
            }
        }
    }

    private Map<String, String> getTypesafeRequestMap(HttpServletRequest request) {
        Map<String, String> typesafeRequestMap = new HashMap();

        String requestParamValue;
        String requestParamName;
        for(Enumeration requestParamNames = request.getParameterNames(); requestParamNames.hasMoreElements(); typesafeRequestMap.put(requestParamName, requestParamValue)) {
            requestParamName = (String)requestParamNames.nextElement();
            if(!"file".equals(requestParamName) && !"files".equals(requestParamName)) {
                requestParamValue = request.getParameter(requestParamName);
            } else {
                requestParamValue = "FILE";
            }

            if(requestParamValue.length() > 1000) {
                requestParamValue = "TOO BIG SIZE OF PARAMETR FOR LOGGING";
            }

            if(this.hidePasswordDetermine(requestParamName)) {
                requestParamValue = "*****";
            }
        }

        return typesafeRequestMap;
    }

    private boolean hidePasswordDetermine(String name) {
        return this.hidePassword && name != null && name.matches(".*[pP]assword.*");
    }

    public void destroy() {
    }
}
