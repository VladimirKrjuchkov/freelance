package com.pb.tel.service.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pb.tel.data.Consumers;
import com.pb.tel.data.User;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.util.StringUtils;

import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by vladimir on 26.07.19.
 */
public class ClientDetails extends BaseClientDetails implements Consumers {

    private final Logger log = Logger.getLogger(ClientDetails.class.getCanonicalName());

    private String name;

    private List<String> groupIds;

    private List<String> ip;

    private List<String> strategies;

    private boolean useForMobile;

    private boolean allowRoleControl;

    private boolean commercial;


    @XmlTransient
    @JsonIgnore
    private AgentDetails agentDetails;

    private String agentCert;


    public ClientDetails(){}

    public ClientDetails(String name, String clientId){
        this.name = name;
        this.setClientId(clientId);
    }

    public ClientDetails(AgentDetails agentDetails) {
        super(agentDetails.getClientId(), agentDetails.getResourceIds(), agentDetails.getScopes(), agentDetails.getGrantTypes(), agentDetails.getAuthorities(), agentDetails.getRedirectUris());
        this.agentDetails = agentDetails;
        this.name = agentDetails.getName();
        this.groupIds = prepareGroupIds(agentDetails.getGroupIds());
        this.ip = Arrays.asList(agentDetails.getIp()==null ? new String[]{""} : agentDetails.getIp().split(","));
        this.useForMobile = agentDetails.isUseForMobile();
        this.allowRoleControl = agentDetails.isAllowRoleControl();
        this.commercial = agentDetails.isCommercial();
        this.agentCert = agentDetails.getAgentCert();
        //if(!Util.isEmpty(agentDetails.getStrategies()))
        this.strategies = Arrays.asList(StringUtils.trimArrayElements(StringUtils.commaDelimitedListToStringArray(agentDetails.getStrategies())));
        this.setClientSecret(agentDetails.getClientSecret());
        setAccessTokenValiditySeconds(agentDetails.getAccessTokenValiditySeconds());
        setRefreshTokenValiditySeconds(agentDetails.getRefreshTokenValiditySeconds());
    }

    private List<String> prepareGroupIds(String groupIdsData){
        String [] groupIds = groupIdsData.split(",");
        List<String> listGroupIds = new ArrayList<String>();
        for(String groupId : groupIds)
            listGroupIds.add(groupId.trim());
        return listGroupIds;
    }

    @Override
    public String getId() {
        return getClientId();
    }

    @Override
    public String getName() {
        return name;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public List<String> getIp() {
        return ip;
    }

    public List<String> getStrategies() {
        return strategies;
    }

    public void setStrategies(List<String> strategies) {
        this.strategies = strategies;
    }

    public boolean isUseForMobile() {
        return useForMobile;
    }

    public boolean isAllowRoleControl() {
        return allowRoleControl;
    }

    public boolean isCommercial() {
        return commercial;
    }

    public AgentDetails getUserDetails() {
        return agentDetails;
    }

    public String getAgentCert() {
        return agentCert;
    }

    public User getTechUser(){
        return agentDetails.getUser();
    }

    @Override
    public String toString() {
        return "ClientDetails [" + (getName() != null ? "getName()=" + getName() + ", " : "")
                + (getStrategies() != null ? "getStrategies()=" + getStrategies() + ", " : "") + "isCommercial()="
                + isCommercial() + ", " + (getClientId() != null ? "getClientId()=" + getClientId() + ", " : "")
                + (getAutoApproveScopes() != null ? "getAutoApproveScopes()=" + getAutoApproveScopes() + ", " : "")
                + "isSecretRequired()=" + isSecretRequired() + ", isScoped()=" + isScoped() + ", "
                + (getScope() != null ? "getScope()=" + getScope() + ", " : "")
                + (getResourceIds() != null ? "getResourceIds()=" + getResourceIds() + ", " : "")
                + (getAuthorizedGrantTypes() != null ? "getAuthorizedGrantTypes()=" + getAuthorizedGrantTypes() + ", "
                : "")
                + (getRegisteredRedirectUri() != null
                ? "getRegisteredRedirectUri()=" + getRegisteredRedirectUri() + ", " : "")
                + (getAuthorities() != null ? "getAuthorities()=" + getAuthorities() + ", " : "")
                + (getAccessTokenValiditySeconds() != null
                ? "getAccessTokenValiditySeconds()=" + getAccessTokenValiditySeconds() + ", " : "")
                + (getRefreshTokenValiditySeconds() != null
                ? "getRefreshTokenValiditySeconds()=" + getRefreshTokenValiditySeconds() + ", " : "")
                + (getAdditionalInformation() != null ? "getAdditionalInformation()=" + getAdditionalInformation() : "")
                + "]";
    }


}
