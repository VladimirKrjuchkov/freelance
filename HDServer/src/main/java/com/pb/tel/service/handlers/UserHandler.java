package com.pb.tel.service.handlers;

import com.pb.tel.dao.OperatorDaoImpl;
import com.pb.tel.data.Operator;
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
    private OperatorDaoImpl operatorDaoDaoImpl;

    public Operator getUserByLogin(String login) {
        Operator result = null;
        try {
            result = operatorDaoDaoImpl.getUserByLogin(login);

        }catch (NoResultException e){
            log.info("No user found for inn: " + login);
        }
        return result;
    }
}
