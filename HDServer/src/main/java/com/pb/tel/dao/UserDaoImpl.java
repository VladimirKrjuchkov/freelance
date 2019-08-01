package com.pb.tel.dao;

import com.pb.tel.data.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

/**
 * Created by vladimir on 29.07.19.
 */

@Repository("userDaoImpl")
@Transactional
public class UserDaoImpl {

    private static final Logger log = Logger.getLogger(UserDaoImpl.class.getCanonicalName());

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true, propagation= Propagation.SUPPORTS)
    public User getUserByLogin(String login){
        long start = System.currentTimeMillis();
        User result = em.find(User.class, login);
        log.info("getUserByLogin at "+(System.currentTimeMillis() - start)+"ms");
        return result;
    }
}
