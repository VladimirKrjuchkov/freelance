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
        if(userAccount.getUserState() == UserState.WAITING_PRESS_BUTTON && userAccount.getCallBackData() == null){
            userAccount.setUserState(UserState.WRONG_ANSWER);
        }
        try {
            telegramRequest.setChat_id(id);
            telegramRequest.setText(getResponseMessage(userAccount));
            if (userAccount.getUserState() == UserState.NEW) {
                InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> InlineKeyboardButtons = new ArrayList<List<InlineKeyboardButton>>();

                InlineKeyboardButton tracking = new InlineKeyboardButton(TelegramButtons.tracking.getButton(), TelegramButtons.tracking.getCode());
                InlineKeyboardButton myPost = new InlineKeyboardButton(TelegramButtons.myPost.getButton(), TelegramButtons.myPost.getCode());
                InlineKeyboardButton adviceButton = new InlineKeyboardButton(TelegramButtons.adviceButton.getButton(), TelegramButtons.adviceButton.getCode());
                InlineKeyboardButton callOper = new InlineKeyboardButton(TelegramButtons.callOper.getButton(), TelegramButtons.callOper.getCode());

                List<InlineKeyboardButton> trackings = new ArrayList<InlineKeyboardButton>();
                trackings.add(tracking);
                List<InlineKeyboardButton> myPosts = new ArrayList<InlineKeyboardButton>();
                myPosts.add(myPost);
                List<InlineKeyboardButton> adviceButtons = new ArrayList<InlineKeyboardButton>();
                adviceButtons.add(adviceButton);
                List<InlineKeyboardButton> callOpers = new ArrayList<InlineKeyboardButton>();
                callOpers.add(callOper);

                InlineKeyboardButtons.add(trackings);
//                InlineKeyboardButtons.add(myPosts);
//                InlineKeyboardButtons.add(adviceButtons);
                InlineKeyboardButtons.add(callOpers);
                reply_markup.setInline_keyboard(InlineKeyboardButtons);
                telegramRequest.setReply_markup(reply_markup);
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
        userAccountStore.flushValue(userId);
    }

    public User getUserFromUpdate(Update update){
        User user = null;
        if(update.getMessage() != null) {
            user = update.getMessage().getFrom();
            user.setText(update.getMessage().getText());
        }else if(update.getCallback_query() != null){
            user = update.getCallback_query().getFrom();
            user.setCall_back_data(update.getCallback_query().getData());
            user.setText(update.getCallback_query().getMessage().getText());
        }
        return user;
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
            if(TelegramButtons.tracking.getCode().equals(userAccount.getCallBackData())) {
                userAccount.setUserState(UserState.WAITING_TTN);
            }else if(TelegramButtons.callOper.getCode().equals(userAccount.getCallBackData())){

            }
        }else if(userAccount.getUserState() == UserState.WAITING_TTN){
            userAccount.setUserState(UserState.NEW);

        }else if(userAccount.getUserState() == UserState.WRONG_ANSWER){
            userAccount.setUserState(UserState.NEW);
        }

        userAccountStore.putValue(userAccount.getId(), userAccount, Utils.getDateAfterSeconds(180));
    }

}
