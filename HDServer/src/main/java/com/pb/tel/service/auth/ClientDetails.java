package com.pb.tel.service.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pb.tel.data.AgentDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by vladimir on 26.07.19.
 */
public class ClientDetails extends BaseClientDetails{

    private final Logger log = Logger.getLogger(ClientDetails.class.getCanonicalName());

    private String name;

    private List<String> groupIds;

    private List<String> ip;

    private boolean allowRoleControl;

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
        this.allowRoleControl = agentDetails.isAllowRoleControl();
        this.agentCert = agentDetails.getAgentCert();
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

    public String getId() {
        return getClientId();
    }

    public String getName() {
        return name;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public List<String> getIp() {
        return ip;
    }

    public boolean isAllowRoleControl() {
        return allowRoleControl;
    }

    public AgentDetails getUserDetails() {
        return agentDetails;
    }

    public String getAgentCert() {
        return agentCert;
    }

    @Override
    public String toString() {
        return "ClientDetails{" +
                "name='" + name + '\'' +
                ", groupIds=" + groupIds +
                ", ip=" + ip +
                ", allowRoleControl=" + allowRoleControl +
                ", agentDetails=" + agentDetails +
                ", agentCert='" + agentCert + '\'' +
                '}';
    }
}
