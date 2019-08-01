package com.pb.tel.service.auth;

import com.pb.tel.data.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by vladimir on 29.07.19.
 */
public class RedisBasedAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    private static final Logger log = Logger.getLogger(RedisBasedAuthorizationCodeServices.class.getCanonicalName());

    private static String prefix = "AUTH_CODE_";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private RandomValueStringGenerator generator = new RandomValueStringGenerator();

    public RedisBasedAuthorizationCodeServices(){
    }

    public RedisBasedAuthorizationCodeServices(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String createAuthorizationCode(OAuth2Authentication authentication) {
        String code = generator.generate();
        String sufix = String.valueOf(System.currentTimeMillis());
        code += sufix.substring(sufix.length()-5);
        log.info("New genereted AuthorizationCode = "+code+ "   for user:"+authentication.getName());
        store(code, authentication);
        return code;
    }

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        Object userAccount ;
        long expire;
        if((userAccount = authentication.getUserAuthentication().getCredentials()) instanceof UserAccount)
            expire = ((UserAccount)userAccount).getMaxInSecondPossibleSessionExpire();
        else
            expire = 180;
        redisTemplate.boundValueOps(prefix+code).set(authentication, expire,  TimeUnit.SECONDS);
        String previewCode = (String)redisTemplate.boundValueOps(prefix+authentication.getName()).get();
        redisTemplate.boundValueOps(prefix+authentication.getName()).set(code, expire,  TimeUnit.SECONDS);//
        if(previewCode!=null)
            redisTemplate.delete(previewCode);

        SecurityContextHolder.clearContext();
    }

    @Override
    public OAuth2Authentication remove(String code) {
        OAuth2Authentication auth = (OAuth2Authentication)redisTemplate.boundValueOps(prefix+code).get();
        redisTemplate.delete(prefix+code);
        if(auth!=null)
            redisTemplate.delete(prefix+auth.getName());
        return auth;
    }
}


