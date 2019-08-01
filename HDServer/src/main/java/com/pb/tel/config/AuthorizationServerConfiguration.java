package com.pb.tel.config;

import com.pb.tel.service.auth.*;
import com.pb.tel.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.vote.ScopeVoter;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by vladimir on 29.07.19.
 */

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private static final Logger log = Logger.getLogger(AuthorizationServerConfiguration.class.getCanonicalName());

    org.springframework.security.oauth2.provider.endpoint.TokenEndpoint sdcs;
    @Resource(name="redisConnectionFactory")
    private JedisConnectionFactory redisConnectionFactory;

    @Autowired
    private ClientDetailsServiceInterface clientDetailsService;

    @Resource(name="tokenService")
    private AuthorizationServerTokenServices tokenService;

    @Resource(name="tokenStore")
    private TokenStoreExtended tokenStore;

    @Resource(name="tokenGranter")
    private TokenGranter tokenGranter;

    @Resource(name="tokenApprovalStore")
    private ApprovalStore approvalStore;

    @Resource(name="authorizationCodeServices")
    private AuthorizationCodeServices authorizationCodeServices;

    @Resource(name="tokenRequestFactory")
    private DefaultOAuth2RequestFactory tokenRequestFactory;

    @Autowired
    private ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter;


//	@EnableResourceServer
//	ResourceServerSecurityConfigurer ss;
//	ResourceServerConfigurerAdapter sacc;
//	SecurityContextPersistenceFilter dcd;
//	UsernamePasswordAuthenticationFilter dcdd;
//	org.springframework.security.core.context.SecurityContextImpl dcds;
//	CompositeSessionAuthenticationStrategy scxssxs;
//	ChangeSessionIdAuthenticationStrategy aaaaw;
//	AuthorizationEndpoint scs;
//	UsernamePasswordAuthenticationFilter sxcs;
//	TokenEndpoint dvdd;
//	HttpSessionSecurityContextRepository cs;
//	org.springframework.security.web.context.SecurityContextPersistenceFilter sxs;
//	AccessDeniedHandlerImpl dvd;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //security.allowFormAuthenticationForClients();
        security.addTokenEndpointAuthenticationFilter(clientCredentialsTokenEndpointFilter);
        log.info("configure  AuthorizationServerSecurityConfigurer   (M1)  in   AuthorizationServerConfiguration");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        log.info("configure  ClientDetailsServiceConfigurer  (M2) in   AuthorizationServerConfiguration");
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        log.info("configure  AuthorizationServerEndpointsConfigurer (M3) in   AuthorizationServerConfiguration");
        //log.info("authenticationManager: "+authenticationManager);
        endpoints.tokenGranter(tokenGranter).
                approvalStore(approvalStore).
                pathMapping("/oauth/confirm_access", "redirect:"+"redirect:"+ MessageUtil.getProperty("entranceLink")+"/continuereg").
//		pathMapping("/oauth/confirm_access", "/oauth/confirm_access").
        requestFactory(tokenRequestFactory).
//                userApprovalHandler(approvalHandler).
                authorizationCodeServices(authorizationCodeServices).
                allowedTokenEndpointRequestMethods(HttpMethod.GET).
                //authenticationManager(authenticationManager).
                        tokenStore(tokenStore).
                tokenServices(tokenService);
    }

    @Bean(name="agentAuthenticationProvider")
    public AgentAuthenticationProvider agentAuthenticationProvider(){
        AgentAuthenticationProvider agentAuthenticationProvider = new AgentAuthenticationProvider();
        agentAuthenticationProvider.setUserDetailsService(new ClientDetailsUserDetailsService(clientDetailsService));
        return agentAuthenticationProvider;
    }

    @Bean(name="clientCredentialsTokenEndpointFilter")
    public ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter(AgentAuthenticationProvider agentAuthenticationProvider){
        ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter = new ClientCredentialsTokenEndpointFilter();
        clientCredentialsTokenEndpointFilter.setAuthenticationManager(new ProviderManager(Arrays.asList(agentAuthenticationProvider)));
        return clientCredentialsTokenEndpointFilter;
    }


    @Bean(name="accessDecisionManager")
    public AccessDecisionManager accessDecisionManager(){
        List<AccessDecisionVoter> listVoters = new ArrayList();
        listVoters.add(new ScopeVoter());
        listVoters.add(new RoleVoter());
        listVoters.add(new AuthenticatedVoter());
        listVoters.add(new WebExpressionVoter());
        return new UnanimousBased(listVoters);
    }
//		--- This is inmemory storage ---
//    @Bean(name="tokenStore")
//    public TokenStore tokenStore(){
//    	InMemoryTokenStore tokenStore = new InMemoryTokenStore() ;//Надо делать реализацию под редис хранилище!!!
//    	tokenStore.setFlushInterval(1);
//    	return tokenStore;
//    }

    @Bean(name="tokenStore")
    public TokenStoreExtended redisTokenStore(){
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory) ;
        tokenStore.setPrefix(SecurityConfig.redisTokenStorePrefix);//"Oauth2$"
        return tokenStore;
    }

    @Bean(name="tokenService")
    public DefaultTokenServices tokenService(TokenStore tokenStore, ClientDetailsServiceInterface clientDetailsService){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setAccessTokenValiditySeconds(SecurityConfig.accessTokenValiditySeconds);
        tokenServices.setRefreshTokenValiditySeconds(SecurityConfig.refreshTokenValiditySeconds);
        tokenServices.setClientDetailsService(clientDetailsService);
        return tokenServices;
    }

    @Bean(name="tokenRequestFactory")
    public DefaultOAuth2RequestFactory defaultOAuth2RequestFactory(ClientDetailsServiceInterface clientDetailsService){
        return new DefaultOAuth2RequestFactory(clientDetailsService);
    }

//    --- This is inmemory storage ---
//    @Bean(name="authorizationCodeServices")
//    public AuthorizationCodeServices authorizationCodeServices(){
//    	return new InMemoryAuthorizationCodeServices();
//    }

    @Bean(name="authorizationCodeServices")
    public AuthorizationCodeServices authorizationCodeServices(RedisTemplate redisTemplate){
        return new RedisBasedAuthorizationCodeServices(redisTemplate);
    }


    @Bean(name="tokenGranter")
    public TokenGranter tokenGranter(AuthorizationServerTokenServices tokenService, AuthorizationCodeServices authorizationCodeServices,
                                     ClientDetailsServiceInterface clientDetailsService, OAuth2RequestFactory tokenRequestFactory){
        List<TokenGranter> tokenGranters = new ArrayList<>();
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenService, authorizationCodeServices, clientDetailsService, tokenRequestFactory));
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenService, clientDetailsService, tokenRequestFactory));
        tokenGranters.add(new RefreshTokenGranter(tokenService, clientDetailsService, tokenRequestFactory));
        CompositeTokenGranter tokenGranter = new CompositeTokenGranter(tokenGranters) ;
        return tokenGranter;
    }

    @Bean(name="tokenApprovalStore")
    public ApprovalStore approvalStore(TokenStore tokenStore){
        TokenApprovalStore tokenApprovalStore = new TokenApprovalStore();
        tokenApprovalStore.setTokenStore(tokenStore);
        return tokenApprovalStore;
    }

    public String toString(){
        return super.toString()+"  Eto moy AuthorizationServerConfiguration !!!";
    }
}
