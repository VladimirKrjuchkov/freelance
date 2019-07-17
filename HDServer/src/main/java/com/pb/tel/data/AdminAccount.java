package com.pb.tel.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pb.tel.data.enumerators.AdminStatus;

import java.io.Serializable;
import java.util.ArrayList;
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

    private String login;

    private List<String> clients;

    AdminStatus adminStatus = AdminStatus.OFFLINE;

    private String socketId;

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

    public List<String> getClients() {
        if(clients == null){
            clients = new ArrayList<String>();
        }
        return clients;
    }

    public void setClients(List<String> clients) {
        this.clients = clients;
    }

    public AdminStatus getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(AdminStatus adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminAccount that = (AdminAccount) o;

        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (clients != null ? !clients.equals(that.clients) : that.clients != null) return false;
        if (adminStatus != that.adminStatus) return false;
        return socketId != null ? socketId.equals(that.socketId) : that.socketId == null;
    }

    @Override
    public int hashCode() {
        int result = sessionId != null ? sessionId.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (clients != null ? clients.hashCode() : 0);
        result = 31 * result + (adminStatus != null ? adminStatus.hashCode() : 0);
        result = 31 * result + (socketId != null ? socketId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AdminAccount{" +
                "sessionId='" + sessionId + '\'' +
                ", login='" + login + '\'' +
                ", clients=" + clients +
                ", adminStatus=" + adminStatus +
                ", socketId='" + socketId + '\'' +
                '}';
    }
}