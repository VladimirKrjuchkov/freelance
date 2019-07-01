package com.pb.tel.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({@PropertySource(value="classpath:com/pb/tel/config/app.properties", encoding="UTF-8"),
	  /*@PropertySource(value="classpath:com/pb/ppls/config/messages.txt", encoding="UTF-8")*/})
@ComponentScan(basePackages = "com.pb.tel")
//,
////		basePackages = {"com.pb.ppls","com.pb.ppls.dao"}, 
////				includeFilters={@ComponentScan.Filter(type=FilterType.REGEX, pattern=".*AgentDaoImpl.*")},
//				excludeFilters={@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, classes=AuthorizationServerConfiguration.class),
//						//@ComponentScan.Filter(type=FilterType.REGEX, pattern=".*AuthorizationServerEndpointsConfiguration|.*AuthorizationServerSecurityConfiguration|.*EnableAuthorizationServerCustom")})
//						@ComponentScan.Filter(type=FilterType.REGEX, pattern="com.pb.ppls.config.oauth2..*")}) //com.pb.ppls.config.oauth2..*
public class RootConfig {
	
//	private static final Logger log = Logger.getLogger(RootConfig.class.getCanonicalName());
		
//	@Bean("messageSource")
//	   public MessageSource messageSource() {
//		 // CustomReloadableResourceBundleMessageSource messageSource=new CustomReloadableResourceBundleMessageSource();
//	      ReloadableResourceBundleMessageSource messageSource=new ReloadableResourceBundleMessageSource();
//	      messageSource.setBasename("classpath:com/pb/ppls/messages/messages");
//	      messageSource.setDefaultEncoding("UTF-8");
//	      messageSource.setUseCodeAsDefaultMessage(true);
//	      messageSource.setCacheMillis(100);
//	      return messageSource;
//	   }

}