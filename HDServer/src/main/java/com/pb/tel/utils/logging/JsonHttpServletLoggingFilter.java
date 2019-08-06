package com.pb.tel.utils.logging;

import com.pb.tel.utils.io.BufferedBodyHttpServletRequestWrapper;
import com.pb.tel.utils.io.BufferedHttpServletResponseWrapper;
import com.pb.tel.utils.StringUtil;
import com.pb.tel.utils.VerifyUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by vladimir on 06.08.19.
 */
public class JsonHttpServletLoggingFilter implements Filter {
    public static final String HEADER_NAME_REAL_IP = "x-real-ip";
    public static final String HEADER_NAME_REAL_CONTENT_TYPE = "content-type";
    public static final String CONTENT_TYPE_MULTIPART = "multipart/form-data";
    private static final String timeFormat = "dd-MMM-yyyy HH:mm:ss";
    private static final Logger log = Logger.getLogger(JsonHttpServletLoggingFilter.class.getName());
    private final String passwordMask = "*****";
    private boolean hidePassword = false;
    private boolean hideData = false;
    private Pattern exclude;
    private Pattern skipResponseBodyByUrl;
    private Pattern skipResponseBodyByResponseContentType;

    public JsonHttpServletLoggingFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
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
            this.getTypesafeRequestMap(httpServletRequest);
            String contentType = request.getContentType();
            BufferedBodyHttpServletRequestWrapper bufferedReqest = new BufferedBodyHttpServletRequestWrapper(httpServletRequest);
            if(this.exclude != null && this.exclude.matcher(httpServletRequest.getRequestURL().toString().toLowerCase()).matches()) {
                chain.doFilter(request, response);
                return;
            }

            String rawBody;
            if(VerifyUtil.isNotEmpty(contentType) && contentType.toLowerCase().contains("multipart/form-data")) {
                rawBody = "FILE:multipart/form-data";
            } else {
                rawBody = bufferedReqest.getBody();
            }

            BufferedHttpServletResponseWrapper bufferedResponse = new BufferedHttpServletResponseWrapper(httpServletResponse);
            String realIp = httpServletRequest.getHeader("x-real-ip");
            String remoteAddr = httpServletRequest.getRemoteAddr();
            String showRemoteAddr = (realIp != null?realIp + "->":"") + remoteAddr;
            String body = this.hidePassword? StringUtil.hidePassword(rawBody):rawBody;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.US);
            formatter.setTimeZone(TimeZone.getDefault());
            Date currentDate = new Date();
            StringBuilder dtBuf = new StringBuilder(25);
            String dt = this.getDateTime(dtBuf, currentDate, formatter, start);
            String type = "INREQ";
            String request_uri = httpServletRequest.getRequestURL().toString();
            String request_body = normalizeBody(body);
            String request_headers = this.headers(httpServletRequest);
            String request_type = httpServletRequest.getMethod();
            chain.doFilter(bufferedReqest, bufferedResponse);
            long duration = System.currentTimeMillis() - start;
            String response_body = Boolean.valueOf(bufferedResponse.containsHeader("LoggingFile")).booleanValue()?" response = FILE ":(this.hideData?StringUtil.hideData(this.responseContent(httpServletRequest, bufferedResponse), bufferedResponse.getContentType()):this.responseContent(httpServletRequest, bufferedResponse));
            String response_code = String.valueOf(200);
            StringBuilder logMessage = (new StringBuilder("\n{ ")).append(" \"DT\":\"").append(dt).append("\",").append(" \"TYPE\": \"").append(type).append("\",").append(" \"REMOTE_HOST\": \"").append(showRemoteAddr).append("\",").append(" \"DURATION\": \"").append(duration).append("\",").append(" \"REQUEST_URI\": \"").append(request_uri).append("\",").append(" \"REQUEST_BODY\": \"").append(request_body).append("\",").append(" \"REQUEST_HEADERS\": \"").append(request_headers).append("\",").append(" \"REQUEST_TYPE\": \"").append(request_type).append("\",").append(" \"REQUEST_PARAMS\": \"").append(this.getTypesafeRequestMap(httpServletRequest)).append("\",").append(" \"RESPONSE_CODE\": \"").append(response_code).append("\",").append(" \"RESPONSE_BODY\": \"").append(normalizeBody(response_body)).append("\",").append(" } ");
            log.fine(logMessage.toString());
        } catch (IOException var36) {
            log.log(Level.SEVERE, "", var36);
            throw var36;
        } catch (ServletException var37) {
            log.log(Level.SEVERE, "", var37);
            throw var37;
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
            result.append("{");

            while(true) {
                while(true) {
                    Object eachName;
                    do {
                        if(!headerNames.hasMoreElements()) {
                            result = result.deleteCharAt(result.lastIndexOf(","));
                            result.append("}");
                            return result.toString();
                        }

                        eachName = headerNames.nextElement();
                    } while(!(eachName instanceof String));

                    result.append(" \"").append(eachName).append("\" : ");
                    if(this.hidePasswordDetermine((String)eachName)) {
                        result.append("\"*****\" ,");
                    } else {
                        Enumeration<?> headerEnumeration = httpServletRequest.getHeaders((String)eachName);
                        int count = 0;

                        while(headerEnumeration.hasMoreElements()) {
                            Object eachValue = headerEnumeration.nextElement();
                            ++count;
                            result.append("\"" + (String)eachValue + "\" ,");
                            if(count > 1) {
                                result.append(" , \"").append(eachName + "_" + count + "\"").append(" : ").append("\"" + (String)eachValue + "\" ,");
                            }
                        }
                    }
                }
            }
        }
    }

    private Map<String, String> getTypesafeRequestMap(HttpServletRequest request) {
        Map<String, String> typesafeRequestMap = new HashMap();

        String requestParamName;
        String requestParamValue;
        for(Enumeration requestParamNames = request.getParameterNames(); requestParamNames.hasMoreElements(); typesafeRequestMap.put(requestParamName, requestParamValue)) {
            requestParamName = (String)requestParamNames.nextElement();
            requestParamValue = request.getParameter(requestParamName);
            if(this.hidePasswordDetermine(requestParamName)) {
                requestParamValue = "*****";
            }
        }

        return typesafeRequestMap;
    }

    private boolean hidePasswordDetermine(String name) {
        return this.hidePassword && name != null && name.matches(".*[pP]assword.*");
    }

    private String getDateTime(StringBuilder dtBuf, Date currentDate, SimpleDateFormat formatter, long timestamp) {
        currentDate.setTime(timestamp);
        dtBuf.append(formatter.format(currentDate));
        long frac = timestamp % 1000L;
        dtBuf.append('.');
        if(frac < 100L) {
            if(frac < 10L) {
                dtBuf.append('0');
                dtBuf.append('0');
            } else {
                dtBuf.append('0');
            }
        }

        dtBuf.append(frac);
        return dtBuf.toString();
    }

    private static String normalizeBody(String source) {
        return source.replaceAll(">\\s{2,}<", "> <").replaceAll("\"", "\\\\\"").replaceAll("\n", " ");
    }

    public void destroy() {
    }
}

