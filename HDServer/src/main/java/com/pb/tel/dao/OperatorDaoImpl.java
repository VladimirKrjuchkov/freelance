package com.pb.tel.dao;

import com.pb.tel.data.Operator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

/**
 * Created by vladimir on 29.07.19.
 */

@Repository("operatorDaoImpl")
@Transactional
public class OperatorDaoImpl {

    private static final Logger log = Logger.getLogger(OperatorDaoImpl.class.getCanonicalName());

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true, propagation= Propagation.SUPPORTS)
    public Operator getUserByLogin(String login){
        long start = System.currentTimeMillis();
        Operator result = em.find(Operator.class, login);
        log.info("getOperatorByLogin at "+(System.currentTimeMillis() - start)+"ms");
        return result;
    }
}
