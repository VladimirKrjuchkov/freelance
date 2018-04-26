package com.pb.tel.service;

import com.pb.tel.dao.MessageDao;
import com.pb.tel.data.Request;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.enums.Locale;
import com.pb.tel.data.enums.TelegramButtons;
import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.privatmarket.BotMessage;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 05.03.18.
 */
@Service("messageHandler")
public class MessageHandler extends AbstractUpdateHandler{

    private static final Logger log = Logger.getLogger(MessageHandler.class.getCanonicalName());

    @Autowired
    private NovaPoshtaAPIHandler novaPoshtaAPIHandler;

    @Autowired
    private ChannelsAPIHandler channelsAPIHandler;

    @Autowired ChatOnlineHandler chatOnlineHandler;

    @Autowired
    private MessageDao messageDaoImpl;

    private static Map<String, BotMessage> botMessagesUa = new ConcurrentHashMap<String, BotMessage>();;

    private static Map<String, BotMessage> botMessagesRu = new ConcurrentHashMap<String, BotMessage>();;

    public MessageHandler(MessageDao messageDaoImpl){
        this.messageDaoImpl = messageDaoImpl;
        init();
    }

    public void init(){
        log.log(Level.INFO, "------ START INIT MESSAGES OBJECTS ------");
        List<BotMessage> listBotMessagesUa = messageDaoImpl.getByLang("ua");
        List<BotMessage> listBotMessagesRu = messageDaoImpl.getByLang("ru");
        log.log(Level.INFO, "listBotMessagesUa length : " + listBotMessagesUa.size());
        log.log(Level.INFO, "listBotMessagesRu length : " + listBotMessagesRu.size());
        for(BotMessage botMessage : listBotMessagesUa){
            botMessagesUa.put(botMessage.getCode(), botMessage);
        }
        for(BotMessage botMessage : listBotMessagesRu){
            botMessagesRu.put(botMessage.getCode(), botMessage);
        }
        log.log(Level.INFO, "------ FINISH INIT MESSAGES OBJECTS ------");
    }

    public String getMessage(UserAccount userAccount) throws Exception{
        log.log(Level.INFO, "User " + userAccount.getId() + " state : " + userAccount.getUserState() + " (" + userAccount.getUserState().getDescr() + ")");
        return fillInMessageByUserData(getRowMessageByUserState(userAccount), userAccount);
    }

    /*Костыльный метод который будет замене походом на БД*/
    private String getRowMessageByUserState(UserAccount userAccount) throws Exception{
        if(userAccount.getUserState() == UserState.NEW) {
            if(userAccount.getLocale() == null){
                return getMessage(userAccount.getLocale(), "unknown_locale_user_start_new_chat");

            }else if(userAccount.getRegistered()) {
                return getMessage(userAccount.getLocale(), "user_start_new_chat");

            }else{
                return getMessage(userAccount.getLocale(), "unregistered_user_start_new_chat");
            }
        }
        if(userAccount.getUserState() == UserState.WAITING_PRESS_BUTTON){
            if(TelegramButtons.tracking.getButton().equals(userAccount.getCallBackData())) {
                return getMessage(userAccount.getLocale(), "user_choose_tracking");
            }
            if(TelegramButtons.callOper.getButton().equals(userAccount.getCallBackData())) {
                channelsAPIHandler.callOper(userAccount);
                return getMessage(userAccount.getLocale(), "user_call_oper");
            }
        }
        if(userAccount.getUserState() == UserState.WAITING_SHARE_CONTACT){
            if(TelegramButtons.register.getButton().equals(userAccount.getCallBackData())) {
                userAccount.setRegistered(registerNewCustomer(userAccount));
                return getMessage(userAccount.getLocale(), "user_start_new_chat");
            }
        }
        if(userAccount.getUserState() == UserState.WAITING_USER_LOCALE){
            if(TelegramButtons.ua.getButton().equals(userAccount.getCallBackData())) {
                userAccount.setLocale(Locale.UA);
            }else if(TelegramButtons.ru.getButton().equals(userAccount.getCallBackData())){
                userAccount.setLocale(Locale.RU);
            }
            if(userAccount.getRegistered()) {
                return getMessage(userAccount.getLocale(), "user_start_new_chat");

            }else{
                return getMessage(userAccount.getLocale(), "unregistered_user_start_new_chat");
            }
        }
        if(userAccount.getUserState() == UserState.WAITING_TTN){
            if(TelegramButtons.callOper.getButton().equals(userAccount.getCallBackData())) {
                channelsAPIHandler.callOper(userAccount);
                return getMessage(userAccount.getLocale(), "user_call_oper");
            }else {
                String message = novaPoshtaAPIHandler.getTrackingByTTN(userAccount);
                return getMessage(userAccount.getLocale(), "tracking_response_from_novaposhta") + " " + userAccount.getUserText() + ": " + message;
            }
        }
        if(userAccount.getUserState() == UserState.SEND_WRONG_CONTACT) {
            return getMessage(userAccount.getLocale(), "wrong_contact");
        }
        if(userAccount.getUserState() == UserState.USER_ANSWERD_YES || userAccount.getUserState() == UserState.USER_ANSWERD_NO || userAccount.getUserState() == UserState.USER_ANSWERD_UNKNOWN) {
            chatOnlineHandler.sendStatistic(userAccount);
            return getMessage(userAccount.getLocale(), "thank_you");
        }
        if(userAccount.getUserState() == UserState.WRONG_ANSWER) {
            return getMessage(userAccount.getLocale(), "wrong_answer");
        }
        if(userAccount.getUserState() == UserState.ANONIM_USER) {
            return getMessage(userAccount.getLocale(), "can_not_repeat_dialog");
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

        if(userAccount.getOperName() != null) {
            rowMessage = rowMessage.replace("{oper_name}", userAccount.getOperName());
        }
        return rowMessage;
    }

    @Override
    public Request deligateMessage(UserAccount userAccount) throws UnresponsibleException {
        return null;
    }

    @Override
    public Request leaveDialog(UserAccount userAccount) throws UnresponsibleException {
        return null;
    }

    public static String getMessage(Locale locale, String code) throws UnresponsibleException {
        try{
            if(locale == Locale.RU){
                return URLDecoder.decode(botMessagesRu.get(code).getMessage(), Utils.encode);
            }else{
                return URLDecoder.decode(botMessagesUa.get(code).getMessage(), Utils.encode);
            }

        }catch (UnsupportedEncodingException e){
            throw new UnresponsibleException("COD01", PropertiesUtil.getProperty("COD01"));
        }
    }
}