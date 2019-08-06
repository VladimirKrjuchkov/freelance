package com.pb.tel.utils.logging;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 06.08.19.
 */
public class LoggingClientHttpRequestInterceptor extends AbstractLoggingClientHttpRequestInterceptor {
    private Logger log;
    private boolean logResponseHeaders;
    private boolean trimResponses;
    private Level level;

    public LoggingClientHttpRequestInterceptor(String classNameForLogger) {
        this.level = Level.INFO;
        this.log = Logger.getLogger(classNameForLogger);
    }

    public LoggingClientHttpRequestInterceptor(String classNameForLogger, boolean logResponseHeaders) {
        this.level = Level.INFO;
        this.log = Logger.getLogger(classNameForLogger);
        this.logResponseHeaders = logResponseHeaders;
    }

    public LoggingClientHttpRequestInterceptor(String classNameForLogger, Level level, boolean logResponseHeaders) {
        this(classNameForLogger, logResponseHeaders);
        if(level != null) {
            this.level = level;
        }

    }

    public LoggingClientHttpRequestInterceptor(Logger logger, boolean logResponseHeaders) {
        this.level = Level.INFO;
        this.log = logger;
        this.logResponseHeaders = logResponseHeaders;
    }

    public void logRequest(HttpRequest request, byte[] body) {
        String headers = request.getHeaders().toString();
        this.log.log(this.level, "Request [" + request.getMethod() + "] to URL: " + request.getURI().toString() + "\nRequest headers: " + headers + "\nRequest body: " + new String(body));
    }

    public void logResponse(long timeMs, String responseBody, ClientHttpResponse responseEntity) throws IOException {
        if(this.trimResponses) {
            responseBody = this.trimResponse(responseBody);
        }

        if(this.logResponseHeaders) {
            this.log.log(this.level, "Response at " + timeMs + " ms (status: " + responseEntity.getRawStatusCode() + "):\nheaders: " + responseEntity.getHeaders() + "\nbody: " + responseBody);
        } else {
            this.log.log(this.level, "Response at " + timeMs + " ms (status: " + responseEntity.getRawStatusCode() + "): " + responseBody);
        }

    }

    public void setLogResponseHeaders(boolean logResponseHeaders) {
        this.logResponseHeaders = logResponseHeaders;
    }

    private String trimResponse(String responseBody) {
        return responseBody.replaceAll("\\n", "").replaceAll("[\\s]{2,}", " ");
    }

    public boolean isTrimResponses() {
        return this.trimResponses;
    }

    public void setTrimResponses(boolean trimResponses) {
        this.trimResponses = trimResponses;
    }
}

