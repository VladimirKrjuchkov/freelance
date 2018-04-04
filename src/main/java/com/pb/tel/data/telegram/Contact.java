package com.pb.tel.data.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by vladimir on 03.04.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class Contact {

    private String phone_number;

    private String first_name;

    private String last_name;

    private String user_id;

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (phone_number != null ? !phone_number.equals(contact.phone_number) : contact.phone_number != null)
            return false;
        if (first_name != null ? !first_name.equals(contact.first_name) : contact.first_name != null) return false;
        if (last_name != null ? !last_name.equals(contact.last_name) : contact.last_name != null) return false;
        return user_id != null ? user_id.equals(contact.user_id) : contact.user_id == null;
    }

    @Override
    public int hashCode() {
        int result = phone_number != null ? phone_number.hashCode() : 0;
        result = 31 * result + (first_name != null ? first_name.hashCode() : 0);
        result = 31 * result + (last_name != null ? last_name.hashCode() : 0);
        result = 31 * result + (user_id != null ? user_id.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "phone_number='" + phone_number + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
