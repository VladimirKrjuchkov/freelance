package com.pb.tel.service.exception;

/**
 * Created by vladimir on 07.03.18.
 */
public class TelegramException extends Exception {

    private static final long serialVersionUID = -3155559670065775765L;

    private String code;
    private String description;
    private Integer userId;

    public TelegramException(String code, String description, Integer userId){
        this.code = code;
        this.description = description;
        this.userId = userId;
    }

    public TelegramException(String description, Integer userId){
        this.description = description;
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public Integer getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "TelegramException{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                '}';
    }
}
