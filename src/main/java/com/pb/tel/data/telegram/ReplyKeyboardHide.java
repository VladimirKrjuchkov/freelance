package com.pb.tel.data.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 28.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReplyKeyboardHide implements KeyboardMarkup{

    public ReplyKeyboardHide(){};

    private Boolean hide_keyboard = true;

    public Boolean getHide_keyboard() {
        return hide_keyboard;
    }

    public void setHide_keyboard(Boolean hide_keyboard) {
        this.hide_keyboard = hide_keyboard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReplyKeyboardHide that = (ReplyKeyboardHide) o;

        return hide_keyboard != null ? hide_keyboard.equals(that.hide_keyboard) : that.hide_keyboard == null;
    }

    @Override
    public int hashCode() {
        return hide_keyboard != null ? hide_keyboard.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ReplyKeyboardHide{" +
                "hide_keyboard=" + hide_keyboard +
                '}';
    }
}
