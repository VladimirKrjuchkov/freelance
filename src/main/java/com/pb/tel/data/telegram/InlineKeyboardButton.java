package com.pb.tel.data.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 06.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InlineKeyboardButton {

    public InlineKeyboardButton(){};

    public InlineKeyboardButton(String text, String callback_data){
        this.text = text;
        this.callback_data = callback_data;
    };

    String text;

    String callback_data;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCallback_data() {
        return callback_data;
    }

    public void setCallback_data(String callback_data) {
        this.callback_data = callback_data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InlineKeyboardButton that = (InlineKeyboardButton) o;

        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        return callback_data != null ? callback_data.equals(that.callback_data) : that.callback_data == null;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (callback_data != null ? callback_data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InlineKeyboardButton{" +
                "text='" + text + '\'' +
                ", callback_data='" + callback_data + '\'' +
                '}';
    }
}
