package com.pb.tel.data.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pb.tel.data.Request;

/**
 * Created by vladimir on 06.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TelegramRequest implements Request {

    public TelegramRequest(){}

    public TelegramRequest(String chat_id, String text){
        this.chat_id = chat_id;
        this.text = text;
    };

    String chat_id;

    String text;

    KeyboardMarkup reply_markup;

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public KeyboardMarkup getReply_markup() {
        return reply_markup;
    }

    public void setReply_markup(KeyboardMarkup reply_markup) {
        this.reply_markup = reply_markup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TelegramRequest that = (TelegramRequest) o;

        if (chat_id != null ? !chat_id.equals(that.chat_id) : that.chat_id != null) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        return reply_markup != null ? reply_markup.equals(that.reply_markup) : that.reply_markup == null;
    }

    @Override
    public int hashCode() {
        int result = chat_id != null ? chat_id.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (reply_markup != null ? reply_markup.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TelegramRequest{" +
                "chat_id='" + chat_id + '\'' +
                ", text='" + text + '\'' +
                ", reply_markup=" + reply_markup +
                '}';
    }
}
