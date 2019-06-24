package com.pb.tel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

@Configuration
@ComponentScan(basePackages = "com.pb.tel.controller")
@EnableWebMvc
public class ServletConfig implements WebMvcConfigurer{
//
//	 @Bean("messageSource")
//	   public MessageSource messageSource() {
//		 // CustomReloadableResourceBundleMessageSource messageSource=new CustomReloadableResourceBundleMessageSource();
//	      ReloadableResourceBundleMessageSource messageSource=new ReloadableResourceBundleMessageSource();
//	      messageSource.setBasename("classpath:com/pb/ppls/messages/messages");
//	      messageSource.setDefaultEncoding("UTF-8");
//	      messageSource.setUseCodeAsDefaultMessage(true);
//	      messageSource.setCacheMillis(100);
//	      return messageSource;
//	   }
	 
	 @Bean
	   public LocaleResolver localeResolver() {		 
	      CookieLocaleResolver localeResolver = new CookieLocaleResolver();
	      return localeResolver;
	   }
	 
//	 @Bean
//	   public WebMvcConfigurer corsConfigurer() {
//	       return new WebMvcConfigurerAdapter() {
//	           @Override
//	           public void addCorsMappings(CorsRegistry registry) {
//	               registry.addMapping("/websocket").allowedOrigins("http://10.56.2.228:9997");
//	           }
//	       };
//	   }
	 
	 //Это было для тестов, для загрузки статики из варника
//	 @Override
//	 public void addResourceHandlers(ResourceHandlerRegistry registry) {
//	        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
//	 }
	
/*	 @Override
	   public void addInterceptors(InterceptorRegistry registry) {
	      ThemeChangeInterceptor themeChangeInterceptor = new ThemeChangeInterceptor();
	      themeChangeInterceptor.setParamName("theme");
	      registry.addInterceptor(themeChangeInterceptor);

	      LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
	      localeChangeInterceptor.setParamName("lang");
	      registry.addInterceptor(localeChangeInterceptor);
	   }
	 
	 
	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	    }
	 
	    @Override
	    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	        configurer.enable();
	    }*/
	 
//	    @Bean
//	    public InternalResourceViewResolver jspViewResolver() {
//	        InternalResourceViewResolver bean = new InternalResourceViewResolver();
//	        bean.setPrefix("/WEB-INF/views/");
//	        bean.setSuffix(".jsp");
//	        return bean;
//	    }
	 
	 
	 
//	@Bean
//    public static PropertiesUtil propertyPlaceholder() {
//		PropertiesUtil propertyPlaceholder = new PropertiesUtil();
//		propertyPlaceholder.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");
//		propertyPlaceholder.setIgnoreUnresolvablePlaceholders(true);
//		propertyPlaceholder.setIgnoreResourceNotFound(true);
//		propertyPlaceholder.setFileEncoding("UTF-8");
//		Resource [] resource = new Resource[]{new ClassPathResource("com/pb/pmk/config/messagges.txt"), 
//											  new FileSystemResource("${configsDirectoryLocation}/messagges.txt"),
//											  new ClassPathResource("com/pb/pmk/config/app.properties"),
//											  new FileSystemResource("${configsDirectoryLocation}/app.properties")};
//		propertyPlaceholder.setLocations(resource);		  
//        return propertyPlaceholder;
//	}
}
