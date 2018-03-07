package com.pb.tel.data.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pb.tel.data.enums.ChatType;

/**
 * Created by vladimir on 02.03.18.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Chat extends TelegramUser{

    public Chat(){};

    private String first_name;

    private String last_name;

    private String type;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getCall_back_data() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        if (first_name != null ? !first_name.equals(chat.first_name) : chat.first_name != null) return false;
        if (last_name != null ? !last_name.equals(chat.last_name) : chat.last_name != null) return false;
        return type != null ? type.equals(chat.type) : chat.type == null;
    }

    @Override
    public int hashCode() {
        int result = first_name != null ? first_name.hashCode() : 0;
        result = 31 * result + (last_name != null ? last_name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
