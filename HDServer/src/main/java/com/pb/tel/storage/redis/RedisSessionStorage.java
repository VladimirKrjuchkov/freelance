package com.pb.tel.storage.redis;

import com.pb.tel.data.UserAccount;
import com.pb.tel.storage.Storage;
import com.pb.tel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by vladimir on 26.07.19.
 */
public class RedisSessionStorage implements Storage<String, UserAccount> {

    private final Logger log = Logger.getLogger(RedisSessionStorage.class.getCanonicalName());

    @Autowired
    private RedisTemplate redisTemplate;

    public UserAccount putValue(String key,  UserAccount userAccount, Date expiry){
        long start = System.currentTimeMillis();
        redisTemplate.boundValueOps(key).set(userAccount, Utils.getSecondsToDate(expiry), TimeUnit.SECONDS);
        log.info("putValue at "+(System.currentTimeMillis()-start)+" ms");
        return userAccount;
    }

    public boolean putValues(Map<String, UserAccount> data, Date expiry){
        return false;
    }

    public boolean putValues(Map<String, UserAccount> data, Map<String, Date> expiry){
        return false;
    }

    public UserAccount updateValue(String  key, UserAccount value){
        long start = System.currentTimeMillis();
        redisTemplate.boundValueOps(key).setIfPresent(value, redisTemplate.getExpire(key), TimeUnit.SECONDS);
        log.info("updateValue at "+(System.currentTimeMillis()-start)+" ms");
        return value;
    }


    public UserAccount removeValue(String  key){
        long start = System.currentTimeMillis();
        UserAccount result = (UserAccount)redisTemplate.boundValueOps(key).get();
        boolean removeResult = redisTemplate.delete(key);
        log.info("removeValue at "+(System.currentTimeMillis()-start)+" ms");
        if(removeResult)
            return result;
        return null;
    }

    public UserAccount getValue(String  key){
        long start = System.currentTimeMillis();
        UserAccount userAccount = (UserAccount)redisTemplate.boundValueOps(key).get();
        log.info("getValue at "+(System.currentTimeMillis()-start)+" ms");
        return userAccount;
    }

    public UserAccount[] getValues(String  []key){
        return null;
    }

    public void flushValue(String  key){

    }

    public boolean contains(String  key){
        long start = System.currentTimeMillis();
        Boolean result = redisTemplate.hasKey(key);
        log.info("contains at "+(System.currentTimeMillis()-start)+" ms");
        return result;
    }
}

