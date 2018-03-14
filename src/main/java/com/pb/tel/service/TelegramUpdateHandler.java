package com.pb.tel.service;

import com.pb.tel.data.enums.TelegramButtons;
import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.telegram.*;
import com.pb.tel.service.exception.TelegramException;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private RedisHandler redisHandler;

    public TelegramRequest getTelegramRequest(Update update) throws Exception{
        TelegramRequest telegramRequest = new TelegramRequest();
        User user = getUserFromUpdate(update);
        try {
            UserState userState = redisHandler.getUserState(user.getId());
            checkUpdate(update, userState);
            telegramRequest.setChat_id(user.getId());
            telegramRequest.setText(getResponseMessage(update, userState));
            if (userState == UserState.NEW) {
                InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> InlineKeyboardButtons = new ArrayList<List<InlineKeyboardButton>>();

                InlineKeyboardButton tracking = new InlineKeyboardButton(TelegramButtons.tracking.getButton(), TelegramButtons.tracking.getCode());
                InlineKeyboardButton myPost = new InlineKeyboardButton(TelegramButtons.myPost.getButton(), TelegramButtons.myPost.getCode());
                InlineKeyboardButton adviceButton = new InlineKeyboardButton(TelegramButtons.adviceButton.getButton(), TelegramButtons.adviceButton.getCode());

                List<InlineKeyboardButton> trackings = new ArrayList<InlineKeyboardButton>();
                trackings.add(tracking);
                List<InlineKeyboardButton> myPosts = new ArrayList<InlineKeyboardButton>();
                myPosts.add(myPost);
                List<InlineKeyboardButton> adviceButtons = new ArrayList<InlineKeyboardButton>();
                adviceButtons.add(adviceButton);

                InlineKeyboardButtons.add(trackings);
//                InlineKeyboardButtons.add(myPosts);
//                InlineKeyboardButtons.add(adviceButtons);
                reply_markup.setInline_keyboard(InlineKeyboardButtons);
                telegramRequest.setReply_markup(reply_markup);
            }
        }catch (TelegramException e){
            throw e;

        }catch (Exception e){
            throw new UnresponsibleException(user.getId(), e.getMessage(), e);
        }
        return telegramRequest;
    }

    public void analyseResponse(TelegramResponse telegramResponse, Update update){
        if(telegramResponse.getOk()) {
            updateUserState(getUserFromUpdate(update));
        }
    }

    public void flushUserState(Integer userId){
        redisHandler.setUserState(UserState.NEW, userId);
    }

    private void checkUpdate(Update update, UserState userState){
        if(userState == UserState.WAITING_PRESS_BUTTON && update.getCallback_query() == null){
            redisHandler.setUserState(UserState.WRONG_ANSWER, getUserFromUpdate(update).getId());
        }
    };

    private String getResponseMessage(Update update, UserState userState) throws Exception{
        User user = getUserFromUpdate(update);
        if(user == null){
            throw new UnresponsibleException("USER01", PropertiesUtil.getProperty("USER01"));
        }
        log.log(Level.INFO, "User " + user.getId() + " state : " + userState + " (" + userState.getDescr() + ")");
        String message = messageHandler.getMessage(user, userState, (update.getMessage() != null) ? update.getMessage().getText() : null);
        return message;
    }

    private void updateUserState(User user){
        UserState userState = redisHandler.getUserState(user.getId());
        if(userState == UserState.NEW){
            redisHandler.setUserState(UserState.WAITING_PRESS_BUTTON, user.getId());

        }else if(userState == UserState.WAITING_PRESS_BUTTON){
            if(TelegramButtons.tracking.getCode().equals(user.getCall_back_data())) {
                redisHandler.setUserState(UserState.WAITING_TTN, user.getId());
            }
        }else if(userState == UserState.WAITING_TTN){
            redisHandler.setUserState(UserState.NEW, user.getId());

        }else if(userState == UserState.WRONG_ANSWER){
            redisHandler.setUserState(UserState.NEW, user.getId());
        }
    }

    private User getUserFromUpdate(Update update){
        User user = null;
        if(update.getMessage() != null) {
            user = update.getMessage().getFrom();
        }else if(update.getCallback_query() != null){
            user = update.getCallback_query().getFrom();
            user.setCall_back_data(update.getCallback_query().getData());
        }
        return user;
    }
}
