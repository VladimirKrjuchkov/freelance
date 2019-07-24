package com.pb.tel.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pb.tel.data.enumerators.Role;
import com.pb.tel.data.enumerators.Status;
import com.pb.tel.service.exception.LogicException;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by vladimir on 02.07.19.
 */

public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sessionId;

    private String login;

    private String password;

    private Status status;

    private HashMap<String, ConnectedAccount> connectedAccounts;

    private Role role;

    @JsonIgnore
    public String  getConnectedOperSessionId(){
        if(connectedAccounts != null) {
            return connectedAccounts.keySet().iterator().next();
        }else{
            return null;
        }
    }

    public void addConnectedAccount(ConnectedAccount connectedAccount) throws LogicException {
        if (this.connectedAccounts == null) {
            this.connectedAccounts = new HashMap<>();
        }
        if(this.role == Role.USER && connectedAccounts.keySet().size()>0) {
            throw new LogicException("EX001", "Нельзя добавлять более одного связанного аккаунта для роли USER");
        }
        connectedAccounts.put(connectedAccount.getSessionId(), connectedAccount);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public HashMap<String, ConnectedAccount> getConnectedAccounts() {
        if(connectedAccounts == null){
            connectedAccounts = new HashMap<>();
        }
        return connectedAccounts;
    }

    public void setConnectedAccounts(HashMap<String, ConnectedAccount> connectedAccounts) {
        this.connectedAccounts = connectedAccounts;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (sessionId != null ? !sessionId.equals(account.sessionId) : account.sessionId != null) return false;
        if (login != null ? !login.equals(account.login) : account.login != null) return false;
        if (password != null ? !password.equals(account.password) : account.password != null) return false;
        if (status != account.status) return false;
        if (connectedAccounts != null ? !connectedAccounts.equals(account.connectedAccounts) : account.connectedAccounts != null)
            return false;
        return role == account.role;
    }

    @Override
    public int hashCode() {
        int result = sessionId != null ? sessionId.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (connectedAccounts != null ? connectedAccounts.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "sessionId='" + sessionId + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", connectedAccounts=" + connectedAccounts +
                ", role=" + role +
                '}';
    }
}