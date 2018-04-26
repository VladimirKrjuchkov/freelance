package com.pb.tel.dao;

import com.pb.tel.data.privatmarket.BotMessage;
import com.pb.tel.data.privatmarket.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 25.04.18.
 */

@Repository("messageDaoImpl")
public class MessageDaoImpl implements MessageDao{

    private static final Logger log = Logger.getLogger(MessageDaoImpl.class.getCanonicalName());

    @PersistenceContext
    private EntityManager em;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true, propagation= Propagation.SUPPORTS)
    public List<BotMessage> getByLang(String lang){
        long start = System.currentTimeMillis();
        TypedQuery<BotMessage> query = em.createNamedQuery("BotMessage.findByLang", BotMessage.class);
        query.setParameter("lang", lang);
        List<BotMessage> result = query.getResultList();
        log.log(Level.INFO, "BotMessage.findByLang at "+(System.currentTimeMillis() - start)+"ms");
        return result;
    }
}
