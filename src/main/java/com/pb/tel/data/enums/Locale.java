package com.pb.tel.data.enums;

import java.util.Arrays;

/**
 * Created by vladimir on 24.04.18.
 */
public enum Locale {
    UA("ua"),
    RU("ru");

    String code;

    Locale(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }

    public static Locale getByCode(String code){
        return Arrays.stream(Locale.values()).filter(locale -> code.equalsIgnoreCase(locale.getCode())).findFirst().orElse(null);
    }

}
