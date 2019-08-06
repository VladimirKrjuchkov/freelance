package com.pb.tel.utils.io;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.*;

/**
 * Created by vladimir on 06.08.19.
 */
public class HttpServletRequestWrapper implements HttpServletRequest {
    private HttpServletRequest req;
    private byte[] contentData;
    private HashMap<String, String[]> parameters;

    public HttpServletRequestWrapper() {
        throw new IllegalArgumentException("Please use HttpServletRequestWrapper(HttpServletRequest request) constructor!");
    }

    private HttpServletRequestWrapper(HttpServletRequest request, byte[] contentData, HashMap<String, String[]> parameters) {
        this.req = request;
        this.contentData = contentData;
        this.parameters = parameters;
    }

    public HttpServletRequestWrapper(HttpServletRequest request) {
        if(request == null) {
            throw new IllegalArgumentException("The HttpServletRequest is null!");
        } else {
            this.req = request;
        }
    }

    public HttpServletRequest getRequest() {
        try {
            this.parseRequest();
        } catch (IOException var2) {
            throw new IllegalStateException("Cannot parse the request!", var2);
        }

        return new HttpServletRequestWrapper(this.req, this.contentData, this.parameters);
    }

    public byte[] getContentData() {
        return (byte[])this.contentData.clone();
    }

    public HashMap<String, String[]> getParameters() {
        HashMap<String, String[]> map = new HashMap(this.parameters.size() * 2);
        Iterator var2 = this.parameters.keySet().iterator();

        while(var2.hasNext()) {
            String key = (String)var2.next();
            map.put(key, ((String[])this.parameters.get(key)).clone());
        }

        return map;
    }

    private void parseRequest() throws IOException {
        if(this.contentData == null) {
            byte[] data = new byte[this.req.getContentLength()];
            int len;
            int totalLen = 0;
            ServletInputStream is = this.req.getInputStream();

            while(totalLen < data.length) {
                totalLen += len = is.read(data, totalLen, data.length - totalLen);
                if(len < 1) {
                    throw new IOException("Cannot read more than " + totalLen + (totalLen == 1?" byte!":" bytes!"));
                }
            }

            this.contentData = data;
            String enc = this.req.getCharacterEncoding();
            if(enc == null) {
                enc = "UTF-8";
            }

            String s = new String(data, enc);
            StringTokenizer st = new StringTokenizer(s, "&");
            HashMap<String, LinkedList<String>> mapA = new HashMap(data.length * 2);
            boolean decode = this.req.getContentType() != null && this.req.getContentType().equals("application/x-www-form-urlencoded");

            LinkedList list;
            while(st.hasMoreTokens()) {
                s = st.nextToken();
                int i = s.indexOf("=");
                if(i > 0 && s.length() > i + 1) {
                    String name = s.substring(0, i);
                    String value = s.substring(i + 1);
                    if(decode) {
                        try {
                            name = URLDecoder.decode(name, "UTF-8");
                        } catch (Exception var18) {
                            ;
                        }

                        try {
                            value = URLDecoder.decode(value, "UTF-8");
                        } catch (Exception var17) {
                            ;
                        }
                    }

                    list = (LinkedList)mapA.get(name);
                    if(list == null) {
                        list = new LinkedList();
                        mapA.put(name, list);
                    }

                    list.add(value);
                }
            }

            HashMap<String, String[]> map = new HashMap(mapA.size() * 2);
            Iterator var15 = mapA.keySet().iterator();

            while(var15.hasNext()) {
                String key = (String)var15.next();
                list = (LinkedList)mapA.get(key);
                map.put(key, new String[list.size()]);
            }

            this.parameters = map;
        }
    }

    public ServletInputStream getInputStream() throws IOException {
        this.parseRequest();
        return new HttpServletRequestWrapper.ServletInputStreamWrapper(this.contentData);
    }

    public BufferedReader getReader() throws IOException {
        this.parseRequest();
        String enc = this.req.getCharacterEncoding();
        if(enc == null) {
            enc = "UTF-8";
        }

        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.contentData), enc));
    }

    public String getParameter(String name) {
        try {
            this.parseRequest();
        } catch (IOException var3) {
            throw new IllegalStateException("Cannot parse the request!", var3);
        }

        String[] values = (String[])this.parameters.get(name);
        return values != null && values.length != 0?values[0]:null;
    }

    public Map getParameterMap() {
        try {
            this.parseRequest();
        } catch (IOException var2) {
            throw new IllegalStateException("Cannot parse the request!", var2);
        }

        return this.getParameters();
    }

    public Enumeration getParameterNames() {
        try {
            this.parseRequest();
        } catch (IOException var2) {
            throw new IllegalStateException("Cannot parse the request!", var2);
        }

        return new Enumeration<String>() {
            private String[] arr = (String[])HttpServletRequestWrapper.this.getParameters().keySet().toArray(new String[0]);
            private int idx = 0;

            public boolean hasMoreElements() {
                return this.idx < this.arr.length;
            }

            public String nextElement() {
                return this.arr[this.idx++];
            }
        };
    }

    public String[] getParameterValues(String name) {
        try {
            this.parseRequest();
        } catch (IOException var3) {
            throw new IllegalStateException("Cannot parse the request!", var3);
        }

        String[] arr = (String[])this.parameters.get(name);
        return arr == null?null:(String[])arr.clone();
    }

    public String getAuthType() {
        return this.req.getAuthType();
    }

    public String getContextPath() {
        return this.req.getContextPath();
    }

    public Cookie[] getCookies() {
        return this.req.getCookies();
    }

    public long getDateHeader(String name) {
        return this.req.getDateHeader(name);
    }

    public String getHeader(String name) {
        return this.req.getHeader(name);
    }

    public Enumeration getHeaderNames() {
        return this.req.getHeaderNames();
    }

    public Enumeration getHeaders(String name) {
        return this.req.getHeaders(name);
    }

    public int getIntHeader(String name) {
        return this.req.getIntHeader(name);
    }

    public String getMethod() {
        return this.req.getMethod();
    }

    public String getPathInfo() {
        return this.req.getPathInfo();
    }

    public String getPathTranslated() {
        return this.req.getPathTranslated();
    }

    public String getQueryString() {
        return this.req.getQueryString();
    }

    public String getRemoteUser() {
        return this.req.getRemoteUser();
    }

    public String getRequestURI() {
        return this.req.getRequestURI();
    }

    public StringBuffer getRequestURL() {
        return this.req.getRequestURL();
    }

    public String getRequestedSessionId() {
        return this.req.getRequestedSessionId();
    }

    public String getServletPath() {
        return this.req.getServletPath();
    }

    public HttpSession getSession() {
        return this.req.getSession();
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    public HttpSession getSession(boolean create) {
        return this.req.getSession(create);
    }

    public Principal getUserPrincipal() {
        return this.req.getUserPrincipal();
    }

    public boolean isRequestedSessionIdFromCookie() {
        return this.req.isRequestedSessionIdFromCookie();
    }

    public boolean isRequestedSessionIdFromURL() {
        return this.req.isRequestedSessionIdFromURL();
    }

    public boolean isRequestedSessionIdFromUrl() {
        return this.req.isRequestedSessionIdFromUrl();
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String username, String password) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        return null;
    }

    public boolean isRequestedSessionIdValid() {
        return this.req.isRequestedSessionIdValid();
    }

    public boolean isUserInRole(String role) {
        return this.req.isUserInRole(role);
    }

    public Object getAttribute(String name) {
        return this.req.getAttribute(name);
    }

    public Enumeration getAttributeNames() {
        return this.req.getAttributeNames();
    }

    public String getCharacterEncoding() {
        return this.req.getCharacterEncoding();
    }

    public int getContentLength() {
        return this.req.getContentLength();
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    public String getContentType() {
        return this.req.getContentType();
    }

    public String getLocalAddr() {
        return this.req.getLocalAddr();
    }

    public String getLocalName() {
        return this.req.getLocalName();
    }

    public int getLocalPort() {
        return this.req.getLocalPort();
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }

    public Locale getLocale() {
        return this.req.getLocale();
    }

    public Enumeration getLocales() {
        return this.req.getLocales();
    }

    public String getProtocol() {
        return this.req.getProtocol();
    }

    public String getRealPath(String path) {
        return this.req.getRealPath(path);
    }

    public String getRemoteAddr() {
        return this.req.getRemoteAddr();
    }

    public String getRemoteHost() {
        return this.req.getRemoteHost();
    }

    public int getRemotePort() {
        return this.req.getRemotePort();
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return this.req.getRequestDispatcher(path);
    }

    public String getScheme() {
        return this.req.getScheme();
    }

    public String getServerName() {
        return this.req.getServerName();
    }

    public int getServerPort() {
        return this.req.getServerPort();
    }

    public boolean isSecure() {
        return this.req.isSecure();
    }

    public void removeAttribute(String name) {
        this.req.removeAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        this.req.setAttribute(name, value);
    }

    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        this.req.setCharacterEncoding(env);
    }

    private class ServletInputStreamWrapper extends ServletInputStream {
        private byte[] data;
        private int idx = 0;

        ServletInputStreamWrapper(byte[] data) {
            if(data == null) {
                data = new byte[0];
            }

            this.data = data;
        }

        public int read() throws IOException {
            return this.idx == this.data.length?-1:this.data[this.idx++];
        }

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
    }
}

