package com.pb.tel.service;

import com.pb.tel.data.enums.TelegramButtons;
import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.novaposhta.NovaPoshtaResponse;
import com.pb.tel.data.telegram.CallbackQuery;
import com.pb.tel.data.telegram.User;
import com.pb.tel.service.exception.TelegramException;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Created by vladimir on 05.03.18.
 */
@Service("messageHandler")
public class MessageHandler {

    private final Logger log = Logger.getLogger(MessageHandler.class.getCanonicalName());

    @Autowired
    private NovaPoshtaAPIHandler novaPoshtaAPIHandler;

    public String getMessage(User user, UserState userState, String userText) throws Exception{
        return fillInMessageByUserData(getRowMessageByUserState(userState, user, userText), user);
    }

    /*Костыльный метод который будет замене походом на БД*/
    private String getRowMessageByUserState(UserState userState, User user, String userText) throws Exception{
        if(userState == UserState.NEW) {
            return PropertiesUtil.getProperty("user_start_new_chat");
        }
        if(userState == UserState.WAITING_PRESS_BUTTON){
            if(TelegramButtons.tracking.getCode().equals(user.getCall_back_data())) {
                return PropertiesUtil.getProperty("user_choose_tracking");
            }
        }
        if(userState == UserState.WAITING_TTN){
            String message = novaPoshtaAPIHandler.getTrackingByTTN(userText, user);
            return PropertiesUtil.getProperty("tracking_response_from_novaposhta") + userText + ": " + message;
        }
        if(userState == UserState.WRONG_ANSWER) {
            return PropertiesUtil.getProperty("wrong_answer");
        }
        return null;
    }

    private String fillInMessageByUserData(String rowMessage, User user){
        if(user.getFirst_name() != null) {
            rowMessage = rowMessage.replace("{user_first_name}", user.getFirst_name());
        }else if(user.getUsername() != null){
            rowMessage = rowMessage.replace("{user_first_name}", user.getUsername());
        }else{
            rowMessage = rowMessage.replace("{user_first_name}", "Шановний користувач");
        }

        if(user.getLast_name() != null) {
            rowMessage = rowMessage.replace("{user_last_name}", user.getLast_name());
        }
        return rowMessage;
    }
}