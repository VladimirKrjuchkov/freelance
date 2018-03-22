package com.pb.tel.data.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 22.03.18.
 */


@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChannelsResponse {

    public ChannelsResponse(){};

    private String result;

    private String reqId;

    private Data data;

    private String action;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChannelsResponse that = (ChannelsResponse) o;

        if (result != null ? !result.equals(that.result) : that.result != null) return false;
        if (reqId != null ? !reqId.equals(that.reqId) : that.reqId != null) return false;
        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        return action != null ? action.equals(that.action) : that.action == null;
    }

    @Override
    public int hashCode() {
        int result1 = result != null ? result.hashCode() : 0;
        result1 = 31 * result1 + (reqId != null ? reqId.hashCode() : 0);
        result1 = 31 * result1 + (data != null ? data.hashCode() : 0);
        result1 = 31 * result1 + (action != null ? action.hashCode() : 0);
        return result1;
    }

    @Override
    public String toString() {
        return "ChannelsResponse{" +
                "result='" + result + '\'' +
                ", reqId='" + reqId + '\'' +
                ", data=" + data +
                ", action='" + action + '\'' +
                '}';
    }
}
