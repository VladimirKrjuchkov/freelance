package com.pb.tel.service.exception;

/**
 * Created by vladimir on 29.03.18.
 */
public class ChannelsException extends Exception{

    private static final long serialVersionUID = -3155559670065775765L;

    private String code;
    private String description;
    private String channelId;
    private String reqId;
    private String token;

    public ChannelsException(String description, String channelId, String reqId, String token){
        this.description = description;
        this.channelId = channelId;
        this.reqId = reqId;
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChannelsException that = (ChannelsException) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (channelId != null ? !channelId.equals(that.channelId) : that.channelId != null) return false;
        if (reqId != null ? !reqId.equals(that.reqId) : that.reqId != null) return false;
        return token != null ? token.equals(that.token) : that.token == null;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (channelId != null ? channelId.hashCode() : 0);
        result = 31 * result + (reqId != null ? reqId.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChannelsException{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", channelId='" + channelId + '\'' +
                ", reqId='" + reqId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
