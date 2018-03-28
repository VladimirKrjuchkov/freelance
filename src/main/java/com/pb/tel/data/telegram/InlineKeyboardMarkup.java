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
public class InlineKeyboardMarkup implements KeyboardMarkup{

    public InlineKeyboardMarkup(){};

    List<List<InlineKeyboardButton>> inline_keyboard;

    List<List<KeyboardButton>> keyboard;



    Boolean resize_keyboard = true;

    Boolean one_time_keyboard = true;

    public List<List<InlineKeyboardButton>> getInline_keyboard() {
        return inline_keyboard;
    }

    public void setInline_keyboard(List<List<InlineKeyboardButton>> inline_keyboard) {
        this.inline_keyboard = inline_keyboard;
    }

    public List<List<KeyboardButton>> getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(List<List<KeyboardButton>> keyboard) {
        this.keyboard = keyboard;
    }

    public Boolean getResize_keyboard() {
        return resize_keyboard;
    }

    public void setResize_keyboard(Boolean resize_keyboard) {
        this.resize_keyboard = resize_keyboard;
    }

    public Boolean getOne_time_keyboard() {
        return one_time_keyboard;
    }

    public void setOne_time_keyboard(Boolean one_time_keyboard) {
        this.one_time_keyboard = one_time_keyboard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InlineKeyboardMarkup that = (InlineKeyboardMarkup) o;

        if (inline_keyboard != null ? !inline_keyboard.equals(that.inline_keyboard) : that.inline_keyboard != null)
            return false;
        if (keyboard != null ? !keyboard.equals(that.keyboard) : that.keyboard != null) return false;
        if (resize_keyboard != null ? !resize_keyboard.equals(that.resize_keyboard) : that.resize_keyboard != null)
            return false;
        return one_time_keyboard != null ? one_time_keyboard.equals(that.one_time_keyboard) : that.one_time_keyboard == null;
    }

    @Override
    public int hashCode() {
        int result = inline_keyboard != null ? inline_keyboard.hashCode() : 0;
        result = 31 * result + (keyboard != null ? keyboard.hashCode() : 0);
        result = 31 * result + (resize_keyboard != null ? resize_keyboard.hashCode() : 0);
        result = 31 * result + (one_time_keyboard != null ? one_time_keyboard.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InlineKeyboardMarkup{" +
                "inline_keyboard=" + inline_keyboard +
                ", keyboard=" + keyboard +
                ", resize_keyboard=" + resize_keyboard +
                ", one_time_keyboard=" + one_time_keyboard +
                '}';
    }
}
