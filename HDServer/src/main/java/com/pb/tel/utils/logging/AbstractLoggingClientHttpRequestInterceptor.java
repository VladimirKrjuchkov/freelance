package com.pb.tel.utils.logging;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.*;

/**
 * Created by vladimir on 06.08.19.
 */
public abstract class AbstractLoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    public AbstractLoggingClientHttpRequestInterceptor() {
    }

    public abstract void logRequest(HttpRequest var1, byte[] var2);

    public abstract void logResponse(long var1, String var3, ClientHttpResponse var4) throws IOException;

    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        long start = System.currentTimeMillis();
        this.logRequest(request, body);
        final ClientHttpResponse execute = execution.execute(request, body);
        execute.getRawStatusCode();
        BufferedInputStream bis = new BufferedInputStream(execute.getBody());
        final ByteArrayOutputStream byteOs = new ByteArrayOutputStream();

        int read;
        while((read = bis.read()) != -1) {
            byteOs.write(read);
        }

        long respTime = System.currentTimeMillis() - start;
        this.logResponse(respTime, byteOs.toString(), execute);
        return new ClientHttpResponse() {
            ClientHttpResponse original = execute;
            InputStream body = new ByteArrayInputStream(byteOs.toByteArray());

            public InputStream getBody() throws IOException {
                return this.body;
            }

            public HttpHeaders getHeaders() {
                return this.original.getHeaders();
            }

            public HttpStatus getStatusCode() throws IOException {
                HttpStatus status = null;

                try {
                    status = this.original.getStatusCode();
                } catch (IllegalArgumentException var3) {
                    if(this.original.getRawStatusCode() > 100 && this.original.getRawStatusCode() < 200) {
                        status = HttpStatus.CONTINUE;
                    } else if(this.original.getRawStatusCode() > 200 && this.original.getRawStatusCode() < 300) {
                        status = HttpStatus.OK;
                    } else if(this.original.getRawStatusCode() > 400 && this.original.getRawStatusCode() < 500) {
                        status = HttpStatus.BAD_REQUEST;
                    } else if(this.original.getRawStatusCode() > 500 && this.original.getRawStatusCode() < 600) {
                        status = HttpStatus.INTERNAL_SERVER_ERROR;
                    }
                }

                return status;
            }

            public int getRawStatusCode() throws IOException {
                return this.original.getRawStatusCode();
            }

            public String getStatusText() throws IOException {
                return this.original.getStatusText();
            }

            public void close() {
                this.original.close();
            }
        };
    }
}

