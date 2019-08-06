package com.pb.tel.config;

//import com.mchange.v2.c3p0.ComboPooledDataSource;

//import com.mchange.v2.c3p0.ComboPooledDataSource;

import com.pb.tel.data.UserAccount;
import com.pb.tel.storage.Storage;
import com.pb.tel.storage.StorageExpiry;
import com.pb.tel.storage.redis.RedisDataStorage;
import com.pb.tel.storage.redis.RedisSessionStorage;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

@Configuration
@EnableTransactionManagement
@EnableCaching
//@EnableWebSocket
public class ServiceConfig {

	private static final Logger log = Logger.getLogger(ServiceConfig.class.getCanonicalName());

	@Autowired
	Environment environment;

	@Bean(name = "emf")
	public LocalContainerEntityManagerFactoryBean getLocalContainerEntityManagerFactoryBean()throws Exception{
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		log.info("emf1 :"+emf);
		emf.setDataSource(getDataSource());
		emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		emf.setPackagesToScan("com.pb.tel.data");

		Properties prop = new Properties();
		prop.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
		prop.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		prop.setProperty("hibernate.max_fetch_depth", "3");
		prop.setProperty("hibernate.jdbc.fetch_size", "50");
		prop.setProperty("hibernate.jdbc.batch_size", "20");
		prop.setProperty("hibernate.batch_fetch_style", "DYNAMIC");
		prop.setProperty("hibernate.show_sql", "true");//
		prop.setProperty("hibernate.format_sql", "true");//
		prop.setProperty("hibernate.cache.use_second_level_cache", "false");

		prop.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
		prop.setProperty("hibernate.cache.use_second_level_cache", "true");
		prop.setProperty("net.sf.ehcache.configurationResourceName", "com/pb/tel/config/ehcache.xml");

		emf.setJpaProperties(prop);
		return emf;
	}

	@Bean
	public EhCacheManagerFactoryBean ehcacheFactoryBean(){
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("com/pb/tel/config/ehcache.xml"));
		ehCacheManagerFactoryBean.setShared(true);
		return ehCacheManagerFactoryBean;
	}

	@Bean
	public CacheManager cacheManager(EhCacheManagerFactoryBean ehcacheFactoryBean){
		EhCacheCacheManager cacheManager = new EhCacheCacheManager(ehcacheFactoryBean.getObject());
		return cacheManager;
	}

	@Bean(name = "dataSource")
	public DataSource getDataSource()throws Exception{
		HikariConfig config = new HikariConfig();
		config.setDriverClassName(environment.getProperty("tel.jdbcDriver"));
		config.setJdbcUrl(environment.getProperty("tel.jdbcUrl"));
		config.setUsername(environment.getProperty("tel.user"));
		config.setPassword(environment.getProperty("tel.password"));
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("leakDetectionThreshold", "4000");
		config.addDataSourceProperty("assumeMinServerVersion", "9.0");
		config.addDataSourceProperty("socketTimeout", "30");
		config.addDataSourceProperty("connectTimeout", "10");
		//config.setAutoCommit(false);
		config.setMaximumPoolSize(10);
		HikariDataSource connectionPool = new HikariDataSource(config);
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
//		prop.put("user", environment.getProperty("tel.user"));
//		prop.put("password", environment.getProperty("tel.password"));
//		dataSource.setProperties(prop);
//		return dataSource;
		return connectionPool;
	}

	@Bean(name = "dataStorage")
	public Storage<?, ?> getDataStorage(){
		Storage<?, ?> storage;
		if(Boolean.valueOf(environment.getProperty("redis.enable"))) {
			storage = new RedisDataStorage<>();
		}else {
			storage = new StorageExpiry<>();
		}
		return storage;
	}

	@Bean(name = "sessionStorage")
	public Storage<String, UserAccount> getSesionStorage(){
		Storage<String, UserAccount> storage;
		if(Boolean.valueOf(environment.getProperty("redis.enable"))) {
			storage = new RedisSessionStorage();
		}else{
			storage = new StorageExpiry<>();
		}

		return storage;
	}

	@Bean(name = "registredOpers")
	public List<UserAccount> getRegistredOpers(){
		List<UserAccount> opers = new ArrayList<>();
		return opers;
	}

	@Bean
	public JpaTransactionManager getTransactionManager(EntityManagerFactory emf){
		JpaTransactionManager transactionManager = new JpaTransactionManager(emf);
		return transactionManager;
	}
}
