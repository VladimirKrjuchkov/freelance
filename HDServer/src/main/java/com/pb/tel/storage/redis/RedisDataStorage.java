package com.pb.tel.storage.redis;

import com.pb.tel.storage.Storage;
import com.pb.tel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by vladimir on 18.07.19.
 */
public class RedisDataStorage<K, V> implements Storage<K, V> {

    private final Logger log = Logger.getLogger(RedisDataStorage.class.getCanonicalName());

    @Autowired
    private RedisTemplate redisTemplate;

    public RedisDataStorage(){}

    public RedisDataStorage(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public V putValue(K key,  V value, Date expiry){
        long start = System.currentTimeMillis();
        redisTemplate.boundValueOps(key).set(value, Utils.getSecondsToDate(expiry), TimeUnit.SECONDS);
        log.info("putValue at "+(System.currentTimeMillis()-start)+" ms");
        return value;
    }

    public boolean putValues(Map<K, V> data, Date expiry){
        return false;
    }

    public boolean putValues(Map<K, V> data, Map<K, Date> expiry){
        return false;
    }

    public V updateValue(K key, V value){
        long start = System.currentTimeMillis();
        redisTemplate.boundValueOps(key).setIfPresent(value, redisTemplate.getExpire(key), TimeUnit.SECONDS);
        log.info("updateValue at "+(System.currentTimeMillis()-start)+" ms");
        return value;
    }


    public V removeValue(K key){
        long start = System.currentTimeMillis();
        V value = (V)redisTemplate.boundValueOps(key).get();
        boolean removeResult = redisTemplate.delete(key);
        log.info("removeValue at "+(System.currentTimeMillis()-start)+" ms");
        if(removeResult)
            return value;
        return null;
    }

    public V getValue(K key){
        long start = System.currentTimeMillis();
        V value = (V)redisTemplate.boundValueOps(key).get();
        log.info("getValue at "+(System.currentTimeMillis()-start)+" ms");
        return value;
    }

    public V[] getValues(K []key){
        return null;
    }

    public void flushValue(K key){

    }

    public boolean contains(K key){
        long start = System.currentTimeMillis();
        Boolean result = redisTemplate.hasKey(key);
        log.info("contains at "+(System.currentTimeMillis()-start)+" ms");
        return result;
    }
}
