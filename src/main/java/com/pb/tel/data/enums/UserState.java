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
    LEAVING_DIALOG("LEAVING_DIALOG", "ченнелс ждет оценки"),
    USER_ANSWERD_UNKNOWN("USER_ANSWERD_UNKNOWN", "Юзер не дал ответа"),
    USER_ANSWERD_YES("USER_ANSWERD_YES", "Юзер дал положительный ответ"),
    USER_ANSWERD_NO("USER_ANSWERD_YES", "Юзер дал отрицательный ответ"),
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
