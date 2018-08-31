package com.pb.tel.data.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 23.07.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Recipient {

    public Recipient(){}

    private Boolean send;

    private String id;

    private String type;

    public Boolean getSend() {
        return send;
    }

    public void setSend(Boolean send) {
        this.send = send;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipient recipient = (Recipient) o;

        if (send != null ? !send.equals(recipient.send) : recipient.send != null) return false;
        if (id != null ? !id.equals(recipient.id) : recipient.id != null) return false;
        return type != null ? type.equals(recipient.type) : recipient.type == null;
    }

    @Override
    public int hashCode() {
        int result = send != null ? send.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "send=" + send +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
