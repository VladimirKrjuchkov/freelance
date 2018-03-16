package com.pb.tel.data.enums;

/**
 * Created by vladimir on 05.03.18.
 */
public enum UserState {
    NEW("NEW", "начал беседу"),
    WAITING_PRESS_BUTTON("WAITING_PRESS_BUTTON", "бот ждет нажатия на кнопку"),
    WAITING_TTN("WAITING_TTN", "бот ждет ТТН"),
    WAITING_OPER("WAITING_OPER", "клиент ждет соеденения с оператором"),
    WRONG_ANSWER("WRONG_ANSWER", "неправильный ответ пользователя");

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
