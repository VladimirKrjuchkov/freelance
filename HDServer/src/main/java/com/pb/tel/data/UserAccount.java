package com.pb.tel.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by vladimir on 05.07.19.
 */

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class UserAccount implements Serializable {

    public UserAccount(){}

    public UserAccount(String sessionId){
        this.sessionId = sessionId;
    }

    private static final long serialVersionUID = 1L;

    private String sessionId;

    private String operSession;

    private String operSocketId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getOperSession() {
        return operSession;
    }

    public void setOperSession(String operSession) {
        this.operSession = operSession;
    }

    public String getOperSocketId() {
        return operSocketId;
    }

    public void setOperSocketId(String operSocketId) {
        this.operSocketId = operSocketId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAccount that = (UserAccount) o;

        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null) return false;
        if (operSession != null ? !operSession.equals(that.operSession) : that.operSession != null) return false;
        return operSocketId != null ? operSocketId.equals(that.operSocketId) : that.operSocketId == null;
    }

    @Override
    public int hashCode() {
        int result = sessionId != null ? sessionId.hashCode() : 0;
        result = 31 * result + (operSession != null ? operSession.hashCode() : 0);
        result = 31 * result + (operSocketId != null ? operSocketId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "sessionId='" + sessionId + '\'' +
                ", operSession='" + operSession + '\'' +
                ", operSocketId='" + operSocketId + '\'' +
                '}';
    }
}
