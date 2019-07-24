package com.pb.tel.data.enumerators;

/**
 * Created by vladimir on 22.07.19.
 */
public enum Role {
    OPERATOR("OPERATOR"),
    USER("USER");

    private String desc;

    Role(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
