package com.pb.tel.data.enums;

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
        for(MessageContent messageContent : MessageContent.values()){
            if(messageContent.descr.equalsIgnoreCase(descr))
                return messageContent;
        }
        return null;
    }
}
