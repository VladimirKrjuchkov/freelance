package com.pb.tel.service;

import com.pb.tel.dao.CustomerDao;
import com.pb.tel.data.Request;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.enums.TelegramButtons;
import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.privatmarket.Customer;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import com.pb.util.zvv.storage.Storage;

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


    public void registateUser(UserAccount userAccount){
        List<Customer> customers = customerDaoImpl.getById(Integer.toString(userAccount.getId()));
        if(customers.size() > 0){
            Customer customer = customers.get(0);
            userAccount.setRegistered(true);
            userAccount.setPhone(customer.getPhone());
            userAccount.setIdEkb((customer.getIdEkb() == null) ? null : Integer.toString(customer.getIdEkb()));
        }
        log.log(Level.INFO, "REGISTERED USER : " + userAccount.getRegistered());
    }

    public void flushUserState(Integer userId){
        userAccountStore.removeValue(userId);
    }

    protected void updateUserState(UserAccount userAccount){
        if(userAccount.getUserState() == UserState.NEW || userAccount.getUserState() == UserState.WAITING_SHARE_CONTACT){
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

        }else if(userAccount.getUserState() == UserState.WRONG_ANSWER || userAccount.getUserState() == UserState.ANONIM_USER || userAccount.getUserState() == UserState.WAITING_SHARE_CONTACT){
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
}
