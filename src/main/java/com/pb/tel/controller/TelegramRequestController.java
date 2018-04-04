package com.pb.tel.controller;

import com.pb.tel.data.Mes;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.channels.ChannelsRequest;
import com.pb.tel.data.channels.Data;
import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.telegram.*;
import com.pb.tel.service.*;
import com.pb.tel.service.exception.ChannelsException;
import com.pb.tel.service.exception.TelegramException;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import com.pb.util.zvv.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Resource
    private TelegramUpdateHandler telegramUpdateHandler;

    @Resource
    private ChannelsUpdateHandler channelsUpdateHandler;

    @Autowired
    private TelegramConnector telegramConnector;

    @Autowired
    private ChannelsConnector channelsConnector;

    @RequestMapping(value = "/update")
    @ResponseBody
    public void update(@RequestBody Update update) throws Exception{
        User user = telegramUpdateHandler.getUserFromUpdate(update);
        UserAccount userAccount = userAccountStore.getValue(user.getId());
        if(userAccount == null) {
            userAccount = new UserAccount(user.getId());
        }
        userAccount.setFirstName(user.getFirst_name());
        userAccount.setLastName(user.getLast_name());
        userAccount.setUserName(user.getUsername());
        userAccount.setCallBackData(user.getText());
        userAccount.setUserText(user.getText());
        userAccount.setUdid(user.getBot_id());
        userAccount.setReqId(update.getUpdate_id());
        userAccount.setPhone(user.getPhone());
        userAccount.setMessenger(user.getMessenger());
        telegramUpdateHandler.registateUser(userAccount);
        userAccountStore.putValue(user.getId(), userAccount, Utils.getDateAfterSeconds(180));

        if(userAccount.getUserState() == UserState.JOIN_TO_DIALOG){
            channelsConnector.doRequest(channelsUpdateHandler.deligateMessage(userAccount), PropertiesUtil.getProperty("channels_api_request_url") + userAccount.getToken());
        }else {
            TelegramResponse response = telegramConnector.sendRequest(telegramUpdateHandler.getTelegramRequest(user.getId()));
            telegramUpdateHandler.analyseResponseTelegramResponse(response, userAccount);
        }
    }

    @RequestMapping(value = "/channels/update")
    @ResponseBody
    public void channelsUpdate(@RequestBody ChannelsRequest channelsRequest) throws Exception{
        UserAccount userAccount = channelsUpdateHandler.getUserAccountByChannelId(((Data)channelsRequest.getData()).getChannelId());
        if("msg".equals(channelsRequest.getAction()) && "o".equals(channelsRequest.getData().getUser().getType())) {
            userAccount.setUserText(channelsRequest.getData().getText());
            telegramConnector.sendRequest(telegramUpdateHandler.deligateMessage(userAccount));
        }else if("channelLeave".equals(channelsRequest.getAction()) && "o".equals(channelsRequest.getData().getUser().getType())){
            TelegramResponse telegramResponse = telegramConnector.sendRequest(telegramUpdateHandler.leaveDialog(userAccount));
            if(telegramResponse.getOk()) {
                channelsUpdateHandler.flushUserState(telegramResponse.getResult().getChat().getId());
            }
        }
    }

    @ExceptionHandler(UnresponsibleException.class)
    @ResponseBody
    public void unresponsibleException(UnresponsibleException e){
        channelsUpdateHandler.flushUserState(e.getId());
        log.log(Level.SEVERE, e.getDescription(), e);
    }

    @ExceptionHandler(TelegramException.class)
    @ResponseBody
    public void telegramExceptionHandler(TelegramException e) throws Exception {
        TelegramRequest message = new TelegramRequest(e.getUserId(), e.getDescription());
        message.setReply_markup(e.getInlineKeyboardMarkup());
        log.log(Level.SEVERE, e.getDescription(), e);
        telegramConnector.sendRequest(message);
    }

    @ExceptionHandler(ChannelsException.class)
    @ResponseBody
    public void channelsExceptionHandler(ChannelsException e) throws Exception {
        ChannelsRequest channelsRequest = new ChannelsRequest();
        channelsRequest.setAction("msg");
        channelsRequest.setReqId(e.getReqId());
        Data data = new Data();
        data.setCompanyId(PropertiesUtil.getProperty("channels_company_id"));
        data.setChannelId(e.getChannelId());
        data.setText(e.getDescription());
        channelsRequest.setData(data);
        channelsConnector.doRequest(channelsRequest, PropertiesUtil.getProperty("channels_api_request_url") + e.getToken());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public void exceptionHandler(Exception e){
        log.log(Level.SEVERE, "TelegramRequestController :: exceptionHandler", e);
    }

}
