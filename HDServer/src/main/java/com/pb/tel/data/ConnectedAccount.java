package com.pb.tel.data;

import java.io.Serializable;

/**
 * Created by vladimir on 22.07.19.
 */
public class ConnectedAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sessionId;

    private String login;

    public ConnectedAccount(){}

    public ConnectedAccount(String sessionId){
        this.sessionId = sessionId;
    }

    public ConnectedAccount(String sessionId, String login){
        this.sessionId = sessionId;
        this.login = login;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConnectedAccount that = (ConnectedAccount) o;

        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null) return false;
        return login != null ? login.equals(that.login) : that.login == null;
    }

    @Override
    public int hashCode() {
        int result = sessionId != null ? sessionId.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConnectedAccount{" +
                "sessionId='" + sessionId + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
