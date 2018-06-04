package com.pb.tel.data.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 02.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Update {

    public Update(){};

    private Integer update_id;

    Message message;

    CallbackQuery callback_query;

    InlineQuery inline_query;

    public Integer getUpdate_id() {
        return update_id;
    }

    public void setUpdate_id(Integer update_id) {
        this.update_id = update_id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public CallbackQuery getCallback_query() {
        return callback_query;
    }

    public void setCallback_query(CallbackQuery callback_query) {
        this.callback_query = callback_query;
    }

    public InlineQuery getInline_query() {
        return inline_query;
    }

    public void setInline_query(InlineQuery inline_query) {
        this.inline_query = inline_query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Update update = (Update) o;

        if (update_id != null ? !update_id.equals(update.update_id) : update.update_id != null) return false;
        if (message != null ? !message.equals(update.message) : update.message != null) return false;
        if (callback_query != null ? !callback_query.equals(update.callback_query) : update.callback_query != null)
            return false;
        return inline_query != null ? inline_query.equals(update.inline_query) : update.inline_query == null;
    }

    @Override
    public int hashCode() {
        int result = update_id != null ? update_id.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (callback_query != null ? callback_query.hashCode() : 0);
        result = 31 * result + (inline_query != null ? inline_query.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Update{" +
                "update_id=" + update_id +
                ", message=" + message +
                ", callback_query=" + callback_query +
                ", inline_query=" + inline_query +
                '}';
    }
}
