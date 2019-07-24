package com.pb.tel.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.logging.Logger;

@Configuration
@PropertySources({@PropertySource(value="classpath:com/pb/tel/config/app.properties", encoding="UTF-8")})
@ComponentScan(basePackages = "com.pb.tel")
public class RootConfig {

	private static final Logger log = Logger.getLogger(RootConfig.class.getCanonicalName());

	@Bean("messageSource")
	   public MessageSource messageSource() {
	      ReloadableResourceBundleMessageSource messageSource=new ReloadableResourceBundleMessageSource();
	      messageSource.setBasename("classpath:com/pb/tel/messages/messages");
	      messageSource.setDefaultEncoding("UTF-8");
	      messageSource.setUseCodeAsDefaultMessage(true);
	      messageSource.setCacheMillis(100);
	      return messageSource;
	   }

}