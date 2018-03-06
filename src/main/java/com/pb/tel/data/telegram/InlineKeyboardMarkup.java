package com.pb.tel.data.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;
import java.util.List;

/**
 * Created by vladimir on 06.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InlineKeyboardMarkup {

    public InlineKeyboardMarkup(){};

    List<List<InlineKeyboardButton>> inline_keyboard;

    public List<List<InlineKeyboardButton>> getInline_keyboard() {
        return inline_keyboard;
    }

    public void setInline_keyboard(List<List<InlineKeyboardButton>> inline_keyboard) {
        this.inline_keyboard = inline_keyboard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InlineKeyboardMarkup that = (InlineKeyboardMarkup) o;

        return inline_keyboard != null ? inline_keyboard.equals(that.inline_keyboard) : that.inline_keyboard == null;
    }

    @Override
    public int hashCode() {
        return inline_keyboard != null ? inline_keyboard.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "InlineKeyboardMarkup{" +
                "inline_keyboard=" + inline_keyboard +
                '}';
    }
}
