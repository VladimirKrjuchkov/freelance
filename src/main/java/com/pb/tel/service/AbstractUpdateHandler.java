package com.pb.tel.service;

import com.pb.tel.dao.CustomerDao;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.enums.Locale;
import com.pb.tel.data.enums.TelegramButtons;
import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.privatmarket.Customer;
import com.pb.tel.service.exception.TelegramException;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import com.pb.util.zvv.storage.Storage;
import com.pb.util.zvv.storage.StorageExpiry;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 03.04.18.
 */

public abstract class AbstractUpdateHandler implements UpdateHandler{

    private final Logger log = Logger.getLogger(AbstractUpdateHandler.class.getCanonicalName());

    @Resource(name="userAccountStore")
    protected StorageExpiry<String, UserAccount> userAccountStore;

    @Resource(name="channelIdByUserIdStore")
    private Storage<String, String> channelIdByUserIdStore;

    @Resource(name="customerDaoImpl")
    private CustomerDao customerDaoImpl;

    @Autowired
    protected EkbDataHandler ekbDataHandler;

    @Autowired
    protected MessageHandler messageHandler;

    public void registrateUser(UserAccount userAccount){
        List<Customer> customers = customerDaoImpl.getById(userAccount.getId());
        if(customers.size() > 0){
            Customer customer = customers.get(0);
            userAccount.setRegistered(true);
            userAccount.setPhone(customer.getPhone());
            Integer idEkb = customer.getIdEkb();
            String locale = customer.getLocale();
            if(userAccount.getLocale() == null) {
                userAccount.setLocale(Locale.getByCode(locale));
            }
            if(idEkb == null){
                tryToSetIdEkb(userAccount, customer);
            }
            if(locale == null && userAccount.getLocale() != null){
                setLocale(userAccount, customer);
            }
        }
        log.log(Level.INFO, "REGISTERED USER : " + userAccount.getRegistered());
    }

    public void flushUserState(String userId){
        userAccountStore.removeValue(userId);
    }

    protected void updateUserState(UserAccount userAccount){
        if(userAccount.getUserState() == UserState.NEW || userAccount.getUserState() == UserState.WAITING_SHARE_CONTACT ||
           userAccount.getUserState() == UserState.SEND_WRONG_CONTACT || userAccount.getUserState() == UserState.WAITING_USER_LOCALE){

            if(userAccount.getLocale() == null){
                userAccount.setUserState(UserState.WAITING_USER_LOCALE);
            }else if(userAccount.getRegistered()) {
                userAccount.setUserState(UserState.WAITING_PRESS_BUTTON);
            }else{
                userAccount.setUserState(UserState.WAITING_SHARE_CONTACT);
            }

        }else if(userAccount.getUserState() == UserState.WAITING_PRESS_BUTTON){
            if(TelegramButtons.tracking.getButton().equals(userAccount.getCallBackData())) {
                userAccount.setUserState(UserState.WAITING_TTN);
            }else if(TelegramButtons.callOper.getButton().equals(userAccount.getCallBackData())){
                userAccountStore.putValue(userAccount.getId() + UserState.JOIN_TO_DIALOG.getCode(), userAccount, Utils.getDateAfterSeconds(3600));
                log.log(Level.INFO, "userAccount was prolonged successfuly, new id : " + userAccount.getId() + userAccount.getUserState().getCode());
                userAccount.setUserState(UserState.JOIN_TO_DIALOG);
            }

        }else if(userAccount.getUserState() == UserState.WAITING_TTN){
            if(TelegramButtons.callOper.getButton().equals(userAccount.getCallBackData())) {
                userAccountStore.putValue(userAccount.getId() + UserState.JOIN_TO_DIALOG.getCode(), userAccount, Utils.getDateAfterSeconds(3600));
                log.log(Level.INFO, "userAccount was prolonged successfuly, new id : " + userAccount.getId() + userAccount.getUserState().getCode());
                userAccount.setUserState(UserState.JOIN_TO_DIALOG);
                return;
            }else {
                userAccount.setUserState(UserState.NEW);
            }

        }else if(userAccount.getUserState() == UserState.WRONG_ANSWER ||
                 userAccount.getUserState() == UserState.ANONIM_USER  ||
                 userAccount.getUserState() == UserState.WAITING_SHARE_CONTACT){
            userAccount.setUserState(UserState.NEW);

        }else if(userAccount.getUserState() == UserState.USER_ANSWERD_YES ||
                 userAccount.getUserState() == UserState.USER_ANSWERD_NO ||
                 userAccount.getUserState() == UserState.USER_ANSWERD_UNKNOWN){
            userAccountStore.removeValue(userAccount.getId() + UserState.JOIN_TO_DIALOG.getCode());
            userAccount.setUserState(UserState.NEW);

        }
        userAccountStore.putValue(userAccount.getId(), userAccount, Utils.getDateAfterSeconds(180));
    }

    public UserAccount getUserAccountByChannelId(String channelId ) throws UnresponsibleException {
        String userId = channelIdByUserIdStore.getValue(channelId);
        if(userId == null){
            throw  new UnresponsibleException("USER02", PropertiesUtil.getProperty("USER02"));
        }
        UserAccount userAccount = userAccountStore.getValue(userId + UserState.JOIN_TO_DIALOG.getCode());
        if(userAccount == null){
            throw  new UnresponsibleException("USER03", PropertiesUtil.getProperty("USER03"));
        }
        return userAccount;
    }

    private void tryToSetIdEkb(UserAccount userAccount, Customer customer){
        new Thread(new Runnable() {
            public void run() {
                try{
                    log.info("\n==========================   START GET DATA FOR: " + userAccount.getPhone() + "    ==========================" + com.pb.util.zvv.logging.MessageHandler.startMarker);
                    Integer idEkb = ekbDataHandler.getEkbIdByPhone(userAccount.getPhone());
                    if(idEkb != null){
                        userAccount.setIdEkb(Integer.toString(idEkb));
                        customer.setIdEkb(idEkb);
                        customerDaoImpl.updateCustomer(customer);
                    }
                    log.info(com.pb.util.zvv.logging.MessageHandler.finishMarker);
                }catch (Exception e){
                    log.log(Level.SEVERE, "ERROR WHILE TRY TO SET ID EKB : ", e);
                }
            }
        }).start();
    }

    private void setLocale(UserAccount userAccount, Customer customer){
        new Thread(new Runnable() {
            public void run() {
                try{
                    log.info("\n==========================   START SET LOCALE FOR: " + userAccount.getPhone() + "    ==========================" + com.pb.util.zvv.logging.MessageHandler.startMarker);
                    if(userAccount.getLocale() != null) {
                        customer.setLocale(userAccount.getLocale().getCode());
                        customerDaoImpl.updateCustomer(customer);
                    }
                    log.info(com.pb.util.zvv.logging.MessageHandler.finishMarker);
                }catch (Exception e){
                    log.log(Level.SEVERE, "ERROR WHILE SET LOCALE : ", e);
                }
            }
        }).start();
    }

    protected boolean registerNewCustomer(UserAccount userAccount){
        new Thread(new Runnable() {
            public void run() {
                try{
                    log.info("\n==========================   START GET DATA FOR: " + userAccount.getPhone() + "    ==========================" + com.pb.util.zvv.logging.MessageHandler.startMarker);
                    Integer idEkb = ekbDataHandler.getEkbIdByPhone(userAccount.getPhone());
                    userAccount.setIdEkb((idEkb == null) ? null : Integer.toString(idEkb));
                    customerDaoImpl.addCustomer(getCustomerFromUserAccount(userAccount));
                    log.info(com.pb.util.zvv.logging.MessageHandler.finishMarker);
                }catch (Exception e){
                    log.log(Level.SEVERE, "ERROR WHILE REGISER NEW CUSTOMER : ", e);
                }
            }
        }).start();

        return true;
    }

    protected void checkUserAnswer(UserAccount userAccount){
        UserState userState = userAccount.getUserState();

        if(userState == UserState.WAITING_PRESS_BUTTON ||
           userState == UserState.WAITING_SHARE_CONTACT ||
           userState == UserState.SEND_WRONG_CONTACT ||
           userState == UserState.WAITING_USER_LOCALE){
            if(userAccount.getCallBackData() == null) {
                userAccount.setUserState((userState == UserState.WAITING_PRESS_BUTTON || userState == UserState.WAITING_USER_LOCALE) ? UserState.WRONG_ANSWER : UserState.ANONIM_USER);
                return;
            }
            if(userState == UserState.WAITING_SHARE_CONTACT){
                if(!userAccount.getId().equals(userAccount.getContactId())){
                    userAccount.setUserState(UserState.SEND_WRONG_CONTACT);
                }else{
                    userAccount.setUserState(UserState.WAITING_SHARE_CONTACT);
                }
            }

            for(TelegramButtons telegramButton : TelegramButtons.values()){
                if(telegramButton.getButton().equals(userAccount.getCallBackData())){
                    return;
                }
            }
            userAccount.setUserState((userState == UserState.WAITING_PRESS_BUTTON || userState == UserState.WAITING_USER_LOCALE) ? UserState.WRONG_ANSWER : UserState.ANONIM_USER);

        }else if(userState == UserState.LEAVING_DIALOG){
            if(userAccount.getCallBackData() == null || (!TelegramButtons.yes.getButton().equals(userAccount.getCallBackData()) && !TelegramButtons.no.getButton().equals(userAccount.getCallBackData()))) {
                userAccount.setUserState(UserState.USER_ANSWERD_UNKNOWN);
                userAccount.setMark("");
            }else if(TelegramButtons.yes.getButton().equals(userAccount.getCallBackData())){
                userAccount.setUserState(UserState.USER_ANSWERD_YES);
                userAccount.setMark("yes");
            }else if(TelegramButtons.no.getButton().equals(userAccount.getCallBackData())){
                userAccount.setUserState(UserState.USER_ANSWERD_NO);
                userAccount.setMark("no");
            }
        }
    }

    private Customer getCustomerFromUserAccount(UserAccount userAccount) throws TelegramException, UnresponsibleException {
        Customer customer = new Customer();
        if (userAccount.getId() == null) {
            throw new UnresponsibleException("USER01", PropertiesUtil.getProperty("USER01"));
        }
        if (userAccount.getMessenger() == null || userAccount.getPhone() == null) {
            flushUserState(userAccount.getId());
            throw new TelegramException(MessageHandler.getMessage(userAccount.getLocale(), "ident_error"), userAccount.getId());
        }
        customer.setExtId(userAccount.getId());
        customer.setIdEkb((userAccount.getIdEkb() == null) ? null : Integer.parseInt(userAccount.getIdEkb()));
        customer.setMessenger(userAccount.getMessenger());
        customer.setPhone(Utils.makeEkbPhone(userAccount.getPhone()));
        customer.setLocale(userAccount.getLocale().getCode());
        return customer;
    }
}
