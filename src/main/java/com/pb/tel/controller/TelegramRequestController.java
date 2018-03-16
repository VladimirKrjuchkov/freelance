package com.pb.tel.controller;

import com.pb.tel.data.Mes;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.telegram.TelegramRequest;
import com.pb.tel.data.telegram.TelegramResponse;
import com.pb.tel.data.telegram.Update;
import com.pb.tel.data.telegram.User;
import com.pb.tel.service.TelegramConnector;
import com.pb.tel.service.TelegramUpdateHandler;
import com.pb.tel.service.Utils;
import com.pb.tel.service.exception.TelegramException;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 06.03.18.
 */

@Controller
public class TelegramRequestController {

    private final Logger log = Logger.getLogger(TelegramRequestController.class.getCanonicalName());

    @Resource(name="userAccountStore")
    private Storage<Integer, UserAccount> userAccountStore;

    @Autowired
    private TelegramUpdateHandler telegramUpdateHandler;

    @Autowired
    private TelegramConnector telegramConnector;

    @RequestMapping(value = "/update")
    @ResponseBody
    public void update(@RequestBody Update update) throws Exception{
        User user = telegramUpdateHandler.getUserFromUpdate(update);
        UserAccount userAccount = userAccountStore.getValue(user.getId());
        if(userAccount == null) {
            userAccount = new UserAccount(user.getId());
            userAccountStore.putValue(user.getId(), userAccount, Utils.getDateAfterSeconds(180));
        }
        userAccount.setFirstName(user.getFirst_name());
        userAccount.setLastName(user.getLast_name());
        userAccount.setUserName(user.getUsername());
        userAccount.setCallBackData(user.getCall_back_data());
        userAccount.setUserText(user.getText());
        userAccountStore.putValue(user.getId(), userAccount, Utils.getDateAfterSeconds(180));
        TelegramResponse response = telegramConnector.sendRequest(telegramUpdateHandler.getTelegramRequest(user.getId()));
        telegramUpdateHandler.analyseResponse(response, userAccount);
    }

    @ExceptionHandler(UnresponsibleException.class)
    @ResponseBody
    public void unresponsibleException(UnresponsibleException e){
        telegramUpdateHandler.flushUserState(e.getId());
        log.log(Level.SEVERE, e.getDescription(), e);
    }

    @ExceptionHandler(TelegramException.class)
    @ResponseBody
    public void telegramExceptionHandler(TelegramException e) throws Exception {
        TelegramRequest message = new TelegramRequest(e.getUserId(), e.getDescription());
        telegramConnector.sendRequest(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public void exceptionHandler(Exception e){
        log.log(Level.SEVERE, "ManagingRequestController :: exceptionHandler", e);
    }

}
