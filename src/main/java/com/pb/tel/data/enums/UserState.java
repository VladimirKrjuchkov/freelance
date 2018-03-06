package com.pb.tel.data.enums;

/**
 * Created by vladimir on 05.03.18.
 */
public enum UserState {
    NEW("NEW", "начал беседу"),
    WAITING_PRESS_BUTTON("WAITING_PRESS_BUTTON", "бот ждет нажатия на кнопку");

    String code;
    String descr;

    UserState(String code, String descr){
        this.code = code;
        this.descr = descr;
    }

    public String getCode(){
        return code;
    };

    public String getDescr(){
        return descr;
    };

    public static UserState getByCode(String code){
        for(UserState userState : UserState.values()){
            if(userState.code.equalsIgnoreCase(code))
                return userState;
        }
        return null;
    }
}
