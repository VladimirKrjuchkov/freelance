package com.pb.tel.config;

import com.pb.tel.data.Account;
import com.pb.tel.storage.Storage;
import com.pb.tel.storage.StorageExpiry;
import com.pb.tel.storage.redis.RedisDataStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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

	@Autowired
	Environment environment;

	@Bean(name = "accountStorage")
	public Storage<String, Account> getAccount(){
		Storage<String, Account> storage;
		if(Boolean.valueOf(environment.getProperty("redis.enable"))) {
			storage = new RedisDataStorage<>();
		}else {
			storage = new StorageExpiry<>();
		}
		return storage;
	}

	@Bean(name = "registredOpers")
	public List<Account> getRegistredOpers(){
		List<Account> opers = new ArrayList<>();
		return opers;
	}

}
