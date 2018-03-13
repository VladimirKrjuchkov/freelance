package com.pb.tel.service;

import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.novaposhta.NovaPoshtaResponse;
import com.pb.tel.data.telegram.User;
import com.pb.tel.service.exception.TelegramException;
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
        return fillInMessage(getRowMessageByUserState(userState, user, userText), user);
    }

    /*Костыльный метод который будет замене походом на БД*/
    private String getRowMessageByUserState(UserState userState, User user, String userText) throws Exception{
        if(userState == UserState.NEW) {
            return "{user_first_name}, чим можу бути корисним сьогодні?\uD83D\uDE0F";
        }
        if(userState == UserState.WAITING_PRESS_BUTTON){
            if("tracking".equals(user.getCall_back_data())) {
                return "Вкажіть, будь ласка, номер вашого відправлення";
            }
        }
        if(userState == UserState.WAITING_TTN){
            NovaPoshtaResponse response = novaPoshtaAPIHandler.getTrackingByTTN(userText);
            String message = "";
            if(response.getSuccess()) {
                if (!userText.equals(response.getData().get(0).getNumber())) {
                    throw new TelegramException("Вибачте але трекінг недоступний, спробуйте ще раз", user.getId());
                }
                message = "{user_first_name}, статус вашого вiдправлення " + response.getData().get(0).getNumber() + ": " + response.getData().get(0).getStatus();
            }else{
                if("20001401442".equals(response.getErrorCodes().get(0))){
                    throw new TelegramException("Номер відправлення вказано невірно, спробуйте ще раз", user.getId());
                }
            }
            return message;
        }
        if(userState == UserState.WRONG_ANSWER) {
            return "{user_first_name}, нажаль я не зрозумiв вашу вiдповiдь\uD83D\uDE14";
        }
        return null;
    }

    private String fillInMessage(String rowMessage, User user){
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