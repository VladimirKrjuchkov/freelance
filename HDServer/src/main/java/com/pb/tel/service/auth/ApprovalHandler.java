package com.pb.tel.service.auth;

import com.pb.tel.data.UserAccount;
import com.pb.tel.storage.Storage;
import com.pb.tel.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 13.08.19.
 */
public class ApprovalHandler extends ApprovalStoreUserApprovalHandler {

    private final Logger log = Logger.getLogger(ApprovalHandler.class.getCanonicalName());

    @Autowired
    private Storage sessionStorage ;

    @Override
    public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
        UserAccount userAccount = null;
        try {
            log.info("userAuthentication: "+userAuthentication);
            if((userAccount = (UserAccount)userAuthentication.getCredentials()).isRegistrationUser()){
                log.info("Approval userAccount: "+userAccount);
                authorizationRequest.setApproved(false);
                userAccount.setSidBi(UUID.randomUUID().toString());
                log.info("register secret: "+userAccount.getSidBi());
            }else {
                authorizationRequest.setApproved(true);
            }
            return authorizationRequest;
        }
        catch (Exception e) {
            log.log(Level.SEVERE, "Error in checkForPreApproval for userAccount = "+userAccount, e);
            throw new ClientRegistrationException(MessageUtil.getMessage("unkn.UNKN01"), e);
        }

    }

}
