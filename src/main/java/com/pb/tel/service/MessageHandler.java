package com.pb.tel.service;

import com.pb.tel.data.UserAccount;
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

    @Autowired
    private ChannelsAPIHandler channelsAPIHandler;

    public String getMessage(UserAccount userAccount) throws Exception{
        return fillInMessageByUserData(getRowMessageByUserState(userAccount), userAccount);
    }

    /*Костыльный метод который будет замене походом на БД*/
    private String getRowMessageByUserState(UserAccount userAccount) throws Exception{
        if(userAccount.getUserState() == UserState.NEW) {
            return PropertiesUtil.getProperty("user_start_new_chat");
        }
        if(userAccount.getUserState() == UserState.WAITING_PRESS_BUTTON){
            if(TelegramButtons.tracking.getButton().equals(userAccount.getCallBackData())) {
                return PropertiesUtil.getProperty("user_choose_tracking");
            }
            if(TelegramButtons.callOper.getButton().equals(userAccount.getCallBackData())) {
                channelsAPIHandler.createChannel(userAccount);
                return PropertiesUtil.getProperty("user_call_oper");
            }
        }
        if(userAccount.getUserState() == UserState.WAITING_TTN){
            if(TelegramButtons.callOper.getButton().equals(userAccount.getCallBackData())) {
                channelsAPIHandler.createChannel(userAccount);
                return PropertiesUtil.getProperty("user_call_oper");
            }else {
                String message = novaPoshtaAPIHandler.getTrackingByTTN(userAccount);
                return PropertiesUtil.getProperty("tracking_response_from_novaposhta") + " " + userAccount.getUserText() + ": " + message;
            }
        }
        if(userAccount.getUserState() == UserState.WRONG_ANSWER) {
            return PropertiesUtil.getProperty("wrong_answer");
        }
        return null;
    }

    public String fillInMessageByUserData(String rowMessage, UserAccount userAccount){
        if(userAccount.getFirstName() != null) {
            rowMessage = rowMessage.replace("{user_first_name}", userAccount.getFirstName());
        }else if(userAccount.getUserName() != null){
            rowMessage = rowMessage.replace("{user_first_name}", userAccount.getUserName());
        }else{
            rowMessage = rowMessage.replace("{user_first_name}", "Шановний користувач");
        }

        if(userAccount.getLastName() != null) {
            rowMessage = rowMessage.replace("{user_last_name}", userAccount.getLastName());
        }
        return rowMessage;
    }
}