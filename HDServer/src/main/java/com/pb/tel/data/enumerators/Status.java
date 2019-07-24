package com.pb.tel.data.enumerators;

/**
 * Created by vladimir on 02.07.19.
 */
public enum Status {
    ONLINE ("ONLINE"),
    OFFLINE ("OFFLINE"),
    CONNECTED("CONNECTED"),
    DISCONNECTED("DISCONNECTED");

    private String desc;

    Status(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
