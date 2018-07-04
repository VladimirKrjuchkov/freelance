package com.pb.tel.data.enums;

import com.pb.util.zvv.PropertiesUtil;

import java.util.Arrays;

/**
 * Created by vladimir on 13.03.18.
 */
public enum TelegramButtons {
    tracking("tracking", PropertiesUtil.getProperty("tracking")),
    myPost("myPost", PropertiesUtil.getProperty("myPost")),
    adviceButton("adviceButton", PropertiesUtil.getProperty("adviceButton")),
    register("register", PropertiesUtil.getProperty("register")),
    yes("yes", PropertiesUtil.getProperty("yes")),
    no("no", PropertiesUtil.getProperty("no")),
    ua("ua", PropertiesUtil.getProperty("ua")),
    ru("ru", PropertiesUtil.getProperty("ru")),
    callOper("callOper", PropertiesUtil.getProperty("callOper"));

    String code;
    String button;

    TelegramButtons(String code, String button){
        this.code = code;
        this.button = button;
    }

    public String getCode() {
        return code;
    }

    public String getButton() {
        return button;
    }

    public static TelegramButtons getByCode(String code){
        return Arrays.stream(TelegramButtons.values()).filter(telegramButtons -> telegramButtons.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
    }
}
