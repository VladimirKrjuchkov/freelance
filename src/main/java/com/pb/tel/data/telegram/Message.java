package com.pb.tel.data.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by vladimir on 02.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class Message{

    public Message(){}

    private Integer message_id;

    private User from;

    private Chat chat;

    private Integer date;

    private String text;

    private Contact contact;

    private List<InputFile> photo;

    private InputFile document;

    private String file_id;

    private Integer file_size;

    private String file_path;

    private String caption;

    public Integer getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Integer message_id) {
        this.message_id = message_id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<InputFile> getPhoto() {
        return photo;
    }

    public void setPhoto(List<InputFile> photo) {
        this.photo = photo;
    }

    public InputFile getDocument() {
        return document;
    }

    public void setDocument(InputFile document) {
        this.document = document;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public Integer getFile_size() {
        return file_size;
    }

    public void setFile_size(Integer file_size) {
        this.file_size = file_size;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (message_id != null ? !message_id.equals(message.message_id) : message.message_id != null) return false;
        if (from != null ? !from.equals(message.from) : message.from != null) return false;
        if (chat != null ? !chat.equals(message.chat) : message.chat != null) return false;
        if (date != null ? !date.equals(message.date) : message.date != null) return false;
        if (text != null ? !text.equals(message.text) : message.text != null) return false;
        if (contact != null ? !contact.equals(message.contact) : message.contact != null) return false;
        if (photo != null ? !photo.equals(message.photo) : message.photo != null) return false;
        if (document != null ? !document.equals(message.document) : message.document != null) return false;
        if (file_id != null ? !file_id.equals(message.file_id) : message.file_id != null) return false;
        if (file_size != null ? !file_size.equals(message.file_size) : message.file_size != null) return false;
        if (file_path != null ? !file_path.equals(message.file_path) : message.file_path != null) return false;
        return caption != null ? caption.equals(message.caption) : message.caption == null;
    }

    @Override
    public int hashCode() {
        int result = message_id != null ? message_id.hashCode() : 0;
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (chat != null ? chat.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        result = 31 * result + (document != null ? document.hashCode() : 0);
        result = 31 * result + (file_id != null ? file_id.hashCode() : 0);
        result = 31 * result + (file_size != null ? file_size.hashCode() : 0);
        result = 31 * result + (file_path != null ? file_path.hashCode() : 0);
        result = 31 * result + (caption != null ? caption.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message_id=" + message_id +
                ", from=" + from +
                ", chat=" + chat +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", contact=" + contact +
                ", photo=" + photo +
                ", document=" + document +
                ", file_id='" + file_id + '\'' +
                ", file_size=" + file_size +
                ", file_path='" + file_path + '\'' +
                ", caption='" + caption + '\'' +
                '}';
    }
}
