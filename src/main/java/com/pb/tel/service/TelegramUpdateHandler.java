package com.pb.tel.service;

import com.pb.tel.dao.CustomerDao;
import com.pb.tel.data.Request;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.enums.TelegramButtons;
import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.telegram.*;
import com.pb.tel.service.exception.TelegramException;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by vladimir on 05.03.18.
 */

@Service("telegramUpdateHandler")
public class TelegramUpdateHandler extends AbstractUpdateHandler {

    private final Logger log = Logger.getLogger(TelegramUpdateHandler.class.getCanonicalName());

    @Autowired
    private MessageHandler messageHandler;

    public TelegramRequest getTelegramRequest(Integer id) throws Exception{
        UserAccount userAccount = userAccountStore.getValue(id);
        TelegramRequest telegramRequest = new TelegramRequest();
        checkUserAnswer(userAccount);
        try {
            telegramRequest.setChat_id(id);
            telegramRequest.setText(messageHandler.getMessage(userAccount));
            if (userAccount.getUserState() == UserState.NEW || userAccount.getUserState() == UserState.WAITING_SHARE_CONTACT) {
                InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
                List<List<KeyboardButton>> keyboardButtons = new ArrayList<List<KeyboardButton>>();
                if(userAccount.getRegistered()) {
                    KeyboardButton tracking = new KeyboardButton(TelegramButtons.tracking.getButton());
                    KeyboardButton callOper = new KeyboardButton(TelegramButtons.callOper.getButton());

                    List<KeyboardButton> trackings = new ArrayList<KeyboardButton>();
                    trackings.add(tracking);
                    List<KeyboardButton> callOpers = new ArrayList<KeyboardButton>();
                    callOpers.add(callOper);

                    keyboardButtons.add(trackings);
                    keyboardButtons.add(callOpers);

                }else{
                    KeyboardButton register = new KeyboardButton(TelegramButtons.register.getButton());
                    register.setRequest_contact(true);
                    List<KeyboardButton> registers = new ArrayList<KeyboardButton>();
                    registers.add(register);
                    keyboardButtons.add(registers);
                }
                reply_markup.setKeyboard(keyboardButtons);
                telegramRequest.setReply_markup(reply_markup);
            }
            if(userAccount.getUserState() == UserState.WAITING_TTN || userAccount.getUserState() == UserState.ANONIM_USER){
                telegramRequest.setReply_markup(new ReplyKeyboardHide());
            }
        }catch (TelegramException e){
            throw e;

        }catch (Exception e){
            throw new UnresponsibleException(id, e.getMessage(), e);
        }
        return telegramRequest;
    }

    public void analyseResponseTelegramResponse(TelegramResponse telegramResponse, UserAccount userAccount){
        if(telegramResponse.getOk()) {
            updateUserState(userAccount);
        }
    }

    public User getUserFromUpdate(Update update){
        User user = null;
        if(update.getMessage() != null) {
            user = update.getMessage().getFrom();
            user.setText(update.getMessage().getText());
            user.setBot_id(update.getMessage().getFrom().getId());
            if(update.getMessage().getContact() != null){
                user.setText(TelegramButtons.register.getButton());
                user.setPhone(update.getMessage().getContact().getPhone_number());
            }
        }else if(update.getCallback_query() != null){
            user = update.getCallback_query().getFrom();
            user.setCall_back_data(update.getCallback_query().getData());
            user.setText(update.getCallback_query().getMessage().getText());
            user.setBot_id(update.getCallback_query().getMessage().getFrom().getId());
        }
        user.setMessenger("Telegram");
        return user;
    }

    @Override
    public Request deligateMessage(UserAccount userAccount) throws UnresponsibleException {
        TelegramRequest message = new TelegramRequest(userAccount.getId(), userAccount.getUserText());
        message.setReply_markup(new ReplyKeyboardHide());
        return message;
    }

    public TelegramRequest leaveDialog(UserAccount userAccount) throws UnresponsibleException {
        TelegramRequest message = new TelegramRequest(userAccount.getId(), PropertiesUtil.getProperty("oper_leave_dialog"));
        message.setReply_markup(new ReplyKeyboardHide());
        return message;
    }

    private void checkUserAnswer(UserAccount userAccount){
        UserState userState = userAccount.getUserState();
        if(userState == UserState.WAITING_PRESS_BUTTON || userState == UserState.WAITING_SHARE_CONTACT){
            if(userAccount.getCallBackData() == null) {
                userAccount.setUserState((userState == UserState.WAITING_PRESS_BUTTON) ? UserState.WRONG_ANSWER : UserState.ANONIM_USER);
                return;
            }
            for(TelegramButtons telegramButton : TelegramButtons.values()){
                if(telegramButton.getButton().equals(userAccount.getCallBackData())){
                    return;
                }
            }
            userAccount.setUserState((userState == UserState.WAITING_PRESS_BUTTON) ? UserState.WRONG_ANSWER : UserState.ANONIM_USER);
        }
    }

}
