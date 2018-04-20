package com.pb.tel.data.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 17.04.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Buttons {

    public Buttons(){};

    private String type;

    private String title;

    private String payload;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Buttons buttons = (Buttons) o;

        if (type != null ? !type.equals(buttons.type) : buttons.type != null) return false;
        if (title != null ? !title.equals(buttons.title) : buttons.title != null) return false;
        return payload != null ? payload.equals(buttons.payload) : buttons.payload == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Buttons{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}
