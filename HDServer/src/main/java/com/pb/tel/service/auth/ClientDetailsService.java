package com.pb.tel.service.auth;

import com.pb.tel.dao.AgentDaoImpl;
import com.pb.tel.data.AgentDetails;
import com.pb.tel.data.Roles;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

/**
 * Created by vladimir on 26.07.19.
 */
public class ClientDetailsService implements ClientDetailsServiceInterface{

    private static final Logger log = Logger.getLogger(ClientDetailsService.class.getCanonicalName());

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    @Autowired
    private AgentDaoImpl agentDaoImpl;

    private Map<String, ClientDetails> clientDetailsStore = new ConcurrentHashMap<String, ClientDetails>();

    private Map<String, ClientDetails> storedView;

    private ClientDetails adminClientDetails;

    @PostConstruct
    private void init()throws LogicException {
        log.info("in init of ClientDetailsService !!! : "+this);
        restoreClient();
    }

    public ClientDetailsService()/*throws LogicException*/{
        log.info("in defoult constructor !!! : "+this);
        //restoreClient();
    }

    public ClientDetailsService(AgentDaoImpl agentDaoImpl)throws LogicException{
        log.info("in constructor whith AgentDaoImpl !!!");
        this.agentDaoImpl = agentDaoImpl;
        restoreClient();
    }

    public void restoreClient()throws LogicException{
        Map<String, ClientDetails> store = reloadAgentDetails();
        setAdminClientDetails(store);
        readWriteLock.writeLock().lock();
        storedView = store;
        clientDetailsStore = new ConcurrentHashMap<String, ClientDetails>(store);
        readWriteLock.writeLock().unlock();
    }

    public Map<String, ClientDetails> reloadAgentDetails()throws LogicException{
        Map<String, ClientDetails> store = new ConcurrentHashMap<String, ClientDetails>();
        List<AgentDetails> agentDetails = agentDaoImpl.getAllAgentDetails();
        //agentDetails = decryptSecret(agentDetails);
        for(AgentDetails agent : agentDetails){
            ClientDetails clientDetails = new ClientDetails(agent);
            store.put(clientDetails.getClientId(), clientDetails);
        }
        return store;
    }

    private void setAdminClientDetails(Map<String, ClientDetails> store)throws LogicException{
        int count = 0;
        ClientDetails adminClientDetails = null;
        GrantedAuthority adminAuthority = new SimpleGrantedAuthority(Roles.ROLE_MAIN_AGENT.getCode());
        for(ClientDetails clientDetails : store.values()){
            if(clientDetails.getAuthorities().contains(adminAuthority)){
                adminClientDetails = clientDetails;
                count++;
            }
        }
//        if(count>1 || adminClientDetails==null)
//            throw Utils.getLogicException("ADM001");
        this.adminClientDetails = adminClientDetails;
    }

    public ClientDetails getAdminClientDetails(){
        return adminClientDetails;
    }

    public boolean isAdminClientDetails(String clientId){
        return clientId.equals(adminClientDetails.getClientId());
    }


    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        log.info("clientId Start :"+clientId);
        if (Utils.isEmpty(clientId)) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
        readWriteLock.readLock().lock();
        ClientDetails details = clientDetailsStore.get(clientId);
        readWriteLock.readLock().unlock();
        if (details == null) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
        log.info("clientId Finish :"+clientId);
        return details;
    }

    public List<ClientDetails> getStoredClients(){
        return new ArrayList<ClientDetails>(storedView.values());
    }

    public void setAgentDaoImpl(AgentDaoImpl agentDaoImpl) {
        this.agentDaoImpl = agentDaoImpl;
    }
}
