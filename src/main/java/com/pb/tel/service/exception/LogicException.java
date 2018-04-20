package com.pb.tel.service.exception;

/**
 * Created by vladimir on 20.04.18.
 */
public class LogicException extends Exception{

    private static final long serialVersionUID = -3155559670065775765L;

    private String code;

    private String description;

    public LogicException(){}

    public LogicException(String code, String description){
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "LogicException{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
