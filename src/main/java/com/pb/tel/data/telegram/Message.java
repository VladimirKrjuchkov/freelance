package com.pb.tel.data.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by vladimir on 02.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class Message {

    public Message(){}

    private Integer message_id;

    private User from;

    private Chat chat;

    private Integer date;

    private String text;

    public Integer getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Integer message_id) {
        this.message_id = message_id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (message_id != null ? !message_id.equals(message.message_id) : message.message_id != null) return false;
        if (from != null ? !from.equals(message.from) : message.from != null) return false;
        if (chat != null ? !chat.equals(message.chat) : message.chat != null) return false;
        if (date != null ? !date.equals(message.date) : message.date != null) return false;
        return text != null ? text.equals(message.text) : message.text == null;
    }

    @Override
    public int hashCode() {
        int result = message_id != null ? message_id.hashCode() : 0;
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (chat != null ? chat.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message_id=" + message_id +
                ", from=" + from +
                ", chat=" + chat +
                ", date=" + date +
                ", text='" + text + '\'' +
                '}';
    }
}
