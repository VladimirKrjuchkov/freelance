package com.pb.tel.service;

import com.pb.tel.dao.CustomerDao;
import com.pb.tel.data.Request;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.enums.TelegramButtons;
import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.privatmarket.Customer;
import com.pb.tel.service.exception.TelegramException;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import com.pb.util.zvv.storage.Storage;
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
    protected Storage<Integer, UserAccount> userAccountStore;

    @Resource(name="channelIdByUserIdStore")
    private Storage<String, Integer> channelIdByUserIdStore;

    @Resource(name="customerDaoImpl")
    private CustomerDao customerDaoImpl;

    @Autowired
    protected EkbDataHandler ekbDataHandler;


    public void registrateUser(UserAccount userAccount){
        List<Customer> customers = customerDaoImpl.getById(Integer.toString(userAccount.getId()));
        if(customers.size() > 0){
            Customer customer = customers.get(0);
            userAccount.setRegistered(true);
            userAccount.setPhone(customer.getPhone());
            Integer idEkb = customer.getIdEkb();
            if(idEkb == null){
                tryToSetIdEkb(userAccount, customer);
            }
        }
        log.log(Level.INFO, "REGISTERED USER : " + userAccount.getRegistered());
    }

    public void flushUserState(Integer userId){
        userAccountStore.removeValue(userId);
    }

    protected void updateUserState(UserAccount userAccount){
        if(userAccount.getUserState() == UserState.NEW || userAccount.getUserState() == UserState.WAITING_SHARE_CONTACT || userAccount.getUserState() == UserState.SEND_WRONG_CONTACT){
            if(userAccount.getRegistered()) {
                userAccount.setUserState(UserState.WAITING_PRESS_BUTTON);
            }else{
                userAccount.setUserState(UserState.WAITING_SHARE_CONTACT);
            }

        }else if(userAccount.getUserState() == UserState.WAITING_PRESS_BUTTON){
            if(TelegramButtons.tracking.getButton().equals(userAccount.getCallBackData())) {
                userAccount.setUserState(UserState.WAITING_TTN);
            }else if(TelegramButtons.callOper.getButton().equals(userAccount.getCallBackData())){
                userAccount.setUserState(UserState.JOIN_TO_DIALOG);
                userAccountStore.putValue(userAccount.getId(), userAccount, Utils.getDateAfterSeconds(3600));
                return;
            }

        }else if(userAccount.getUserState() == UserState.WAITING_TTN){
            if(TelegramButtons.callOper.getButton().equals(userAccount.getCallBackData())) {
                userAccount.setUserState(UserState.JOIN_TO_DIALOG);
                userAccountStore.putValue(userAccount.getId(), userAccount, Utils.getDateAfterSeconds(3600));
                return;
            }else {
                userAccount.setUserState(UserState.NEW);
            }

        }else if(userAccount.getUserState() == UserState.WRONG_ANSWER ||
                 userAccount.getUserState() == UserState.ANONIM_USER  ||
                 userAccount.getUserState() == UserState.WAITING_SHARE_CONTACT ||
                 userAccount.getUserState() == UserState.USER_ANSWERD_YES ||
                 userAccount.getUserState() == UserState.USER_ANSWERD_NO ||
                userAccount.getUserState() == UserState.USER_ANSWERD_UNKNOWN){
            userAccount.setUserState(UserState.NEW);

        }

        userAccountStore.putValue(userAccount.getId(), userAccount, Utils.getDateAfterSeconds(180));
    }

    public UserAccount getUserAccountByChannelId(String channelId ) throws UnresponsibleException {
        Integer userId = channelIdByUserIdStore.getValue(channelId);
        if(userId == null){
            throw  new UnresponsibleException("USER02", PropertiesUtil.getProperty("USER02"));
        }
        UserAccount userAccount = userAccountStore.getValue(userId);
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
                        customerDaoImpl.setCustomerWithIdEkb(customer);
                    }
                    log.info(com.pb.util.zvv.logging.MessageHandler.finishMarker);
                }catch (Exception e){
                    log.log(Level.SEVERE, "ERROR WHILE TRY TO SET ID EKB : ", e);
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
        customer.setPhone(Utils.makeEkbPhone(userAccount.getPhone()));
        return customer;
    }
}
