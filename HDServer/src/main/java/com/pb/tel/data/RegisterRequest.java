package com.pb.tel.data;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 02.07.19.
 */

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RegisterRequest {
    public RegisterRequest(){}

    public RegisterRequest(String login, String password){
        this.login = login;
        this.password = password;
    }

    String login;

    String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegisterRequest that = (RegisterRequest) o;

        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        return password != null ? password.equals(that.password) : that.password == null;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
