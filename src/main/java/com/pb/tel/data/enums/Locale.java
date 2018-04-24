package com.pb.tel.data.enums;

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
        for(Locale locale : Locale.values()){
            if(locale.code.equalsIgnoreCase(code))
                return locale;
        }
        return null;
    }
}
