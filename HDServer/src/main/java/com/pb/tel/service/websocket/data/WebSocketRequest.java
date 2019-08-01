/*
 * Copyright (c) 2017. iDoc LLC
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     (1) Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *     (2) Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 *     (3)The name of the author may not be used to
 *     endorse or promote products derived from this software without
 *     specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.pb.tel.service.websocket.data;

import com.pb.tel.data.UserAccount;
import com.pb.tel.data.enumerators.RequestType;

import java.io.Serializable;


public class WebSocketRequest implements Serializable{

    public WebSocketRequest(){}

    public WebSocketRequest(String message){
        this.message = message;
    }

    public WebSocketRequest(boolean ok){
        this.ok = ok;
    };

    public WebSocketRequest(boolean ok, String message){
        this.ok = ok;
        this.message = message;
    }

    public WebSocketRequest(boolean ok, String message, UserAccount account){
        this.ok = ok;
        this.message = message;
        this.userAccount = userAccount;
    }

    public WebSocketRequest(boolean ok, UserAccount userAccount){
        this.ok = ok;
        this.userAccount = userAccount;
    }

    private boolean ok;

    private String message;

    private long sessionExp;

    private UserAccount userAccount;

    private RequestType requestType;

    private String userSessionId;

    public static WebSocketRequest getErrorRequest(String message){
        return new WebSocketRequest(false, message);
    }

    public static WebSocketRequest getSuccessRequest(String message){
        return new WebSocketRequest(true, message);
    }

    public static WebSocketRequest getSuccessRequest(String message, UserAccount userAccount){
        return new WebSocketRequest(true, message, userAccount);
    }

    public static WebSocketRequest getSuccessRequest(UserAccount userAccount){
        return new WebSocketRequest(true, userAccount);
    }

    public static WebSocketRequest getSuccessRequest(){
        return new WebSocketRequest(true);
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSessionExp() {
        return sessionExp;
    }

    public void setSessionExp(long sessionExp) {
        this.sessionExp = sessionExp;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getUserSessionId() {
        return userSessionId;
    }

    public void setUserSessionId(String userSessionId) {
        this.userSessionId = userSessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebSocketRequest that = (WebSocketRequest) o;

        if (ok != that.ok) return false;
        if (sessionExp != that.sessionExp) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (userAccount != null ? !userAccount.equals(that.userAccount) : that.userAccount != null) return false;
        if (requestType != that.requestType) return false;
        return userSessionId != null ? userSessionId.equals(that.userSessionId) : that.userSessionId == null;
    }

    @Override
    public int hashCode() {
        int result = (ok ? 1 : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (int) (sessionExp ^ (sessionExp >>> 32));
        result = 31 * result + (userAccount != null ? userAccount.hashCode() : 0);
        result = 31 * result + (requestType != null ? requestType.hashCode() : 0);
        result = 31 * result + (userSessionId != null ? userSessionId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WebSocketRequest{" +
                "ok=" + ok +
                ", message='" + message + '\'' +
                ", sessionExp=" + sessionExp +
                ", userAccount=" + userAccount +
                ", requestType=" + requestType +
                ", userSessionId='" + userSessionId + '\'' +
                '}';
    }
}
