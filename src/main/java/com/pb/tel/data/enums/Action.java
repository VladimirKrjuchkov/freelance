package com.pb.tel.data.enums;

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
        for(Action action : Action.values()){
            if(action.code.equalsIgnoreCase(code))
                return action;
        }
        return null;
    }
}
