package com.pb.tel.data.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 20.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TokenCreate implements Create{

    public TokenCreate(){};

    private String udid;

    private String ssoToken;

    private DeviceInfo deviceInfo;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenCreate that = (TokenCreate) o;

        if (udid != null ? !udid.equals(that.udid) : that.udid != null) return false;
        if (ssoToken != null ? !ssoToken.equals(that.ssoToken) : that.ssoToken != null) return false;
        return deviceInfo != null ? deviceInfo.equals(that.deviceInfo) : that.deviceInfo == null;
    }

    @Override
    public int hashCode() {
        int result = udid != null ? udid.hashCode() : 0;
        result = 31 * result + (ssoToken != null ? ssoToken.hashCode() : 0);
        result = 31 * result + (deviceInfo != null ? deviceInfo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TokenCreate{" +
                "udid='" + udid + '\'' +
                ", ssoToken='" + ssoToken + '\'' +
                ", deviceInfo=" + deviceInfo +
                '}';
    }
}
