package com.pb.tel.config;

import com.pb.tel.data.AdminAccount;
import com.pb.tel.storage.Storage;
import com.pb.tel.storage.StorageExpiry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Configuration
@EnableTransactionManagement
//@EnableCaching
@EnableWebSocket
public class ServiceConfig {

	private static final Logger log = Logger.getLogger(ServiceConfig.class.getCanonicalName());

	@Bean(name = "adminStorage")
	public Storage<String, AdminAccount> getSesionStorage(){
		Storage<String, AdminAccount> storage = new StorageExpiry<>();
		return storage;
	}

	@Bean(name = "freeOpers")
	public List<String> getFreeOpers(){
		List<String> opers = new ArrayList<String>();
		return opers;
	}
//
//	@Bean(name = "sessionStorage")
//	public Storage<String, UserAccount> getSesionStorage(){
//		Storage<String, UserAccount> storage = new SessionStorageRedis();
//		//Storage<String, UserAccount> storage = new StorageExpiry<>();
//		return storage;
//	}

//	@Bean(name = "dataSource")
//	public DataSource getDataSource()throws Exception{
//		ComboPooledDataSource dataSource = new ComboPooledDataSource();
//		dataSource.setDriverClass(environment.getProperty("tel.jdbcDriver"));
//		dataSource.setJdbcUrl(environment.getProperty("tel.jdbcUrl"));
//		dataSource.setInitialPoolSize(0);
//		dataSource.setMinPoolSize(0);
//		dataSource.setMaxPoolSize(25);
//		dataSource.setMaxIdleTime(60);
//		dataSource.setAcquireIncrement(1);
//		dataSource.setAcquireRetryAttempts(5);
//		dataSource.setAcquireRetryDelay(1000);
//		dataSource.setCheckoutTimeout(0);
//		dataSource.setIdleConnectionTestPeriod(5);
//		dataSource.setPreferredTestQuery("select 1");
//
//		Properties prop = new Properties();
//		prop.put("user", environment.getProperty("ppls.user"));
//		prop.put("password", environment.getProperty("ppls.password"));
//		dataSource.setProperties(prop);
//		return dataSource;
//
//	}

//	@Bean(name = "emf")
//	public LocalContainerEntityManagerFactoryBean getLocalContainerEntityManagerFactoryBean()throws Exception{
//		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//		log.info("emf1 :"+emf);
//		emf.setDataSource(getDataSource());
//		emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//		emf.setPackagesToScan("com.pb.ppls.data");
//
//		Properties prop = new Properties();
//		prop.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
//		prop.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//		prop.setProperty("hibernate.max_fetch_depth", "3");
//		prop.setProperty("hibernate.jdbc.fetch_size", "50");
//		prop.setProperty("hibernate.jdbc.batch_size", "20");
//		prop.setProperty("hibernate.batch_fetch_style", "DYNAMIC");
//		prop.setProperty("hibernate.show_sql", "true");//
//		prop.setProperty("hibernate.format_sql", "true");//
//		prop.setProperty("hibernate.cache.use_second_level_cache", "false");
//
//		prop.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
//		prop.setProperty("hibernate.cache.use_second_level_cache", "true");
//		prop.setProperty("net.sf.ehcache.configurationResourceName", "com/pb/ppls/config/ehcache.xml");
//
//		emf.setJpaProperties(prop);
//		return emf;
//	}

//	@Bean
//	public JpaTransactionManager getTransactionManager(EntityManagerFactory emf){
//		JpaTransactionManager transactionManager = new JpaTransactionManager(emf);
//		return transactionManager;
//	}


//	@Bean
//	public EhCacheManagerFactoryBean ehcacheFactoryBean(){
//		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
//		ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("com/pb/ppls/config/ehcache.xml"));
//		ehCacheManagerFactoryBean.setShared(true);
//		return ehCacheManagerFactoryBean;
//	}

//	@Bean
//	public CacheManager cacheManager(EhCacheManagerFactoryBean ehcacheFactoryBean){
//		EhCacheCacheManager cacheManager = new EhCacheCacheManager(ehcacheFactoryBean.getObject());
//		return cacheManager;
//	}

}
