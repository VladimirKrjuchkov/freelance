package com.pb.tel.service;

import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.telegram.*;
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

    public String getResponseMessage(Update update){

        Integer userId = update.getMessage().getFrom().getId();
        UserState userState = redisHandler.getUserState(userId);
        log.log(Level.INFO, "User " + userId + " state : " + userState + " (" + userState.getDescr() + ")");
        String rowMessage = messageHandler.getRowMessageByUserState(userState);
        String message = fillInMessage(rowMessage, update.getMessage().getFrom());
        log.log(Level.INFO, "User " + userId + " message : " + message);
        return message;
    }

    public TelegramRequest getTelegramRequest(String message, Integer id){
        UserState userState = redisHandler.getUserState(id);
        TelegramRequest telegramRequest = new TelegramRequest();
        if(userState == UserState.NEW){
            telegramRequest.setChat_id(id);
            telegramRequest.setText(message);
            InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> InlineKeyboardButtons = new ArrayList<List<InlineKeyboardButton>>();

            InlineKeyboardButton tracking = new InlineKeyboardButton("\uD83D\uDE9A Знайти посилку", "tracking");
            InlineKeyboardButton myPost = new InlineKeyboardButton("\uD83D\uDCEB Мій поштомат", "myPost");
            InlineKeyboardButton adviceButton = new InlineKeyboardButton("\uD83D\uDE1C Рекомендувати другу", "adviceButton");

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
        Integer id = telegramResponse.getResult().getChat().getId();
        UserState userState = redisHandler.getUserState(id);
        if(telegramResponse.getOk() && userState == UserState.NEW){
            redisHandler.setUserState(UserState.WAITING_PRESS_BUTTON, id);
        }
    }

    private String fillInMessage(String rowMessage, User user){
        rowMessage = rowMessage.replace("{user_first_name}", user.getFirst_name());
        rowMessage = rowMessage.replace("{user_last_name}", user.getLast_name());
        return rowMessage;
    }
}
