package com.pb.tel.service.exception;

/**
 * Created by vladimir on 07.03.18.
 */
public class UnresponsibleException extends Exception{

    private static final long serialVersionUID = -3155559670065775765L;

    private String code;

    private String description;

    public UnresponsibleException(String code, String description){
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
        return "UnresponsibleException{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
