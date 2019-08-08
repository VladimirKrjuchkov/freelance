package com.pb.tel.data.enumerators;

/**
 * Created by vladimir on 03.07.19.
 */
public enum RequestType {
    CALL_OPER("CALL_OPER");

    private String desc;

    RequestType(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
