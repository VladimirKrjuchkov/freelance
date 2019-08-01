package com.pb.tel.service.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pb.tel.data.Consumers;
import com.pb.tel.data.User;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by vladimir on 26.07.19.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "agentDetails")
@XmlType
@Entity
@Table(name = "AgentDetails")
@NamedQueries({@NamedQuery(name = "AgentDetails.findAll", query = "SELECT DISTINCT w FROM AgentDetails w LEFT JOIN FETCH w.oper"),
        @NamedQuery(name = "AgentDetails.findAllAndpackTemplates", query = "SELECT DISTINCT w FROM AgentDetails w "/*LEFT JOIN FETCH w.commercDetails as commercDetails LEFT JOIN FETCH commercDetails.packTemplates as packTemplates LEFT JOIN FETCH packTemplates.accPairSet"*/),
        @NamedQuery(name = "AgentDetails.findById", query = "SELECT w FROM AgentDetails w where w.clientId = :clientId"),
        @NamedQuery(name = "AgentDetails.findByIdWithCommercDetails", query = "SELECT DISTINCT w FROM AgentDetails w "/*LEFT JOIN FETCH w.commercDetails as commercDetails LEFT JOIN FETCH commercDetails.packTemplates as packTemplates LEFT JOIN FETCH packTemplates.accPairSet where w.clientId = :clientId"*/)})
@BatchSize(size=50)
@DynamicUpdate
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
public class AgentDetails implements Serializable, Comparator<AgentDetails>, Consumers {

    public AgentDetails(){}

    public AgentDetails(String clientId, String name){
        this.clientId = clientId;
        this.name = name;
    }

    @Id
    @Column(name = "clientId", nullable=false)
    private String clientId;

    @XmlTransient
    @Column(name = "clientSecret", nullable=false)
    private String clientSecret;

    @XmlTransient
    @Column(name = "resourceIds", nullable=false)
    private String resourceIds;

    @XmlTransient
    @Column(name = "scopes", nullable=true)
    private String scopes;

    @XmlTransient
    @Column(name = "grantTypes", nullable=false)
    private String grantTypes;

    @XmlTransient
    @Column(name = "authorities", nullable=false)
    private String authorities;

    @XmlTransient
    @Column(name = "redirectUris", nullable=true)
    private String redirectUris;

    @XmlTransient
    @Column(name = "accessTokenValiditySeconds", nullable=true)
    private Integer accessTokenValiditySeconds;

    @XmlTransient
    @Column(name = "refreshTokenValiditySeconds", nullable=true)
    private Integer refreshTokenValiditySeconds;

    @Column(name = "name", nullable=false)
    private String name;

    @XmlTransient
    @Column(name = "groupIds", nullable=false)
    private String groupIds;

    @XmlTransient
    @Column(name = "ip", nullable=true)
    private String ip;

    @XmlTransient
    @Column(name = "useForMobile", nullable=false)
    private boolean useForMobile;

    @XmlTransient
    @Column(name = "allowRoleControl", nullable=false)
    private boolean allowRoleControl;

    @XmlTransient
    @Column(name = "commercial", nullable=false)
    private boolean commercial;

    @XmlTransient
    @Column(name = "agentCert", nullable=true)
    private String agentCert;

    @XmlTransient
    @Column(name = "description", nullable=true)
    private String description;

    @XmlTransient
    @Column(name = "strategies", nullable=true)
    private String strategies;

    @OneToOne(fetch=FetchType.LAZY, optional=false, orphanRemoval=false)
    @JoinColumn(name="techLogin", referencedColumnName="login")
    private User user;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getId(){
        return getClientId();
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

    @Override
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

    public boolean isUseForMobile() {
        return useForMobile;
    }

    public void setUseForMobile(boolean useForMobile) {
        this.useForMobile = useForMobile;
    }

    public boolean isAllowRoleControl() {
        return allowRoleControl;
    }

    public void setAllowRoleControl(boolean allowRoleControl) {
        this.allowRoleControl = allowRoleControl;
    }

    public boolean isCommercial() {
        return commercial;
    }

    public void setCommercial(boolean commercial) {
        this.commercial = commercial;
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

    public String getStrategies() {
        return strategies;
    }

    public void setStrategies(String strategies) {
        this.strategies = strategies;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accessTokenValiditySeconds == null) ? 0 : accessTokenValiditySeconds.hashCode());
        result = prime * result + ((agentCert == null) ? 0 : agentCert.hashCode());
        result = prime * result + (allowRoleControl ? 1231 : 1237);
        result = prime * result + ((authorities == null) ? 0 : authorities.hashCode());
        result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
        result = prime * result + ((clientSecret == null) ? 0 : clientSecret.hashCode());
        result = prime * result + (commercial ? 1231 : 1237);
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((grantTypes == null) ? 0 : grantTypes.hashCode());
        result = prime * result + ((groupIds == null) ? 0 : groupIds.hashCode());
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((redirectUris == null) ? 0 : redirectUris.hashCode());
        result = prime * result + ((refreshTokenValiditySeconds == null) ? 0 : refreshTokenValiditySeconds.hashCode());
        result = prime * result + ((resourceIds == null) ? 0 : resourceIds.hashCode());
        result = prime * result + ((scopes == null) ? 0 : scopes.hashCode());
        result = prime * result + ((strategies == null) ? 0 : strategies.hashCode());
        result = prime * result + (useForMobile ? 1231 : 1237);
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AgentDetails other = (AgentDetails) obj;
        if (accessTokenValiditySeconds == null) {
            if (other.accessTokenValiditySeconds != null)
                return false;
        } else if (!accessTokenValiditySeconds.equals(other.accessTokenValiditySeconds))
            return false;
        if (agentCert == null) {
            if (other.agentCert != null)
                return false;
        } else if (!agentCert.equals(other.agentCert))
            return false;
        if (allowRoleControl != other.allowRoleControl)
            return false;
        if (authorities == null) {
            if (other.authorities != null)
                return false;
        } else if (!authorities.equals(other.authorities))
            return false;
        if (clientId == null) {
            if (other.clientId != null)
                return false;
        } else if (!clientId.equals(other.clientId))
            return false;
        if (clientSecret == null) {
            if (other.clientSecret != null)
                return false;
        } else if (!clientSecret.equals(other.clientSecret))
            return false;
        if (commercial != other.commercial)
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (grantTypes == null) {
            if (other.grantTypes != null)
                return false;
        } else if (!grantTypes.equals(other.grantTypes))
            return false;
        if (groupIds == null) {
            if (other.groupIds != null)
                return false;
        } else if (!groupIds.equals(other.groupIds))
            return false;
        if (ip == null) {
            if (other.ip != null)
                return false;
        } else if (!ip.equals(other.ip))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (redirectUris == null) {
            if (other.redirectUris != null)
                return false;
        } else if (!redirectUris.equals(other.redirectUris))
            return false;
        if (refreshTokenValiditySeconds == null) {
            if (other.refreshTokenValiditySeconds != null)
                return false;
        } else if (!refreshTokenValiditySeconds.equals(other.refreshTokenValiditySeconds))
            return false;
        if (resourceIds == null) {
            if (other.resourceIds != null)
                return false;
        } else if (!resourceIds.equals(other.resourceIds))
            return false;
        if (scopes == null) {
            if (other.scopes != null)
                return false;
        } else if (!scopes.equals(other.scopes))
            return false;
        if (strategies == null) {
            if (other.strategies != null)
                return false;
        } else if (!strategies.equals(other.strategies))
            return false;
        if (useForMobile != other.useForMobile)
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }


    @Override
    public int compare(AgentDetails o1, AgentDetails o2) {
        if(o1!=null && o2!=null && o1.getClientId()!=null && o2.getClientId()!=null){
            return o1.getClientId().compareTo(o2.getClientId());
        }
        return 0;
    }

    @Override
    public String toString() {
        return "AgentDetails [" + (clientId != null ? "clientId=" + clientId + ", " : "")
                + (clientSecret != null ? "clientSecret=" + clientSecret + ", " : "")
                + (resourceIds != null ? "resourceIds=" + resourceIds + ", " : "")
                + (scopes != null ? "scopes=" + scopes + ", " : "")
                + (grantTypes != null ? "grantTypes=" + grantTypes + ", " : "")
                + (authorities != null ? "authorities=" + authorities + ", " : "")
                + (redirectUris != null ? "redirectUris=" + redirectUris + ", " : "")
                + (accessTokenValiditySeconds != null
                ? "accessTokenValiditySeconds=" + accessTokenValiditySeconds + ", " : "")
                + (refreshTokenValiditySeconds != null
                ? "refreshTokenValiditySeconds=" + refreshTokenValiditySeconds + ", " : "")
                + (name != null ? "name=" + name + ", " : "") + (groupIds != null ? "groupIds=" + groupIds + ", " : "")
                + (ip != null ? "ip=" + ip + ", " : "") + "useForMobile=" + useForMobile + ", allowRoleControl="
                + allowRoleControl + ", commercial=" + commercial + ", "
                + (agentCert != null ? "agentCert=" + agentCert + ", " : "")
                + (description != null ? "description=" + description + ", " : "")
                + (strategies != null ? "strategies=" + strategies + ", " : "") + (user != null ? "oper=" + user : "")
                + "]";
    }
}
