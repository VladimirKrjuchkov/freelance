package com.pb.tel.service.auth;

import com.pb.tel.utils.MessageUtil;
import com.pb.tel.utils.Utils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 29.07.19.
 */
public class AgentAuthenticationProvider extends AbstractAuthenticationProvider {

    private final Logger log = Logger.getLogger(AgentAuthenticationProvider.class.getCanonicalName());

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Start authenticate in AgentAuthenticationProvider");
        String secretComponent = (String)authentication.getDetails();
        String clientId = (String)authentication.getPrincipal();
        String password = (String)authentication.getCredentials();
        UserDetails user;
        try{
            user = userDetailsService.loadUserByUsername(clientId);
            String secret = clientId + user.getPassword()+secretComponent;

            if(!user.isEnabled())
                throw new BadCredentialsException(MessageUtil.getMessage("auth.AUTH10", ((ClientDetails)user).getName()));

            if(/*!password.equals(user.getPassword()) && !password.equals(Util.getShHash(secret)) &&*/ !password.equals(Utils.getHash(secret, "SHA-512")))
                throw new BadCredentialsException(MessageUtil.getMessage("auth.AUTH09"));

        }
        catch (Exception e) {
            log.log(Level.SEVERE, "Error in time check agent credentials ! ", e);
            throw new BadCredentialsException(MessageUtil.getMessage("auth.AUTH09"), e);
        }

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                authentication.getCredentials(), user.getAuthorities());
        result.setDetails(authentication.getDetails());
        log.info("Finish authenticate in AgentAuthenticationProvider");
        return result;
    }
}
