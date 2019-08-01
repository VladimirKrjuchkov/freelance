package com.pb.tel.service.handlers;

import com.pb.tel.dao.UserDaoImpl;
import com.pb.tel.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.logging.Logger;

/**
 * Created by vladimir on 24.07.19.
 */

@Service("userHandler")
public class UserHandler {

    private final static Logger log = Logger.getLogger(UserHandler.class.getCanonicalName());

    @Autowired
    private UserDaoImpl userDaoImpl;

    public User getUserByLogin(String login) {
        User result = null;
        try {
            result = userDaoImpl.getUserByLogin(login);
//            result.setSessionTtl(Utils.getSecondsToDate(new Date())*1000);

        }catch (NoResultException e){
            log.info("No user found for inn: " + login);
        }
//		result.setSessionTtl(Util.getSecondsToDate(userAccount.getMaxPossibleSessionExpire())*1000);
        return result;
    }
}
