package com.pb.tel.data.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by vladimir on 20.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChannelsRequest {

    public ChannelsRequest(){};

    private String action;

    private String reqId;

    private Create data;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public Create getData() {
        return data;
    }

    public void setData(Create data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChannelsRequest that = (ChannelsRequest) o;

        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        if (reqId != null ? !reqId.equals(that.reqId) : that.reqId != null) return false;
        return data != null ? data.equals(that.data) : that.data == null;
    }

    @Override
    public int hashCode() {
        int result = action != null ? action.hashCode() : 0;
        result = 31 * result + (reqId != null ? reqId.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChannelsRequest{" +
                "action='" + action + '\'' +
                ", reqId='" + reqId + '\'' +
                ", data=" + data +
                '}';
    }
}
