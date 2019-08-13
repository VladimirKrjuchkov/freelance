package com.pb.tel.service.handlers;

import com.pb.tel.data.UserAccount;
import com.pb.tel.service.auth.ClientDetailsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by vladimir on 08.08.19.
 */

@Service("accountHandler")
public class AccountHandler {

    private static final Logger log = Logger.getLogger(AccountHandler.class.getCanonicalName());

    @Autowired
    private ClientDetailsServiceInterface clientDetailsService;

    @Autowired
    private Environment environment;

    public UserAccount generateUserAccount(){
        UserAccount userAccount = new UserAccount();
        userAccount.setClientId(clientDetailsService.getAdminClientDetails().getClientId());
        userAccount.setMaxPossibleSessionExpire(new Date(System.currentTimeMillis() + Long.valueOf(environment.getProperty("storage.key.validity.millis"))));
        userAccount.setMaxInSecondPossibleSessionExpire(Integer.valueOf(environment.getProperty("storage.key.validity.millis")));
        return userAccount;
    }
}
