package com.pb.tel.data;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 02.03.18.
 */

public class Message {

    public Message(){}

    Integer message_id;

    User from;

    Chat chat;

    Integer date;

    String text;
}
