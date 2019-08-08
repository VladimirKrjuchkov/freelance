package com.pb.tel.utils;

import com.pb.tel.service.exception.LogicException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Created by vladimir on 25.06.19.
 */

public class Utils {
    @Autowired
    private Environment environment;

    public static Environment property;

    @PostConstruct
    private void init(){
        property = environment;
    }

    public static void setCookie(HttpServletResponse response, String cookieName, String cookieValue, String path, String domain, boolean isHttpOnly, int expire){
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        if(path!=null)
            cookie.setPath(path);
        if(!isEmpty(domain))
            cookie.setDomain(domain);
        cookie.setMaxAge(expire/1000+10500);
        cookie.setHttpOnly(isHttpOnly);
        response.addCookie(cookie);
    }

    public static int getSecondsToDate(Date date){
        int ttlValue = (int)(date.getTime() - System.currentTimeMillis())/1000;
        if(ttlValue<0)
            ttlValue = 0;
        return ttlValue;
    }

    public static String getCookie(HttpServletRequest request, String name){
        String value = null;
        Cookie [] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                value = cookie.getValue();
            }
        }
        return value;
    }

    public static String getHash(String input, String algorithm)throws Exception{
        if(isEmpty(input))
            return null;
        MessageDigest md = MessageDigest.getInstance(algorithm);//SHA-1 SHA-256	MD5
        md.update(input.getBytes("UTF-8"));
        byte[] digest = md.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(0xff & digest[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String exrtactCookieValue(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();
        if(!isEmpty(cookies))
            for(Cookie cookie : cookies){
                if(cookieName.equals(cookie.getName()))
                    return cookie.getValue();
            }
        return null;
    }

    public static String getQueryUrl(HttpServletRequest request) {
        return request.getRequestURL()+(isEmpty(request.getQueryString()) ? "" : "?"+request.getQueryString());
    }

    public static String getQueryString(HttpServletRequest request) {
        StringBuffer query = request.getRequestURL();
        Map<String, String[]> parameterMap = request.getParameterMap();
        int countParamValue = 0;
        String paramDelimiter = "?";
        for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
            for (String paramValue : param.getValue()) {
                if(countParamValue>0)
                    paramDelimiter="&";
                query.append(paramDelimiter).append(param.getKey()).append("=").append(paramValue);
                countParamValue++;
            }
        }
        return query.toString();
    }


    public static LogicException getLogicException(String code, Object... arguments){
        return getLogicException(code, null, arguments);
    }

    public static LogicException getLogicException(String code, Exception e, Object... arguments){
        return new LogicException(code, MessageUtil.getMessageTemplate(code), e, arguments);
        //return new LogicException(code, PropertiesUtil.getProperty(code), e, arguments);
    }

    public static Date getDateAfterSeconds(int afterSeconds){
        return new Date(System.currentTimeMillis() + afterSeconds*1000);
    }

    public static String getSh256(String toHash)throws Exception{
        return Base64.getUrlEncoder().withoutPadding().encodeToString(DigestUtils.getSha256Digest().digest(toHash.getBytes("UTF-8")));
    }

    public static String getSh512(String toHash)throws Exception{
        return Base64.getUrlEncoder().withoutPadding().encodeToString(DigestUtils.getSha512Digest().digest(toHash.getBytes("UTF-8")));
    }

    public static boolean isEmpty(Object data){
        if(data==null)
            return true;
        if(data instanceof String && ((String)data).isEmpty())
            return true;
        if(data instanceof Collection && ((Collection)data).isEmpty())
            return true;
        if(data instanceof Map && ((Map)data).isEmpty())
            return true;
        return false;
    }
}
