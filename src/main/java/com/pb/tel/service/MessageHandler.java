package com.pb.tel.service;

import com.pb.tel.dao.CustomerDao;
import com.pb.tel.data.Request;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.enums.TelegramButtons;
import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.novaposhta.NovaPoshtaResponse;
import com.pb.tel.data.privatmarket.Customer;
import com.pb.tel.data.telegram.CallbackQuery;
import com.pb.tel.data.telegram.User;
import com.pb.tel.service.exception.TelegramException;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 05.03.18.
 */
@Service("messageHandler")
public class MessageHandler extends AbstractUpdateHandler{

    private final Logger log = Logger.getLogger(MessageHandler.class.getCanonicalName());

    @Autowired
    private NovaPoshtaAPIHandler novaPoshtaAPIHandler;

    @Autowired
    private EkbDataHandler ekbDataHandler;

    @Autowired
    private ChannelsAPIHandler channelsAPIHandler;

    @Resource(name="customerDaoImpl")
    private CustomerDao customerDaoImpl;

    public String getMessage(UserAccount userAccount) throws Exception{
        log.log(Level.INFO, "User " + userAccount.getId() + " state : " + userAccount.getUserState() + " (" + userAccount.getUserState().getDescr() + ")");
        return fillInMessageByUserData(getRowMessageByUserState(userAccount), userAccount);
    }

    /*Костыльный метод который будет замене походом на БД*/
    private String getRowMessageByUserState(UserAccount userAccount) throws Exception{
        if(userAccount.getUserState() == UserState.NEW) {
            if(userAccount.getRegistered()) {
                return PropertiesUtil.getProperty("user_start_new_chat");
            }else{
                return PropertiesUtil.getProperty("unregistered_user_start_new_chat");
            }
        }
        if(userAccount.getUserState() == UserState.WAITING_PRESS_BUTTON){
            if(TelegramButtons.tracking.getButton().equals(userAccount.getCallBackData())) {
                return PropertiesUtil.getProperty("user_choose_tracking");
            }
            if(TelegramButtons.callOper.getButton().equals(userAccount.getCallBackData())) {
                channelsAPIHandler.callOper(userAccount);
                return PropertiesUtil.getProperty("user_call_oper");
            }
        }
        if(userAccount.getUserState() == UserState.WAITING_SHARE_CONTACT){
            if(TelegramButtons.register.getButton().equals(userAccount.getCallBackData())) {
                userAccount.setRegistered(registerNewCustomer(userAccount));
                return PropertiesUtil.getProperty("user_start_new_chat");
            }
        }
        if(userAccount.getUserState() == UserState.WAITING_TTN){
            if(TelegramButtons.callOper.getButton().equals(userAccount.getCallBackData())) {
                channelsAPIHandler.callOper(userAccount);
                return PropertiesUtil.getProperty("user_call_oper");
            }else {
                String message = novaPoshtaAPIHandler.getTrackingByTTN(userAccount);
                return PropertiesUtil.getProperty("tracking_response_from_novaposhta") + " " + userAccount.getUserText() + ": " + message;
            }
        }
        if(userAccount.getUserState() == UserState.WRONG_ANSWER) {
            return PropertiesUtil.getProperty("wrong_answer");
        }
        if(userAccount.getUserState() == UserState.ANONIM_USER) {
            return PropertiesUtil.getProperty("can_not_repeat_dialog");
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

    private boolean registerNewCustomer(UserAccount userAccount){
        new Thread(new Runnable() {
            public void run() {
                try{
                    Integer idEkb = ekbDataHandler.getEkbIdByPhone(userAccount.getPhone());
                    userAccount.setIdEkb((idEkb == null) ? null : Integer.toString(idEkb));
                    customerDaoImpl.addCustomer(getCustomerFromUserAccount(userAccount));

                    }catch (Exception e){
                        log.log(Level.SEVERE, "ERROR WHILE REGISER NEW CUSTOMER : ", e);
                    }
                }
            }).start();

        return true;
    }

    private Customer getCustomerFromUserAccount(UserAccount userAccount) throws TelegramException, UnresponsibleException {
        Customer customer = new Customer();
        if (userAccount.getId() == null) {
            throw new UnresponsibleException("USER01", PropertiesUtil.getProperty("USER01"));
        }
        if (userAccount.getMessenger() == null || userAccount.getPhone() == null) {
            flushUserState(userAccount.getId());
            throw new TelegramException(PropertiesUtil.getProperty("ident_error"), userAccount.getId());
        }
        customer.setExtId(Integer.toString(userAccount.getId()));
        customer.setIdEkb((userAccount.getIdEkb() == null) ? null : Integer.parseInt(userAccount.getIdEkb()));
        customer.setMessenger(userAccount.getMessenger());
        customer.setPhone(userAccount.getPhone());

        return customer;
    }

    @Override
    public Request deligateMessage(UserAccount userAccount) throws UnresponsibleException {
        return null;
    }
}