package com.pb.tel.data.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by vladimir on 12.04.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Message {

    public Message(){};

    private String mid;

    private String seq;

    private String text;

    private Attachment attachment;

    private List<Attachment> attachments;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (mid != null ? !mid.equals(message.mid) : message.mid != null) return false;
        if (seq != null ? !seq.equals(message.seq) : message.seq != null) return false;
        if (text != null ? !text.equals(message.text) : message.text != null) return false;
        if (attachment != null ? !attachment.equals(message.attachment) : message.attachment != null) return false;
        return attachments != null ? attachments.equals(message.attachments) : message.attachments == null;
    }

    @Override
    public int hashCode() {
        int result = mid != null ? mid.hashCode() : 0;
        result = 31 * result + (seq != null ? seq.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (attachment != null ? attachment.hashCode() : 0);
        result = 31 * result + (attachments != null ? attachments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mid='" + mid + '\'' +
                ", seq='" + seq + '\'' +
                ", text='" + text + '\'' +
                ", attachment=" + attachment +
                ", attachments=" + attachments +
                '}';
    }
}
