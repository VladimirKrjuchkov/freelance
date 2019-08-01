package com.pb.tel.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Created by vladimir on 26.07.19.
 */
@Service("messageUtil")
public class MessageUtil {

    private static final Logger log = Logger.getLogger(MessageUtil.class.getCanonicalName());

    private static MessageSource messageSourceStatic;

    public static Environment env;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Environment environment;

    private static Locale en = new Locale("en");
    private static Locale uk = new Locale("uk");
    private static Locale ru = new Locale("ru");


    @PostConstruct
    private void init(){
        messageSourceStatic = messageSource;
        env = environment;
    }


    public static String getMessageTemplate(String code){
        return messageSourceStatic.getMessage(code, null, uk);
    }

    public static String getMessage(String code){
        return messageSourceStatic.getMessage(code, null, uk).replaceAll("\\{[0-9]\\}", "");
    }

    public static String getMessage(String code, String ...args){
        return messageSourceStatic.getMessage(code, args, uk).replaceAll("\\{[0-9]\\}", "");
    }

    public static String getProperty(String propertyName){
        return env.getProperty(propertyName);
    }
}
