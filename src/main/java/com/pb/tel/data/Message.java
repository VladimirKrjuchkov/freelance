package com.pb.tel.data;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 01.03.18.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Message {
    public Message(){}

    public Message(String state, String description){
        this.state = state;
        this.code = code;
        this.description = description;
    }

    public Message(String state, String code, String description){
        this.state = state;
        this.code = code;
        this.description = description;
    }

    String state;

    String code;

    String description;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (state != null ? !state.equals(message.state) : message.state != null) return false;
        if (code != null ? !code.equals(message.code) : message.code != null) return false;
        return description != null ? description.equals(message.description) : message.description == null;
    }

    @Override
    public int hashCode() {
        int result = state != null ? state.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "state='" + state + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
