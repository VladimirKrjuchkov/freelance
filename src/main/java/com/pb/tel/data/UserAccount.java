package com.pb.tel.data;

import com.pb.tel.data.enums.Locale;
import com.pb.tel.data.enums.MessageContent;
import com.pb.tel.data.enums.UserState;

import java.util.logging.Logger;

/**
 * Created by vladimir on 15.03.18.
 */
public class UserAccount {

    public UserAccount(){}

    public UserAccount(String id){
        this.id = id;
    }

    private String id;

    private String token;

    private String ssoToken;

    private String firstName;

    private String lastName;

    private String userName;

    private String callBackData;

    private String udid;

    private String reqId;

    private String userText;

    private UserState userState = UserState.NEW;

    private String operId;

    private String operName;

    private String channelId;

    private Boolean registered = false;

    private String phone;

    private String idEkb = null;

    private String messenger;

    private String mark;

    private Long sessionStartTime;

    private Long sessionEndTime;

    private String sessionId;

    private String contactId;

    private Locale locale;

    private File file;

    private MessageContent messageContent;

    private String mode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
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

    public Boolean getRegistered() {
        return registered;
    }

    public void setRegistered(Boolean registered) {
        this.registered = registered;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdEkb() {
        return idEkb;
    }

    public void setIdEkb(String idEkb) {
        this.idEkb = idEkb;
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public MessageContent getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(MessageContent messageContent) {
        this.messageContent = messageContent;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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
        if (channelId != null ? !channelId.equals(that.channelId) : that.channelId != null) return false;
        if (registered != null ? !registered.equals(that.registered) : that.registered != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (idEkb != null ? !idEkb.equals(that.idEkb) : that.idEkb != null) return false;
        if (messenger != null ? !messenger.equals(that.messenger) : that.messenger != null) return false;
        if (mark != null ? !mark.equals(that.mark) : that.mark != null) return false;
        if (sessionStartTime != null ? !sessionStartTime.equals(that.sessionStartTime) : that.sessionStartTime != null)
            return false;
        if (sessionEndTime != null ? !sessionEndTime.equals(that.sessionEndTime) : that.sessionEndTime != null)
            return false;
        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null) return false;
        if (contactId != null ? !contactId.equals(that.contactId) : that.contactId != null) return false;
        if (locale != that.locale) return false;
        if (file != null ? !file.equals(that.file) : that.file != null) return false;
        if (messageContent != that.messageContent) return false;
        return mode != null ? mode.equals(that.mode) : that.mode == null;
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
        result = 31 * result + (registered != null ? registered.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (idEkb != null ? idEkb.hashCode() : 0);
        result = 31 * result + (messenger != null ? messenger.hashCode() : 0);
        result = 31 * result + (mark != null ? mark.hashCode() : 0);
        result = 31 * result + (sessionStartTime != null ? sessionStartTime.hashCode() : 0);
        result = 31 * result + (sessionEndTime != null ? sessionEndTime.hashCode() : 0);
        result = 31 * result + (sessionId != null ? sessionId.hashCode() : 0);
        result = 31 * result + (contactId != null ? contactId.hashCode() : 0);
        result = 31 * result + (locale != null ? locale.hashCode() : 0);
        result = 31 * result + (file != null ? file.hashCode() : 0);
        result = 31 * result + (messageContent != null ? messageContent.hashCode() : 0);
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", ssoToken='" + ssoToken + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", callBackData='" + callBackData + '\'' +
                ", udid='" + udid + '\'' +
                ", reqId='" + reqId + '\'' +
                ", userText='" + userText + '\'' +
                ", userState=" + userState +
                ", operId='" + operId + '\'' +
                ", operName='" + operName + '\'' +
                ", channelId='" + channelId + '\'' +
                ", registered=" + registered +
                ", phone='" + phone + '\'' +
                ", idEkb='" + idEkb + '\'' +
                ", messenger='" + messenger + '\'' +
                ", mark='" + mark + '\'' +
                ", sessionStartTime=" + sessionStartTime +
                ", sessionEndTime=" + sessionEndTime +
                ", sessionId='" + sessionId + '\'' +
                ", contactId='" + contactId + '\'' +
                ", locale=" + locale +
                ", file=" + file +
                ", messageContent=" + messageContent +
                ", mode='" + mode + '\'' +
                '}';
    }
}
