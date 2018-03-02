package com.pb.tel.data.enums;

/**
 * Created by vladimir on 02.03.18.
 */
public enum ChatType {
    Private("private"),
    Group("group"),
    Supergroup("supergroup"),
    Channel("channel");

    String name;

    ChatType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
