package com.pb.tel.config;

import com.pb.tel.filter.oauth.ResourceTokenClientIdProcessingFilter;
import com.pb.tel.service.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;


@Configuration
@EnableWebSecurity(debug=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger log = Logger.getLogger(SecurityConfig.class.getCanonicalName());


	@Value("${refresh.token.validity.seconds}")
  	private int refreshTokenValidity;

  	@Value("${access.token.validity.seconds}")
  	private int accessTokenValidity;

  	@Value("${sid.parametr.name}")
  	private String sidParametrName;

	@Value("${entrancelink}")
  	private String entranceLink;

	@Value("${redis.token.store.prefix}")
	private String prefix;

	@Value("${not.secured.urls}")
	private String[] notSecuredUrls;

	@Value("${redis.enable}")
	private boolean useRedisSecurityContextRepository;

  	public static int refreshTokenValiditySeconds;

  	public static int accessTokenValiditySeconds;

  	public static String redisTokenStorePrefix;

  	@PostConstruct
  	private void init(){
  		accessTokenValiditySeconds = accessTokenValidity;
  		refreshTokenValiditySeconds = refreshTokenValidity;
  		redisTokenStorePrefix = prefix;
  	}

	@Autowired
	private UserNamePasswordAuthenticationProvider userNamePasswordAuthenticationProvider;

    @Autowired
    private AuthenticationFailureHandlerImpl authenticationFailureHandlerImpl;

    @Autowired
    @Lazy
	private ResourceServerTokenServices tokenServices;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(userNamePasswordAuthenticationProvider).eraseCredentials(false);
    	log.info("AuthenticationManagerBuilder: "+auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	HttpSecurity preparedHttp = http.
    		 csrf().disable().
    		 authorizeRequests().
    		 antMatchers(notSecuredUrls).permitAll().
             anyRequest().authenticated().
             and().
			 addFilterBefore(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).
             exceptionHandling().
             authenticationEntryPoint(loginUrlAuthenticationEntryPoint()).
             and();
    		 if(useRedisSecurityContextRepository) {
				 preparedHttp.securityContext().securityContextRepository(getRedisSecurityContextRepository());
			 }
    }

    @Configuration
    @Order(1)
    public class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        protected void configure(HttpSecurity http) throws Exception {
            http
            	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            	.csrf().disable()
            	.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
				.and()
                .antMatcher("/checked/**")
                .authorizeRequests()
				.anyRequest().authenticated()
                .and()
                .addFilterBefore(resourceTokenClientIdProcessingFilter(), AbstractPreAuthenticatedProcessingFilter.class)
				.securityContext().securityContextRepository(new NullSecurityContextRepository());
        }
    }

	@Bean(name="usernamePasswordAuthenticationFilter")
	public com.pb.tel.filter.oauth.UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter(){
		com.pb.tel.filter.oauth.UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new com.pb.tel.filter.oauth.UsernamePasswordAuthenticationFilter();
		usernamePasswordAuthenticationFilter.setFilterProcessesUrl("/login");
		usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(successHandlerImplementation());
		usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandlerImpl);
		return usernamePasswordAuthenticationFilter;
	}


	@Bean( name="authenticationManager")
	@Override
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

    @Bean(name="redisSecurityContextRepository")
    public RedisSecurityContextRepository getRedisSecurityContextRepository(){
    	return new RedisSecurityContextRepository();
    }

    @Bean(name="resourceTokenClientIdProcessingFilter")
    public ResourceTokenClientIdProcessingFilter resourceTokenClientIdProcessingFilter(){
    	ResourceTokenClientIdProcessingFilter resourceTokenClientIdProcessingFilter = new ResourceTokenClientIdProcessingFilter();
    	OAuth2AuthenticationManager oAuth2AuthenticationManager = new OAuth2AuthenticationManager();
    	oAuth2AuthenticationManager.setClientDetailsService(clientDetailsService());
    	oAuth2AuthenticationManager.setTokenServices(tokenServices);
    	resourceTokenClientIdProcessingFilter.setAuthenticationManager(oAuth2AuthenticationManager);
    	resourceTokenClientIdProcessingFilter.setTokenExtractor(new TokenClientIdExtractor(sidParametrName));
    	return resourceTokenClientIdProcessingFilter;
    }

    @Bean(name="successHandlerImplementation")
  	public SuccessHandlerImplementation successHandlerImplementation(){
    	SuccessHandlerImplementation successHandlerImplementation = new SuccessHandlerImplementation();
  		return successHandlerImplementation;
  	}

    @Bean(name="authenticationFailureHandlerImpl")
  	public AuthenticationFailureHandlerImpl authenticationFailureHandlerImpl(){
    	AuthenticationFailureHandlerImpl authenticationFailureHandlerImpl = new AuthenticationFailureHandlerImpl(entranceLink+"/admin.html");
    	return authenticationFailureHandlerImpl;
    }


  	@Bean(name="userNamePasswordAuthenticationProvider")
  	public UserNamePasswordAuthenticationProvider userNamePasswordAuthenticationProvider(HelpDeskUserDetailsService helpDeskUserDetailsService){
  		UserNamePasswordAuthenticationProvider userNamePasswordAuthenticationProvider = new UserNamePasswordAuthenticationProvider();
  		userNamePasswordAuthenticationProvider.setUserDetailsService(helpDeskUserDetailsService);
  		return userNamePasswordAuthenticationProvider;
  	}

  	@Bean(name="loginUrlAuthenticationEntryPoint")
  	public LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint(){
		LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(entranceLink+"/agent", sidParametrName);
  		loginUrlAuthenticationEntryPoint.setForceHttps(true);
  		return loginUrlAuthenticationEntryPoint;
  	}

	@Bean(name="clientDetailsService")
	public ClientDetailsServiceInterface clientDetailsService(){
		ClientDetailsServiceInterface clientDetailsService = new ClientDetailsService();
		return clientDetailsService;
	}
}
