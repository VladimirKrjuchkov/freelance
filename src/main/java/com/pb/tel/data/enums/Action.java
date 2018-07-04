package com.pb.tel.data.enums;

import java.util.Arrays;

/**
 * Created by vladimir on 11.06.18.
 */
public enum Action {

    newUser("newUser"),
    trackError("trackError"),
    chatOnline("chatOnline"),
    trackSuccess("trackSuccess");

    String code;

    Action(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }

    public static Action getByCode(String code){
        return Arrays.stream(Action.values()).filter(action -> code.equalsIgnoreCase(action.getCode())).findFirst().orElse(null);
    }
}
