package com.pb.tel.service.exception;

import com.pb.tel.data.telegram.InlineKeyboardMarkup;

/**
 * Created by vladimir on 07.03.18.
 */
public class TelegramException extends Exception {

    private static final long serialVersionUID = -3155559670065775765L;

    private String code;
    private String description;
    private Integer userId;
    private InlineKeyboardMarkup inlineKeyboardMarkup;

    public TelegramException(String code, String description, Integer userId){
        this.code = code;
        this.description = description;
        this.userId = userId;
    }

    public TelegramException(String description, Integer userId){
        this.description = description;
        this.userId = userId;
    }

    public TelegramException(String description, Integer userId, InlineKeyboardMarkup inlineKeyboardMarkup){
        this.description = description;
        this.userId = userId;
        this.inlineKeyboardMarkup = inlineKeyboardMarkup;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        return inlineKeyboardMarkup;
    }

    public void setInlineKeyboardMarkup(InlineKeyboardMarkup inlineKeyboardMarkup) {
        this.inlineKeyboardMarkup = inlineKeyboardMarkup;
    }

    @Override
    public String toString() {
        return "TelegramException{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                ", inlineKeyboardMarkup=" + inlineKeyboardMarkup +
                '}';
    }
}
