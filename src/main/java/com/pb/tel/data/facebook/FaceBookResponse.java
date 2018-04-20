package com.pb.tel.data.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pb.tel.data.Response;

/**
 * Created by vladimir on 16.04.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FaceBookResponse implements Response {

    public FaceBookResponse(){};

    private String recipient_id;

    private String message_id;

    public String getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FaceBookResponse that = (FaceBookResponse) o;

        if (recipient_id != null ? !recipient_id.equals(that.recipient_id) : that.recipient_id != null) return false;
        return message_id != null ? message_id.equals(that.message_id) : that.message_id == null;
    }

    @Override
    public int hashCode() {
        int result = recipient_id != null ? recipient_id.hashCode() : 0;
        result = 31 * result + (message_id != null ? message_id.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FaceBookResponse{" +
                "recipient_id='" + recipient_id + '\'' +
                ", message_id='" + message_id + '\'' +
                '}';
    }
}
