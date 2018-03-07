package com.pb.tel.service;

import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.telegram.User;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Created by vladimir on 05.03.18.
 */
@Service("messageHandler")
public class MessageHandler {

    private final Logger log = Logger.getLogger(MessageHandler.class.getCanonicalName());


    public String getMessage(User user, UserState userState){
        return fillInMessage(getRowMessageByUserState(userState, user), user);
    }

    /*Костыльный метод который будет замене походом на БД*/
    private String getRowMessageByUserState(UserState userState, User user){
        if(userState == UserState.NEW) {
            return "{user_first_name}, чим можу бути корисним сьогодні?\uD83D\uDE0F";
        }
        if(userState == UserState.WAITING_PRESS_BUTTON){
            if("tracking".equals(user.getCall_back_data())) {
                return "Вкажіть, будь ласка, номер вашого відправлення";
            }
        }
        return null;
    }

    private String fillInMessage(String rowMessage, User user){
        rowMessage = rowMessage.replace("{user_first_name}", user.getFirst_name());
        rowMessage = rowMessage.replace("{user_last_name}", user.getLast_name());
        return rowMessage;
    }
}