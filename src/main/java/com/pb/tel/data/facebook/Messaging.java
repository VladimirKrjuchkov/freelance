package com.pb.tel.data.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pb.tel.data.Request;

import java.util.Date;

/**
 * Created by vladimir on 12.04.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Messaging implements Request{

    public Messaging(){};

    private String messaging_type;

    private Participant sender;

    private Participant recipient;

    private Date timestamp;

    private Message message;

    private Postback postback;

    public String getMessaging_type() {
        return messaging_type;
    }

    public void setMessaging_type(String messaging_type) {
        this.messaging_type = messaging_type;
    }

    public Participant getSender() {
        return sender;
    }

    public void setSender(Participant sender) {
        this.sender = sender;
    }

    public Participant getRecipient() {
        return recipient;
    }

    public void setRecipient(Participant recipient) {
        this.recipient = recipient;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Postback getPostback() {
        return postback;
    }

    public void setPostback(Postback postback) {
        this.postback = postback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Messaging messaging = (Messaging) o;

        if (messaging_type != null ? !messaging_type.equals(messaging.messaging_type) : messaging.messaging_type != null)
            return false;
        if (sender != null ? !sender.equals(messaging.sender) : messaging.sender != null) return false;
        if (recipient != null ? !recipient.equals(messaging.recipient) : messaging.recipient != null) return false;
        if (timestamp != null ? !timestamp.equals(messaging.timestamp) : messaging.timestamp != null) return false;
        if (message != null ? !message.equals(messaging.message) : messaging.message != null) return false;
        return postback != null ? postback.equals(messaging.postback) : messaging.postback == null;
    }

    @Override
    public int hashCode() {
        int result = messaging_type != null ? messaging_type.hashCode() : 0;
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (recipient != null ? recipient.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (postback != null ? postback.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Messaging{" +
                "messaging_type='" + messaging_type + '\'' +
                ", sender=" + sender +
                ", recipient=" + recipient +
                ", timestamp=" + timestamp +
                ", message=" + message +
                ", postback=" + postback +
                '}';
    }
}
