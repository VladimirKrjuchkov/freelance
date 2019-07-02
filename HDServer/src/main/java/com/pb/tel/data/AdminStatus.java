package com.pb.tel.data;

/**
 * Created by vladimir on 02.07.19.
 */
public enum AdminStatus {
    FREE ("свободен"),
    BUSY ("занят");

    private String desc;

    AdminStatus(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
