package com.pb.tel.data;

import com.pb.tel.data.enums.UserState;

import java.util.logging.Logger;

/**
 * Created by vladimir on 15.03.18.
 */
public class UserAccount {

    public UserAccount(){}

    public UserAccount(Integer id){
        this.id = id;
    }

    private Integer id;

    private String token;

    private String ssoToken;

    private String firstName;

    private String lastName;

    private String userName;

    private String callBackData;

    private Integer udid;

    private Integer reqId;

    private String userText;

    private UserState userState = UserState.NEW;

    private String operId;

    private String operName;

    private String channelId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(String ssoToken) {
        this.ssoToken = ssoToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCallBackData() {
        return callBackData;
    }

    public void setCallBackData(String callBackData) {
        this.callBackData = callBackData;
    }

    public Integer getUdid() {
        return udid;
    }

    public void setUdid(Integer udid) {
        this.udid = udid;
    }

    public Integer getReqId() {
        return reqId;
    }

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAccount that = (UserAccount) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        if (ssoToken != null ? !ssoToken.equals(that.ssoToken) : that.ssoToken != null) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (callBackData != null ? !callBackData.equals(that.callBackData) : that.callBackData != null) return false;
        if (udid != null ? !udid.equals(that.udid) : that.udid != null) return false;
        if (reqId != null ? !reqId.equals(that.reqId) : that.reqId != null) return false;
        if (userText != null ? !userText.equals(that.userText) : that.userText != null) return false;
        if (userState != that.userState) return false;
        if (operId != null ? !operId.equals(that.operId) : that.operId != null) return false;
        if (operName != null ? !operName.equals(that.operName) : that.operName != null) return false;
        return channelId != null ? channelId.equals(that.channelId) : that.channelId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (ssoToken != null ? ssoToken.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (callBackData != null ? callBackData.hashCode() : 0);
        result = 31 * result + (udid != null ? udid.hashCode() : 0);
        result = 31 * result + (reqId != null ? reqId.hashCode() : 0);
        result = 31 * result + (userText != null ? userText.hashCode() : 0);
        result = 31 * result + (userState != null ? userState.hashCode() : 0);
        result = 31 * result + (operId != null ? operId.hashCode() : 0);
        result = 31 * result + (operName != null ? operName.hashCode() : 0);
        result = 31 * result + (channelId != null ? channelId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", ssoToken='" + ssoToken + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", callBackData='" + callBackData + '\'' +
                ", udid=" + udid +
                ", reqId=" + reqId +
                ", userText='" + userText + '\'' +
                ", userState=" + userState +
                ", operId='" + operId + '\'' +
                ", operName='" + operName + '\'' +
                ", channelId='" + channelId + '\'' +
                '}';
    }
}
