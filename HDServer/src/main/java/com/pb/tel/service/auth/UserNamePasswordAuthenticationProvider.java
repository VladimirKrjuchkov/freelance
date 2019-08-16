package com.pb.tel.service.auth;

import com.pb.tel.data.Roles;
import com.pb.tel.data.UserAccount;
import com.pb.tel.service.handlers.AccountHandler;
import com.pb.tel.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * Created by vladimir on 26.07.19.
 */
public class UserNamePasswordAuthenticationProvider extends AbstractAuthenticationProvider {

    private final Logger log = Logger.getLogger(UserNamePasswordAuthenticationProvider.class.getCanonicalName());

    @Resource
    private AccountHandler accountHandler;

    @Autowired
    private ClientDetailsServiceInterface clientDetailsService;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Start authenticate in UserNamePasswordAuthenticationProvider");
        boolean agentAuth = false;
        Object details = authentication.getDetails();
        log.info("details: "+details);
        String login = (String)authentication.getPrincipal();
        String password = null;
        UserAccount userAccount = null;
        if(Roles.convertAuthorityToRoles(authentication.getAuthorities()).contains(Roles.ROLE_APP)){
            userAccount = (UserAccount)authentication.getCredentials();
            agentAuth = true;
        }else{
            password = (String)authentication.getCredentials();
        }
        UserDetails user = null;
        log.info("login: "+login);
        log.info("password: "+password);
        if(!agentAuth){
            userAccount = accountHandler.generateUserAccount();
            user = userDetailsService.loadUserByUsername(login);
            userAccount.setUser(user);
            String password256Hash = password;
            if(!user.isEnabled()) {
                throw new BadCredentialsException(MessageUtil.getMessage("auth.AUTH03", login));
            }
            if(!password256Hash.equals(user.getPassword())) {
                throw new BadCredentialsException(MessageUtil.getMessage("auth.AUTH02"));
            }
        }
//      String password256Hash = Utils.getSh256(password + Reference.passwordSalt); <<<--- после рефакторинга надо будет хешить пароль
//      if(!password256Hash.equals(user.getPassword()) && !Utils.getSh512(password + Reference.passwordSalt).equals(user.getPassword())) <<<--- после рефакторинга надо будет хешить пароль
        log.info("authentication: "+authentication);
        log.info("authentication: "+authentication.getPrincipal());
        log.info("authentication: "+authentication.getCredentials());
        log.info("authentication: "+user.getAuthorities());
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user.getUsername(), userAccount, user.getAuthorities());
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
