package com.pb.tel.service.auth;

import com.pb.tel.data.Operator;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.Map;
import java.util.Set;

/**
 * Created by vladimir on 29.07.19.
 */
public interface TokenStoreExtended extends TokenStore {

    public Set<OAuth2Authentication> findAuthenticationsByUserName(String userName);

    public void removeAccessTokens(Set<String> tokensValue);

    public void removeAccessToken(String tokensValue);

    public void leaveMaineSession(String userName, String mainToken);

    public void invalidateUserAuthentications(String userName, Operator user);

    public Map<String, Set<OAuth2Authentication>> getAllAuthenticationsByLogin();

    public Set<String> getAllLoggedLogin();
}