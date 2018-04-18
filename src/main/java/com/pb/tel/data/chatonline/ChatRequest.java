package com.pb.tel.data.chatonline;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pb.tel.data.Request;
import com.pb.tel.data.channels.ChannelsRequest;

/**
 * Created by vladimir on 18.04.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChatRequest implements Request{

    public ChatRequest(){};

    private String mark;

    private String action;

    private String channelId;

    private String sessionId;

    private Long sessionStartTime;

    private Long sessionEndTime;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(Long sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public Long getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionEndTime(Long sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatRequest that = (ChatRequest) o;

        if (mark != null ? !mark.equals(that.mark) : that.mark != null) return false;
        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        if (channelId != null ? !channelId.equals(that.channelId) : that.channelId != null) return false;
        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null) return false;
        if (sessionStartTime != null ? !sessionStartTime.equals(that.sessionStartTime) : that.sessionStartTime != null)
            return false;
        return sessionEndTime != null ? sessionEndTime.equals(that.sessionEndTime) : that.sessionEndTime == null;
    }

    @Override
    public int hashCode() {
        int result = mark != null ? mark.hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (channelId != null ? channelId.hashCode() : 0);
        result = 31 * result + (sessionId != null ? sessionId.hashCode() : 0);
        result = 31 * result + (sessionStartTime != null ? sessionStartTime.hashCode() : 0);
        result = 31 * result + (sessionEndTime != null ? sessionEndTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChatRequest{" +
                "mark='" + mark + '\'' +
                ", action='" + action + '\'' +
                ", channelId='" + channelId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", sessionStartTime=" + sessionStartTime +
                ", sessionEndTime=" + sessionEndTime +
                '}';
    }
}
