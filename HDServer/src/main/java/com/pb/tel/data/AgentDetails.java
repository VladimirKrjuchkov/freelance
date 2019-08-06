package com.pb.tel.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by vladimir on 02.08.19.
 */

@Entity
@Table(name = "helpdeskagentdetails")
@NamedQueries({@NamedQuery(name = "AgentDetails.findAll", query = "SELECT DISTINCT w FROM AgentDetails w")})
@BatchSize(size=50)
@DynamicUpdate
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class AgentDetails  implements Serializable {

    public AgentDetails(){}

    @Id
    @Column(name = "clientId", nullable=false)
    private String clientId;

    @Column(name = "clientSecret", nullable=false)
    private String clientSecret;

    @Column(name = "resourceIds", nullable=false)
    private String resourceIds;

    @Column(name = "scopes", nullable=true)
    private String scopes;

    @Column(name = "grantTypes", nullable=false)
    private String grantTypes;

    @Column(name = "authorities", nullable=false)
    private String authorities;

    @Column(name = "redirectUris", nullable=true)
    private String redirectUris;

    @Column(name = "accessTokenValiditySeconds", nullable=true)
    private Integer accessTokenValiditySeconds;

    @Column(name = "refreshTokenValiditySeconds", nullable=true)
    private Integer refreshTokenValiditySeconds;

    @Column(name = "name", nullable=false)
    private String name;

    @Column(name = "groupIds", nullable=false)
    private String groupIds;

    @Column(name = "ip", nullable=true)
    private String ip;

    @Column(name = "allowRoleControl", nullable=false)
    private boolean allowRoleControl;

    @Column(name = "agentCert", nullable=true)
    private String agentCert;

    @Column(name = "description", nullable=true)
    private String description;

    @Column(name = "techLogin", nullable=false)
    private String techLogin;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public String getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String redirectUris) {
        this.redirectUris = redirectUris;
    }

    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isAllowRoleControl() {
        return allowRoleControl;
    }

    public void setAllowRoleControl(boolean allowRoleControl) {
        this.allowRoleControl = allowRoleControl;
    }

    public String getAgentCert() {
        return agentCert;
    }

    public void setAgentCert(String agentCert) {
        this.agentCert = agentCert;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechLogin() {
        return techLogin;
    }

    public void setTechLogin(String techLogin) {
        this.techLogin = techLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AgentDetails that = (AgentDetails) o;

        if (allowRoleControl != that.allowRoleControl) return false;
        if (clientId != null ? !clientId.equals(that.clientId) : that.clientId != null) return false;
        if (clientSecret != null ? !clientSecret.equals(that.clientSecret) : that.clientSecret != null) return false;
        if (resourceIds != null ? !resourceIds.equals(that.resourceIds) : that.resourceIds != null) return false;
        if (scopes != null ? !scopes.equals(that.scopes) : that.scopes != null) return false;
        if (grantTypes != null ? !grantTypes.equals(that.grantTypes) : that.grantTypes != null) return false;
        if (authorities != null ? !authorities.equals(that.authorities) : that.authorities != null) return false;
        if (redirectUris != null ? !redirectUris.equals(that.redirectUris) : that.redirectUris != null) return false;
        if (accessTokenValiditySeconds != null ? !accessTokenValiditySeconds.equals(that.accessTokenValiditySeconds) : that.accessTokenValiditySeconds != null)
            return false;
        if (refreshTokenValiditySeconds != null ? !refreshTokenValiditySeconds.equals(that.refreshTokenValiditySeconds) : that.refreshTokenValiditySeconds != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (groupIds != null ? !groupIds.equals(that.groupIds) : that.groupIds != null) return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (agentCert != null ? !agentCert.equals(that.agentCert) : that.agentCert != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return techLogin != null ? techLogin.equals(that.techLogin) : that.techLogin == null;
    }

    @Override
    public int hashCode() {
        int result = clientId != null ? clientId.hashCode() : 0;
        result = 31 * result + (clientSecret != null ? clientSecret.hashCode() : 0);
        result = 31 * result + (resourceIds != null ? resourceIds.hashCode() : 0);
        result = 31 * result + (scopes != null ? scopes.hashCode() : 0);
        result = 31 * result + (grantTypes != null ? grantTypes.hashCode() : 0);
        result = 31 * result + (authorities != null ? authorities.hashCode() : 0);
        result = 31 * result + (redirectUris != null ? redirectUris.hashCode() : 0);
        result = 31 * result + (accessTokenValiditySeconds != null ? accessTokenValiditySeconds.hashCode() : 0);
        result = 31 * result + (refreshTokenValiditySeconds != null ? refreshTokenValiditySeconds.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (groupIds != null ? groupIds.hashCode() : 0);
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (allowRoleControl ? 1 : 0);
        result = 31 * result + (agentCert != null ? agentCert.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (techLogin != null ? techLogin.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AgentDetails{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", resourceIds='" + resourceIds + '\'' +
                ", scopes='" + scopes + '\'' +
                ", grantTypes='" + grantTypes + '\'' +
                ", authorities='" + authorities + '\'' +
                ", redirectUris='" + redirectUris + '\'' +
                ", accessTokenValiditySeconds=" + accessTokenValiditySeconds +
                ", refreshTokenValiditySeconds=" + refreshTokenValiditySeconds +
                ", name='" + name + '\'' +
                ", groupIds='" + groupIds + '\'' +
                ", ip='" + ip + '\'' +
                ", allowRoleControl=" + allowRoleControl +
                ", agentCert='" + agentCert + '\'' +
                ", description='" + description + '\'' +
                ", techLogin='" + techLogin + '\'' +
                '}';
    }
}
