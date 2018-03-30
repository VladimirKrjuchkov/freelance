package com.pb.tel.data.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 28.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class KeyboardButton {

    public KeyboardButton(){}

    public KeyboardButton(String text){
        this.text = text;
    }

    String text;

    Boolean request_contact;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getRequest_contact() {
        return request_contact;
    }

    public void setRequest_contact(Boolean request_contact) {
        this.request_contact = request_contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyboardButton that = (KeyboardButton) o;

        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        return request_contact != null ? request_contact.equals(that.request_contact) : that.request_contact == null;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (request_contact != null ? request_contact.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "KeyboardButton{" +
                "text='" + text + '\'' +
                ", request_contact=" + request_contact +
                '}';
    }
}
