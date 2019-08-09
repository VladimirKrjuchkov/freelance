package com.pb.tel.service;

import com.pb.tel.data.UserAccount;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.utils.MessageUtil;
import com.pb.tel.utils.Utils;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
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

    public static String buildSid(String accessToken, String clientId) {
        return "Bearer " + accessToken + ", Id " + clientId;
    }

    public static Authentication authorizeUser(UserDetails userDetails, UserAccount userAccount, Collection<GrantedAuthority> authority, Object details) {
        PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(userDetails, userAccount, authority);
        authenticationToken.setDetails(details);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("return token = " + authenticationToken);
        return authenticationToken;
    }

    public static Collection<GrantedAuthority> getCombinetAuthorities(Collection<? extends GrantedAuthority> agentAuthority, Collection<? extends GrantedAuthority> userAuthority) {
        if (agentAuthority == null || userAuthority == null)
            return null;
        HashSet<GrantedAuthority> authority = new HashSet<>(agentAuthority);
        authority.addAll(userAuthority);
        return authority;
    }

    public static UserAccount getAccountFromContext() throws LogicException {
        log.info("@#@ SecurityContextHolder.getContext(): " + SecurityContextHolder.getContext());
        log.info("@#@ SecurityContextHolder.getContext().getAuthentication(): " + SecurityContextHolder.getContext().getAuthentication());
        return getAccountFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
    }

    public static UserAccount getAccountFromAuthentication(Authentication authentication) throws LogicException {
        log.info("@#@ authentication: " + authentication);
        log.info("@#@ (authentication instanceof OAuth2Authentication ): " + (authentication instanceof OAuth2Authentication));
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
            Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
            log.info("@#@ userAuthentication: " + userAuthentication);
            return getUserAccountFromCredentials(userAuthentication.getCredentials());
        } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            PreAuthenticatedAuthenticationToken preAuthentication = (PreAuthenticatedAuthenticationToken) authentication;
            return getUserAccountFromCredentials(preAuthentication.getCredentials());
        } else
            throw Utils.getLogicException("auth.AUTH08");
    }

    private static UserAccount getUserAccountFromCredentials(Object credentials ) throws LogicException{
        log.info("@#@ credentials: "+credentials);
        log.info("@#@ credentials instanceof UserAccount : "+(credentials instanceof UserAccount ));
        if(credentials instanceof UserAccount )
            return (UserAccount)credentials;
        else
            throw Utils.getLogicException("auth.AUTH08");
    }
}
