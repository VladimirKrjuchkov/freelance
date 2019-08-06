package com.pb.tel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.pb.tel.controller")
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ServletConfig implements WebMvcConfigurer{
	   @Bean
	   public LocaleResolver localeResolver() {		 
	      CookieLocaleResolver localeResolver = new CookieLocaleResolver();
	      return localeResolver;
	   }

		@Override
		public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
			StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
			stringHttpMessageConverter.setWriteAcceptCharset(false);  // see SPR-7316
			stringHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
			stringHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_PLAIN, MediaType.TEXT_HTML, MediaType.ALL));
			converters.add(1, stringHttpMessageConverter);
		}
}
