package com.pb.tel.data.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by vladimir on 02.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class User {

    public User(){};

    private Integer id;

    private Boolean is_bot;

    private String first_name;

    private String last_name;

    private String language_code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (is_bot != null ? !is_bot.equals(user.is_bot) : user.is_bot != null) return false;
        if (first_name != null ? !first_name.equals(user.first_name) : user.first_name != null) return false;
        if (last_name != null ? !last_name.equals(user.last_name) : user.last_name != null) return false;
        return language_code != null ? language_code.equals(user.language_code) : user.language_code == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (is_bot != null ? is_bot.hashCode() : 0);
        result = 31 * result + (first_name != null ? first_name.hashCode() : 0);
        result = 31 * result + (last_name != null ? last_name.hashCode() : 0);
        result = 31 * result + (language_code != null ? language_code.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", is_bot=" + is_bot +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", language_code='" + language_code + '\'' +
                '}';
    }
}
