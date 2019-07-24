package com.pb.tel.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        cookie.setMaxAge(expire);
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

    public static Date getDateAfterSeconds(int afterSeconds){
        return new Date(System.currentTimeMillis() + afterSeconds*1000);
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
