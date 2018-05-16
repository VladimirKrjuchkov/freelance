package com.pb.tel.data.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by vladimir on 22.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Data{

    public Data(){};

    private User user;

    private String token;

    private Long time;

    private List<Operator> operators;

    private String channelId;

    private List<User> users;

    private String companyId;

    private String text;

    private String type;

    private List<String> invites;

    private String udid;

    private String ssoToken;

    private DeviceInfo deviceInfo;

    private String sessionId;

    private List<File> files;

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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getInvites() {
        return invites;
    }

    public void setInvites(List<String> invites) {
        this.invites = invites;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(String ssoToken) {
        this.ssoToken = ssoToken;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
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
        if (users != null ? !users.equals(data.users) : data.users != null) return false;
        if (companyId != null ? !companyId.equals(data.companyId) : data.companyId != null) return false;
        if (text != null ? !text.equals(data.text) : data.text != null) return false;
        if (type != null ? !type.equals(data.type) : data.type != null) return false;
        if (invites != null ? !invites.equals(data.invites) : data.invites != null) return false;
        if (udid != null ? !udid.equals(data.udid) : data.udid != null) return false;
        if (ssoToken != null ? !ssoToken.equals(data.ssoToken) : data.ssoToken != null) return false;
        if (deviceInfo != null ? !deviceInfo.equals(data.deviceInfo) : data.deviceInfo != null) return false;
        if (sessionId != null ? !sessionId.equals(data.sessionId) : data.sessionId != null) return false;
        return files != null ? files.equals(data.files) : data.files == null;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (operators != null ? operators.hashCode() : 0);
        result = 31 * result + (channelId != null ? channelId.hashCode() : 0);
        result = 31 * result + (users != null ? users.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (invites != null ? invites.hashCode() : 0);
        result = 31 * result + (udid != null ? udid.hashCode() : 0);
        result = 31 * result + (ssoToken != null ? ssoToken.hashCode() : 0);
        result = 31 * result + (deviceInfo != null ? deviceInfo.hashCode() : 0);
        result = 31 * result + (sessionId != null ? sessionId.hashCode() : 0);
        result = 31 * result + (files != null ? files.hashCode() : 0);
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
                ", companyId='" + companyId + '\'' +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", invites=" + invites +
                ", udid='" + udid + '\'' +
                ", ssoToken='" + ssoToken + '\'' +
                ", deviceInfo=" + deviceInfo +
                ", sessionId='" + sessionId + '\'' +
                ", files=" + files +
                '}';
    }
}
