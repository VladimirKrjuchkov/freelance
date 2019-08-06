package com.pb.tel.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pb.tel.data.enumerators.AuthType;
import com.pb.tel.data.enumerators.Role;
import com.pb.tel.data.enumerators.Status;
import com.pb.tel.service.auth.ClientDetails;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.utils.Utils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.*;

/**
 * Created by vladimir on 02.07.19.
 */

public class UserAccount implements Serializable {

    public UserAccount(){}

    public UserAccount(UserDetails user){
        this.user = user;
    }

    private static final long serialVersionUID = 1L;

    private String sessionId;

    private String userName;

    private String password;

    private Status status;

    private HashMap<String, ConnectedAccount> ConnectedAccounts;

    private Role role;

    private Mes mes;

    @JsonIgnore
    String storageKey;

    @JsonProperty
    private String autorizeUrl;

    @JsonIgnore
    private Collection<? extends GrantedAuthority> authority;

    @JsonProperty
    private String clientId;

    @XmlElement
    @JsonProperty
    private String workStationUserId;

    @JsonProperty
    private String sidBi;

    @JsonProperty
    private int maxInSecondPossibleSessionExpire;

    @JsonProperty
    private Date maxPossibleSessionExpire;

    @JsonProperty
    private AuthType authType;

    @JsonProperty
    private boolean registrationUser;

    @JsonProperty
    private String accessToken;

    @JsonProperty
    private Set<String> sessionAccessTokens;

    @JsonProperty
    private Date accessTokenExpire;

    @JsonProperty
    private Date refreshTokenExpire;

    @JsonProperty
    private String refreshToken;

    private UserDetails user;

    @JsonIgnore
    private OAuth2AccessToken oAuth2AccessToken;

    @JsonIgnore
    public String  getConnectedOperSessionId(){
        if(ConnectedAccounts != null) {
            return ConnectedAccounts.keySet().iterator().next();
        }else{
            return null;
        }
    }

    public void addConnectedAccount(ConnectedAccount connectedUserAccount) throws LogicException {
        if (this.ConnectedAccounts == null) {
            this.ConnectedAccounts = new HashMap<>();
        }
        if(this.role == Role.USER && ConnectedAccounts.keySet().size()>0) {
//            throw new LogicException("EX001", "Нельзя добавлять более одного связанного аккаунта для роли USER");
        }
        ConnectedAccounts.put(connectedUserAccount.getSessionId(), connectedUserAccount);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAutorizeUrl() {
        return autorizeUrl;
    }

    public void setAutorizeUrl(String autorizeUrl) {
        this.autorizeUrl = autorizeUrl;
    }

    public HashMap<String, ConnectedAccount> getConnectedAccounts() {
        if(ConnectedAccounts == null){
            ConnectedAccounts = new HashMap<>();
        }
        return ConnectedAccounts;
    }

    public void setConnectedAccounts(HashMap<String, ConnectedAccount> ConnectedAccounts) {
        this.ConnectedAccounts = ConnectedAccounts;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public Collection<? extends GrantedAuthority> getAuthority() {

        return authority;
    }

    public void setAuthority(Collection<? extends GrantedAuthority> authority) {
        this.authority = authority;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getWorkStationUserId() {
        return workStationUserId;
    }

    public void setWorkStationUserId(String workStationUserId) {
        this.workStationUserId = workStationUserId;
    }

    public String getSidBi() {
        return sidBi;
    }

    public void setSidBi(String sidBi) {
        this.sidBi = sidBi;
    }

    public int getMaxInSecondPossibleSessionExpire() {
        return maxInSecondPossibleSessionExpire;
    }

    public void setMaxInSecondPossibleSessionExpire(int maxInSecondPossibleSessionExpire) {
        this.maxInSecondPossibleSessionExpire = maxInSecondPossibleSessionExpire;
    }

    public Date getMaxPossibleSessionExpire() {
        return maxPossibleSessionExpire;
    }

    public void setMaxPossibleSessionExpire(Date maxPossibleSessionExpire) {
        this.maxPossibleSessionExpire = maxPossibleSessionExpire;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public boolean isRegistrationUser() {
        return registrationUser;
    }

    public void setRegistrationUser(boolean registrationUser) {
        this.registrationUser = registrationUser;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Set<String> getSessionAccessTokens() {
        return sessionAccessTokens;
    }

    public void setSessionAccessTokens(Set<String> sessionAccessTokens) {
        this.sessionAccessTokens = sessionAccessTokens;
    }

    public Date getAccessTokenExpire() {
        return accessTokenExpire;
    }

    public void setAccessTokenExpire(Date accessTokenExpire) {
        this.accessTokenExpire = accessTokenExpire;
    }

    public Date getRefreshTokenExpire() {
        return refreshTokenExpire;
    }

    public void setRefreshTokenExpire(Date refreshTokenExpire) {
        this.refreshTokenExpire = refreshTokenExpire;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserDetails getUser() {
        return user;
    }

    public void setUser(UserDetails user) {
        this.user = user;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }

    public OAuth2AccessToken getAccessTokenObject(){
        return oAuth2AccessToken;
    }

    public void setAccessTokenObject(OAuth2AccessToken token) {
        oAuth2AccessToken = token;
        this.accessToken = token.getValue();
        addSessionAccessToken(accessToken);
        setAccessTokenExpire(token.getExpiration());
        if(token.getRefreshToken()!=null){
            this.refreshToken = token.getRefreshToken().getValue();
            if (token.getRefreshToken() instanceof ExpiringOAuth2RefreshToken) {
                ExpiringOAuth2RefreshToken expiringToken = (ExpiringOAuth2RefreshToken) token.getRefreshToken();
                setRefreshTokenExpire(expiringToken.getExpiration());
                setMaxPossibleSessionExpire(new Date(expiringToken.getExpiration().getTime()+(token.getExpiration().getTime()-System.currentTimeMillis())));
            }
        }
        else
            setMaxPossibleSessionExpire(token.getExpiration());
    }

    public void addSessionAccessToken(String accessToken) {
        if(sessionAccessTokens==null && !Utils.isEmpty(accessToken))
            this.sessionAccessTokens = new HashSet<String>();
        if(!Utils.isEmpty(accessToken))
            sessionAccessTokens.add(accessToken);
    }

    @JsonIgnore
    public UserAccount fillAgentUserAccaunt(ClientDetails clientDetails) {
//        this.setUsername(clientDetails.getTechUser().getLogin());
        this.setPassword("userPassword");
//        this.setAuthority(clientDetails.getTechUser().getAuthorities());
        return this;
    }
}