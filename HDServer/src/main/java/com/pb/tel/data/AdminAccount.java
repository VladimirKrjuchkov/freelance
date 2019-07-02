package com.pb.tel.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by vladimir on 02.07.19.
 */

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class AdminAccount implements Serializable {

    public AdminAccount(String sessionId){
        this.sessionId = sessionId;
    }

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(AdminAccount.class.getCanonicalName());

    private String sessionId;

    private List<String> clients;

    boolean online = false;

    AdminStatus adminStatus = AdminStatus.FREE;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<String> getClients() {
        return clients;
    }

    public void setClients(List<String> clients) {
        this.clients = clients;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminAccount that = (AdminAccount) o;

        if (online != that.online) return false;
        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null) return false;
        return clients != null ? clients.equals(that.clients) : that.clients == null;
    }

    @Override
    public int hashCode() {
        int result = sessionId != null ? sessionId.hashCode() : 0;
        result = 31 * result + (clients != null ? clients.hashCode() : 0);
        result = 31 * result + (online ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AdminAccount{" +
                "sessionId='" + sessionId + '\'' +
                ", clients=" + clients +
                ", online=" + online +
                '}';
    }
}