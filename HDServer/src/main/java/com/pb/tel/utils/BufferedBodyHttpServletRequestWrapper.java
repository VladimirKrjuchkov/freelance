//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.pb.tel.utils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BufferedBodyHttpServletRequestWrapper extends HttpServletRequestWrapper {
    Logger log = Logger.getLogger(BufferedBodyHttpServletRequestWrapper.class.getName());
    private String body;
    private final Object bodySynchro = new Object();
    private final byte[] bodyBytes;

    public BufferedBodyHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        BufferedInputStream bis = null;

        try {
            InputStream is = request.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(is.available());
            if(is != null) {
                bis = new BufferedInputStream(is);
                byte[] buf = new byte[8192];
                boolean var6 = true;

                int bytesRead;
                while((bytesRead = bis.read(buf)) > 0) {
                    baos.write(buf, 0, bytesRead);
                }
            }

            this.bodyBytes = baos.toByteArray();
            this.body = new String(this.bodyBytes);
        } catch (IOException var14) {
            this.log.log(Level.SEVERE, "", var14);
            throw var14;
        } finally {
            if(bis != null) {
                try {
                    bis.close();
                } catch (IOException var13) {
                    this.log.log(Level.SEVERE, "", var13);
                    throw var13;
                }
            }

        }

    }

    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.bodyBytes);
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return servletInputStream;
    }

    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    public String getBody() {
        if(this.body == null && this.bodyBytes != null) {
            Object var1 = this.bodySynchro;
            synchronized(this.bodySynchro) {
                if(this.body != null) {
                    return this.body;
                }

                this.body = new String(this.bodyBytes);
            }

            return this.body;
        } else {
            return this.body;
        }
    }

    public byte[] getBodyBytes() {
        return this.bodyBytes;
    }

    public Map<String, String> getTypesafeRequestMap() {
        Map<String, String> typesafeRequestMap = new HashMap();
        Enumeration requestParamNames = this.getParameterNames();

        while(requestParamNames.hasMoreElements()) {
            String requestParamName = (String)requestParamNames.nextElement();
            String requestParamValue = this.getParameter(requestParamName);
            typesafeRequestMap.put(requestParamName, requestParamValue);
        }

        return typesafeRequestMap;
    }

    public static String extractBody(ServletRequest servletRequest) {
        return extract(servletRequest).getBody();
    }

    public static BufferedBodyHttpServletRequestWrapper extract(ServletRequest servletRequest) {
        if(servletRequest instanceof BufferedBodyHttpServletRequestWrapper) {
            return (BufferedBodyHttpServletRequestWrapper)servletRequest;
        } else {
            ServletRequest forEachWrap = servletRequest;

            do {
                if(!(forEachWrap instanceof ServletRequestWrapper)) {
                    throw new RuntimeException("Servlet class must extends/wrap com.pb.util.bpn.ws.io.BufferedBodyHttpServletRequestWrapper. Actual servlet class: " + servletRequest.getClass().getName());
                }

                forEachWrap = ((ServletRequestWrapper)forEachWrap).getRequest();
            } while(!(forEachWrap instanceof BufferedBodyHttpServletRequestWrapper));

            return (BufferedBodyHttpServletRequestWrapper)forEachWrap;
        }
    }

    public static byte[] extractBodyBytes(ServletRequest servletRequest) {
        return extract(servletRequest).getBodyBytes();
    }
}
