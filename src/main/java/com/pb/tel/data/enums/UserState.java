package com.pb.tel.data.enums;

/**
 * Created by vladimir on 05.03.18.
 */
public enum UserState {
    NEW("NEW", "начал беседу"),
    WAITING_PRESS_BUTTON("WAITING_PRESS_BUTTON", "бот ждет нажатия на кнопку"),
    WAITING_TTN("WAITING_TTN", "бот ждет ТТН"),
    WAITING_OPER("WAITING_OPER", "клиент ждет соеденения с оператором"),
    JOIN_TO_DIALOG("JOIN_TO_DIALOG", "клиента соеденили с оператором"),
    ANONIM_USER("ANONIM_USER", "клиент не желает давать свои данные"),
    WAITING_SHARE_CONTACT("WAITING_SHARE_CONTACT", "бот ждет, расшаривания контакта"),
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
