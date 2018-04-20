package com.pb.tel.data.telegram;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by vladimir on 02.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class User extends TelegramUser{

    public User(){};

    private String id;

    private Boolean is_bot;

    private String first_name;

    private String last_name;

    private String language_code;

    private String username;

    @JsonIgnore
    private String call_back_data;

    @JsonIgnore
    private String text;

    @JsonIgnore
    private String bot_id;

    @JsonIgnore
    private String phone;

    @JsonIgnore
    private String messenger;

    @JsonIgnore
    private String contactId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIs_bot() {
        return is_bot;
    }

    public void setIs_bot(Boolean is_bot) {
        this.is_bot = is_bot;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCall_back_data() {
        return call_back_data;
    }

    public void setCall_back_data(String call_back_data) {
        this.call_back_data = call_back_data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBot_id() {
        return bot_id;
    }

    public void setBot_id(String bot_id) {
        this.bot_id = bot_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (is_bot != null ? !is_bot.equals(user.is_bot) : user.is_bot != null) return false;
        if (first_name != null ? !first_name.equals(user.first_name) : user.first_name != null) return false;
        if (last_name != null ? !last_name.equals(user.last_name) : user.last_name != null) return false;
        if (language_code != null ? !language_code.equals(user.language_code) : user.language_code != null)
            return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (call_back_data != null ? !call_back_data.equals(user.call_back_data) : user.call_back_data != null)
            return false;
        if (text != null ? !text.equals(user.text) : user.text != null) return false;
        if (bot_id != null ? !bot_id.equals(user.bot_id) : user.bot_id != null) return false;
        if (phone != null ? !phone.equals(user.phone) : user.phone != null) return false;
        if (messenger != null ? !messenger.equals(user.messenger) : user.messenger != null) return false;
        return contactId != null ? contactId.equals(user.contactId) : user.contactId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (is_bot != null ? is_bot.hashCode() : 0);
        result = 31 * result + (first_name != null ? first_name.hashCode() : 0);
        result = 31 * result + (last_name != null ? last_name.hashCode() : 0);
        result = 31 * result + (language_code != null ? language_code.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (call_back_data != null ? call_back_data.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (bot_id != null ? bot_id.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (messenger != null ? messenger.hashCode() : 0);
        result = 31 * result + (contactId != null ? contactId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", is_bot=" + is_bot +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", language_code='" + language_code + '\'' +
                ", username='" + username + '\'' +
                ", call_back_data='" + call_back_data + '\'' +
                ", text='" + text + '\'' +
                ", bot_id='" + bot_id + '\'' +
                ", phone='" + phone + '\'' +
                ", messenger='" + messenger + '\'' +
                ", contactId='" + contactId + '\'' +
                '}';
    }
}
