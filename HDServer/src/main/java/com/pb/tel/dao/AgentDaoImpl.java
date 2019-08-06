package com.pb.tel.dao;

import com.pb.tel.data.AgentDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by vladimir on 26.07.19.
 */

@Repository("agentDaoImpl")
@Transactional
public class AgentDaoImpl {

    private static final Logger log = Logger.getLogger(AgentDaoImpl.class.getCanonicalName());

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true, propagation= Propagation.SUPPORTS)
    public List<AgentDetails> getAllAgentDetails(){
        long start = System.currentTimeMillis();
        List<AgentDetails> result = em.createNamedQuery("AgentDetails.findAll", AgentDetails.class).getResultList();
        log.info("getAllAgentDetails at "+(System.currentTimeMillis() - start)+"ms");
        return result;
    }
}
