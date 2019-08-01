package com.pb.tel.service.auth;

import com.pb.tel.service.exception.LogicException;

import java.util.List;
import java.util.Map;

/**
 * Created by vladimir on 26.07.19.
 */
public interface ClientDetailsServiceInterface extends org.springframework.security.oauth2.provider.ClientDetailsService{

    public void restoreClient()throws LogicException;

    public Map<String, ClientDetails> reloadAgentDetails()throws LogicException;

    public ClientDetails getAdminClientDetails();

    public boolean isAdminClientDetails(String clientId);

    public List<ClientDetails> getStoredClients();
}
