package com.pb.tel.service.auth;

import com.pb.tel.data.UserAccount;
import com.pb.tel.storage.Storage;
import com.pb.tel.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 26.07.19.
 */
public class UserNamePasswordAuthenticationProvider extends AbstractAuthenticationProvider {

    private final Logger log = Logger.getLogger(UserNamePasswordAuthenticationProvider.class.getCanonicalName());

    @Autowired
    private Storage sessionStorage ;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Start authenticate in UserNamePasswordAuthenticationProvider");
        Object details = authentication.getDetails();
        log.info("details: "+details);
        //String secretComponent = (String)authentication.getDetails();
        String login = (String)authentication.getPrincipal();
        String password = (String)authentication.getCredentials();
        UserDetails user;
        try{
            log.info("login: "+login);
            log.info("password: "+password);
            user = userDetailsService.loadUserByUsername(login);
            String principals[] = ((String) authentication.getPrincipal()).split(",");
            String storageKey = principals[0];
            UserAccount userAccount = (UserAccount)sessionStorage.getValue(storageKey);
            userAccount.setUser(user);
            sessionStorage.putValue(storageKey, userAccount, userAccount.getMaxPossibleSessionExpire());
            UsernamePasswordAuthenticationToken result;


//            String password256Hash = Utils.getSh256(password + Reference.passwordSalt); <<<--- после рефакторинга надо будет хешить пароль
            String password256Hash = password;

            if(!user.isEnabled())
                throw new BadCredentialsException(MessageUtil.getMessage("auth.AUTH03", login));

//            if(!password256Hash.equals(user.getPassword()) && !Utils.getSh512(password + Reference.passwordSalt).equals(user.getPassword())) <<<--- после рефакторинга надо будет хешить пароль
            if(!password256Hash.equals(user.getPassword()))
                throw new BadCredentialsException(MessageUtil.getMessage("auth.AUTH02"));
        }
        catch (BadCredentialsException | UsernameNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            log.log(Level.SEVERE, "Error in time check user credentials ! ", e);
            throw new BadCredentialsException(MessageUtil.getMessage("auth.AUTH02"), e);
        }
        log.info("authentication: "+authentication);
        log.info("authentication: "+authentication.getPrincipal());
        log.info("authentication: "+authentication.getCredentials());
        log.info("authentication: "+user.getAuthorities());
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user.getUsername(), user, user.getAuthorities());
        result.setDetails(authentication.getDetails());

        log.info("result authentication: "+result);
        log.info("result authentication: "+result.getPrincipal());
        log.info("result authentication: "+result.getCredentials());
        log.info("result authentication: "+result.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(result);

        log.info("result.getAuthorities: "+result.getAuthorities());
        log.info("Finish authenticate in AgentAuthenticationProvider");
        return result;
    }
}
