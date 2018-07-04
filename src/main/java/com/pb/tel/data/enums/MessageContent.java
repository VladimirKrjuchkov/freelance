package com.pb.tel.data.enums;

import java.util.Arrays;

/**
 * Created by vladimir on 16.05.18.
 */
public enum MessageContent {
    PICTURE("picture"),
    DOCUMENT("document");

    String descr;

    MessageContent(String code){
        this.descr = code;
    }

    public String getDescr(){
        return descr;
    }

    public static MessageContent getByDescr(String descr){
        return Arrays.stream(MessageContent.values()).filter(messageContent -> messageContent.getDescr().equalsIgnoreCase(descr)).findFirst().orElse(null);
    }
}
