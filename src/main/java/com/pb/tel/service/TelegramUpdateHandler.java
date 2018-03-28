package com.pb.tel.service;

import com.pb.tel.data.UserAccount;
import com.pb.tel.data.enums.TelegramButtons;
import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.telegram.*;
import com.pb.tel.service.exception.TelegramException;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 05.03.18.
 */

@Service("telegramUpdateHandler")
public class TelegramUpdateHandler {

    private final Logger log = Logger.getLogger(TelegramUpdateHandler.class.getCanonicalName());

    @Autowired
    private MessageHandler messageHandler;

    @Resource(name="userAccountStore")
    private Storage<Integer, UserAccount> userAccountStore;

    public TelegramRequest getTelegramRequest(Integer id) throws Exception{
        UserAccount userAccount = userAccountStore.getValue(id);
        TelegramRequest telegramRequest = new TelegramRequest();
        checkUserAnswer(userAccount);
        try {
            telegramRequest.setChat_id(id);
            telegramRequest.setText(getResponseMessage(userAccount));
            if (userAccount.getUserState() == UserState.NEW) {
                InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
                List<List<KeyboardButton>> keyboardButtons = new ArrayList<List<KeyboardButton>>();

                KeyboardButton tracking = new KeyboardButton(TelegramButtons.tracking.getButton());
                KeyboardButton callOper = new KeyboardButton(TelegramButtons.callOper.getButton());

                List<KeyboardButton> trackings = new ArrayList<KeyboardButton>();
                trackings.add(tracking);
                List<KeyboardButton> callOpers = new ArrayList<KeyboardButton>();
                callOpers.add(callOper);

                keyboardButtons.add(trackings);
                keyboardButtons.add(callOpers);

                reply_markup.setKeyboard(keyboardButtons);
                telegramRequest.setReply_markup(reply_markup);
            }
            if(userAccount.getUserState() == UserState.WAITING_TTN){
                telegramRequest.setReply_markup(new ReplyKeyboardHide());
            }
        }catch (TelegramException e){
            throw e;

        }catch (Exception e){
            throw new UnresponsibleException(id, e.getMessage(), e);
        }
        return telegramRequest;
    }

    public void analyseResponse(TelegramResponse telegramResponse, UserAccount userAccount){
        if(telegramResponse.getOk()) {
            updateUserState(userAccount);
        }
    }

    public void flushUserState(Integer userId){
        userAccountStore.removeValue(userId);
    }

    public User getUserFromUpdate(Update update){
        User user = null;
        if(update.getMessage() != null) {
            user = update.getMessage().getFrom();
            user.setText(update.getMessage().getText());
            user.setBot_id(update.getMessage().getFrom().getId());
        }else if(update.getCallback_query() != null){
            user = update.getCallback_query().getFrom();
            user.setCall_back_data(update.getCallback_query().getData());
            user.setText(update.getCallback_query().getMessage().getText());
            user.setBot_id(update.getCallback_query().getMessage().getFrom().getId());
        }
        return user;
    }

    private void checkUserAnswer(UserAccount userAccount){
        if(userAccount.getUserState() == UserState.WAITING_PRESS_BUTTON){
            if(userAccount.getCallBackData() == null) {
                userAccount.setUserState(UserState.WRONG_ANSWER);
            }
            for(TelegramButtons telegramButton : TelegramButtons.values()){
                if(telegramButton.getButton().equals(userAccount.getCallBackData())){
                    return;
                }
            }
            userAccount.setUserState(UserState.WRONG_ANSWER);
        }
    }

    private String getResponseMessage(UserAccount userAccount) throws Exception {
        log.log(Level.INFO, "User " + userAccount.getId() + " state : " + userAccount.getUserState() + " (" + userAccount.getUserState().getDescr() + ")");
        String message = messageHandler.getMessage(userAccount);
        return message;
    }

    private void updateUserState(UserAccount userAccount){
        if(userAccount.getUserState() == UserState.NEW){
            userAccount.setUserState(UserState.WAITING_PRESS_BUTTON);

        }else if(userAccount.getUserState() == UserState.WAITING_PRESS_BUTTON){
            if(TelegramButtons.tracking.getButton().equals(userAccount.getCallBackData())) {
                userAccount.setUserState(UserState.WAITING_TTN);
            }else if(TelegramButtons.callOper.getButton().equals(userAccount.getCallBackData())){
                userAccount.setUserState(UserState.WAITING_OPER);
            }

        }else if(userAccount.getUserState() == UserState.WAITING_TTN){
            userAccount.setUserState(UserState.NEW);

        }else if(userAccount.getUserState() == UserState.WRONG_ANSWER){
            userAccount.setUserState(UserState.NEW);
        }

        userAccountStore.putValue(userAccount.getId(), userAccount, Utils.getDateAfterSeconds(180));
    }

}
