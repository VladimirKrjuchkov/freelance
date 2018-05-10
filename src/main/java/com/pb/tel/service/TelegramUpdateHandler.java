package com.pb.tel.service;

import com.pb.tel.data.Request;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.enums.TelegramButtons;
import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.telegram.*;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.service.exception.TelegramException;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by vladimir on 05.03.18.
 */

@Service("telegramUpdateHandler")
public class TelegramUpdateHandler extends AbstractUpdateHandler {

    private final Logger log = Logger.getLogger(TelegramUpdateHandler.class.getCanonicalName());

    public TelegramRequest getTelegramRequest(UserAccount userAccount) throws Exception{
        TelegramRequest telegramRequest = new TelegramRequest();
        checkUserAnswer(userAccount);
        try {
            telegramRequest.setChat_id(userAccount.getId());
            telegramRequest.setText(messageHandler.getMessage(userAccount));
            if (userAccount.getUserState() == UserState.NEW ||
                userAccount.getUserState() == UserState.WAITING_SHARE_CONTACT ||
                userAccount.getUserState() == UserState.SEND_WRONG_CONTACT ||
                userAccount.getUserState() == UserState.WAITING_USER_LOCALE) {
                    InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
                    List<List<KeyboardButton>> keyboardButtons = new ArrayList<List<KeyboardButton>>();
                    if(userAccount.getLocale() == null){
                        KeyboardButton ua = new KeyboardButton(TelegramButtons.ua.getButton());
                        KeyboardButton ru = new KeyboardButton(TelegramButtons.ru.getButton());

                        List<KeyboardButton> uas = new ArrayList<KeyboardButton>();
                        uas.add(ua);
                        List<KeyboardButton> rus = new ArrayList<KeyboardButton>();
                        rus.add(ru);

                        keyboardButtons.add(uas);
                        keyboardButtons.add(rus);

                    }else if(userAccount.getRegistered()) {
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
            if(userAccount.getUserState() == UserState.WAITING_TTN ||
               userAccount.getUserState() == UserState.ANONIM_USER ||
               userAccount.getUserState() == UserState.USER_ANSWERD_UNKNOWN ||
               userAccount.getUserState() == UserState.WRONG_ANSWER){
                telegramRequest.setReply_markup(new ReplyKeyboardHide());

            }
        }catch (LogicException e){
            InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
            List<List<KeyboardButton>> keyboardButtons = new ArrayList<List<KeyboardButton>>();
            KeyboardButton callOper = new KeyboardButton(TelegramButtons.callOper.getButton());
            List<KeyboardButton> callOpers = new ArrayList<KeyboardButton>();
            callOpers.add(callOper);
            keyboardButtons.add(callOpers);
            reply_markup.setKeyboard(keyboardButtons);
            throw new TelegramException(e.getDescription(), userAccount.getId(), reply_markup);

        }catch (TelegramException e){
            throw e;

        }catch (Exception e){
            throw new UnresponsibleException(userAccount.getId(), e.getMessage(), e);
        }
        return telegramRequest;
    }

    public void analyseTelegramResponse(TelegramResponse telegramResponse, UserAccount userAccount){
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
                user.setContactId(update.getMessage().getContact().getUser_id());
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
        TelegramRequest message = new TelegramRequest(userAccount.getId(), MessageHandler.getMessage(userAccount.getLocale(),"after_oper_leave_dialog"));
        InlineKeyboardMarkup reply_markup = new InlineKeyboardMarkup();
        List<List<KeyboardButton>> keyboardButtons = new ArrayList<List<KeyboardButton>>();
        KeyboardButton yes = new KeyboardButton(TelegramButtons.yes.getButton());
        KeyboardButton no = new KeyboardButton(TelegramButtons.no.getButton());

        List<KeyboardButton> yeses = new ArrayList<KeyboardButton>();
        yeses.add(yes);
        List<KeyboardButton> nos = new ArrayList<KeyboardButton>();
        nos.add(no);

        keyboardButtons.add(yeses);
        keyboardButtons.add(nos);

        reply_markup.setKeyboard(keyboardButtons);
        message.setReply_markup(reply_markup);
        return message;
    }

}
