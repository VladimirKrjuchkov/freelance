package com.pb.tel.data.enumerators;

/**
 * Created by vladimir on 03.07.19.
 */
public enum RequestType {
    CALL_OPER ("вызов оператора");

    private String desc;

    RequestType(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
