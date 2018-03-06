package com.pb.tel.service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;

/**
 * Created by vladimir on 05.03.18.
 */
public class RedisStorage {

    private final Logger log = Logger.getLogger(RedisStorage.class.getCanonicalName());

    protected JedisPool jedisPool;

    RedisStorage(JedisPool jedisPool){
        this.jedisPool = jedisPool;
    }

    @PreDestroy
    protected void destroyPool(){
        jedisPool.destroy();
    }

    public byte[] getValue(String key){
        Jedis jedis = null;
        byte[] value = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key.getBytes(Utils.encode));
            jedisPool.returnResource(jedis);
        }catch (JedisConnectionException ex) {
            log.log(Level.SEVERE, "", ex);
            jedisPool.returnBrokenResource(jedis);
        } catch (JedisException ex) {
            log.log(Level.SEVERE, "", ex);
            jedisPool.returnResource(jedis);
        }
        catch (Exception ex) {
            log.log(Level.SEVERE, "", ex);
            if(jedis!=null)
                jedisPool.returnResource(jedis);
        }
        return value;
    }

    public void putValue(String key, byte[] value, Date expiry){
        Jedis jedis = null;
        try {
            int ttlInSeconds = Utils.getSecondsToDate(expiry);
            if(ttlInSeconds<0)ttlInSeconds=10;
            jedis = jedisPool.getResource();
            jedis.setex(key.getBytes(Utils.encode), ttlInSeconds, value);
            jedisPool.returnResource(jedis);
        }catch (JedisConnectionException ex) {
            log.log(Level.SEVERE, "", ex);
            jedisPool.returnBrokenResource(jedis);
        } catch (JedisException ex) {
            log.log(Level.SEVERE, "", ex);
            jedisPool.returnResource(jedis);
        }
        catch (Exception ex) {
            log.log(Level.SEVERE, "", ex);
            if(jedis!=null)
                jedisPool.returnResource(jedis);
        }
    }
}
