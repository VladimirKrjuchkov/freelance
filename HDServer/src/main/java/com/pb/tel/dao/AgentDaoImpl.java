package com.pb.tel.dao;

import com.pb.tel.service.auth.AgentDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladimir on 26.07.19.
 */

@Repository("agentDaoImpl")
@Transactional
public class AgentDaoImpl {

    @Transactional(readOnly = true, propagation= Propagation.SUPPORTS)
    public List<AgentDetails> getAllAgentDetails(){
        return new ArrayList<>();
    }
}
