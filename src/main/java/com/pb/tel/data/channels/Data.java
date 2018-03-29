package com.pb.tel.data.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by vladimir on 22.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Data {

    public Data(){};

    private User user;

    private String token;

    private Long time;

    private List<Operator> operators;

    private String channelId;

    private List<User> users;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public void setOperators(List<Operator> operators) {
        this.operators = operators;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Data data = (Data) o;

        if (user != null ? !user.equals(data.user) : data.user != null) return false;
        if (token != null ? !token.equals(data.token) : data.token != null) return false;
        if (time != null ? !time.equals(data.time) : data.time != null) return false;
        if (operators != null ? !operators.equals(data.operators) : data.operators != null) return false;
        if (channelId != null ? !channelId.equals(data.channelId) : data.channelId != null) return false;
        return users != null ? users.equals(data.users) : data.users == null;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (operators != null ? operators.hashCode() : 0);
        result = 31 * result + (channelId != null ? channelId.hashCode() : 0);
        result = 31 * result + (users != null ? users.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Data{" +
                "user=" + user +
                ", token='" + token + '\'' +
                ", time=" + time +
                ", operators=" + operators +
                ", channelId='" + channelId + '\'' +
                ", users=" + users +
                '}';
    }
}
