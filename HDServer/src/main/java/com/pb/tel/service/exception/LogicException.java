package com.pb.tel.service.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * Created by vladimir on 20.04.18.
 */
public class LogicException extends Exception{
    private static final long serialVersionUID = -3155559670065775765L;

    @Autowired
    private Environment environment;

    private String code;
    private String messageTemplate;
    private Object[] arguments;
    private Exception parent;

    public LogicException(String code, String messageTemplate, Exception e, Object... arguments){
        super(e);
        this.code = code;
        this.messageTemplate = messageTemplate;
        if(messageTemplate==null)
            this.messageTemplate = environment.getProperty("UNKN02");
        for(Object arg : arguments){
            if(arg==null)
                arg="";
        }
        this.arguments = arguments;
        this.parent = e;
    }

    public LogicException(String code, Exception e, Object... arguments){
        super(e);
        this.code = code;
        this.messageTemplate = "{0} {1} {2} {3} {4} {5}";
        for(Object arg : arguments){
            if(arg==null)
                arg="";
        }
        this.arguments = arguments;
        this.parent = e;
    }

    public String getCode(){
        return code;
    }

    public String getMessageTemplate(){
        return messageTemplate;
    }

    public Object[] getArguments(){
        return arguments;
    }

    public Exception getParent() {
        return parent;
    }

    public void setParent(Exception e) {
        this.parent = e;
    }

    public String getText(){
        return java.text.MessageFormat.format(messageTemplate, arguments).replaceAll("\\{[0-9]\\}", "");
    }

    public String getTextTemplate(){
        return messageTemplate.replaceAll("\\{[0-9]\\}", "");
    }

    public String getTemplatedText(String template){
        return java.text.MessageFormat.format(template, arguments).replaceAll("\\{[0-9]\\}", "");
    }

    public static String getText(String text, Object... arguments){
        return java.text.MessageFormat.format(text, arguments).replaceAll("\\{[0-9]\\}", "");
    }

    @Override
    public String toString() {
        return "LogicException ["
                + (code != null ? "code=" + code + ", " : "")
                + (messageTemplate != null ? "messageTemplate="
                + messageTemplate + ", " : "")
                + (arguments != null ? "arguments="
                + Arrays.toString(arguments) : "") + "Message= "+getText()+"]";
    }
}
