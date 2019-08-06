package com.pb.tel.service;

import com.pb.tel.data.UserAccount;
import com.pb.tel.utils.MessageUtil;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Created by vladimir on 26.07.19.
 */
public class Reference {

    private final static Logger log = Logger.getLogger(Reference.class.getCanonicalName());


    private static Environment env = MessageUtil.env;
    public static String sidParametrName = env.getProperty("sid.parametr.name");
    public static String passwordSalt = env.getProperty("password.salt");

    public static String buildSid(String accessToken, String clientId){
        return "Bearer "+accessToken+", Id "+clientId;
    }

    public static Authentication authorizeUser(UserDetails userDetails, UserAccount userAccount, Collection<GrantedAuthority> authority, Object details) {
        //UserDetails userDetails = new User(userAccount.getUsername(), userAccount.getClientId(), authority);
        //UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userAccount, userDetails.getAuthorities());
        PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(userDetails, userAccount, authority);
        authenticationToken.setDetails(details);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("return token = "+authenticationToken);
        return authenticationToken;
    }

    public static Collection<GrantedAuthority> getCombinetAuthorities(Collection<? extends GrantedAuthority> agentAuthority, Collection<? extends GrantedAuthority> userAuthority){
        if(agentAuthority==null || userAuthority==null)
            return null;
        HashSet<GrantedAuthority> authority = new HashSet<>(agentAuthority);
        authority.addAll(userAuthority);
        return authority;
    }
}
