package com.pb.tel.service;

import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.telegram.*;
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

    public TelegramRequest getTelegramRequest(Update update) throws UnresponsibleException {
        User user = getUserFromUpdate(update);
        TelegramRequest telegramRequest = new TelegramRequest();
        telegramRequest.setChat_id(user.getId());
        telegramRequest.setText(getResponseMessage(update));
        if(redisHandler.getUserState(user.getId()) == UserState.NEW){
            InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> InlineKeyboardButtons = new ArrayList<List<InlineKeyboardButton>>();

            InlineKeyboardButton tracking = new InlineKeyboardButton(PropertiesUtil.getProperty("tracking"), "tracking");
            InlineKeyboardButton myPost = new InlineKeyboardButton(PropertiesUtil.getProperty("myPost"), "myPost");
            InlineKeyboardButton adviceButton = new InlineKeyboardButton(PropertiesUtil.getProperty("adviceButton"), "adviceButton");

            List<InlineKeyboardButton> trackings = new ArrayList<InlineKeyboardButton>();
            trackings.add(tracking);
            List<InlineKeyboardButton> myPosts = new ArrayList<InlineKeyboardButton>();
            myPosts.add(myPost);
            List<InlineKeyboardButton> adviceButtons = new ArrayList<InlineKeyboardButton>();
            adviceButtons.add(adviceButton);

            InlineKeyboardButtons.add(trackings);
            InlineKeyboardButtons.add(myPosts);
            InlineKeyboardButtons.add(adviceButtons);
            reply_markup.setInline_keyboard(InlineKeyboardButtons);
            telegramRequest.setReply_markup(reply_markup);
        }
        return telegramRequest;
    }

    public void analyseResponse(TelegramResponse telegramResponse){
        if(telegramResponse.getOk()) {
            updateUserState(telegramResponse.getResult().getChat());
        }
    }

    private String getResponseMessage(Update update) throws UnresponsibleException {
        User user = getUserFromUpdate(update);
        if(user == null){
            throw new UnresponsibleException("USER01", "Null user");
        }
        log.log(Level.INFO, "User " + user.getId() + " state : " + redisHandler.getUserState(user.getId()) + " (" + redisHandler.getUserState(user.getId()).getDescr() + ")");
        String message = messageHandler.getMessage(user, redisHandler.getUserState(user.getId()));
        log.log(Level.INFO, "User " + user.getId() + " message : " + message);
        return message;
    }

    private void updateUserState(TelegramUser user){
        UserState userState = redisHandler.getUserState(user.getId());
        if(userState == UserState.NEW){
            redisHandler.setUserState(UserState.WAITING_PRESS_BUTTON, user.getId());

        }else if(userState == UserState.WAITING_PRESS_BUTTON){
            if("tracking".equals(user.getCall_back_data())) {
                redisHandler.setUserState(UserState.WAITING_TTN, user.getId());
            }
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
