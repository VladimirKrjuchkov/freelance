package com.pb.tel.data.enumerators;

/**
 * Created by vladimir on 02.07.19.
 */
public enum AdminStatus {
    ONLINE ("ONLINE"),
    OFFLINE ("OFFLINE");

    private String desc;

    AdminStatus(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
