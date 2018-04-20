package com.pb.tel.data.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.ws.rs.POST;

/**
 * Created by vladimir on 20.04.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Postback {

    public Postback(){};

    private String payload;

    private String title;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Postback postback = (Postback) o;

        if (payload != null ? !payload.equals(postback.payload) : postback.payload != null) return false;
        return title != null ? title.equals(postback.title) : postback.title == null;
    }

    @Override
    public int hashCode() {
        int result = payload != null ? payload.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Postback{" +
                "payload='" + payload + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
