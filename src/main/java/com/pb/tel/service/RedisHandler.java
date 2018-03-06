package com.pb.tel.service;

import com.pb.tel.controller.TelegramRequestController;
import com.pb.tel.data.enums.UserState;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 06.03.18.
 */

@Service("redisHandler")
public class RedisHandler {

    private final Logger log = Logger.getLogger(RedisHandler.class.getCanonicalName());

    private static int secondsTtl;

    @Autowired
    private RedisStorage redisStorage;

    static{
        secondsTtl = Integer.valueOf(PropertiesUtil.getProperty("waiting_for_press_button_seconds"));
    }

    public UserState getUserState(Integer userId){
        byte[] userStateFromRedis = redisStorage.getValue(userId.toString());
        String userStateCode = null;
        if(userStateFromRedis == null || "nil".equals(new String(userStateFromRedis))){
            userStateCode = UserState.NEW.getCode();
        }else {
            userStateCode = new String(userStateFromRedis);
        }
        log.log(Level.INFO, "userStateCode = " + userStateCode);
        UserState userState = UserState.getByCode(userStateCode);
        return userState;
    }

    public void setUserState(UserState userState, Integer userId){
        redisStorage.putValue(String.valueOf(userId), UserState.WAITING_PRESS_BUTTON.getCode().getBytes(), Utils.getDateAfterSeconds(secondsTtl));
    }
}
