package com.pb.tel.controller;

import com.pb.tel.data.UserAccount;
import com.pb.tel.data.channels.ChannelsRequest;
import com.pb.tel.data.channels.Data;
import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.facebook.*;
import com.pb.tel.data.facebook.Message;
import com.pb.tel.data.telegram.*;
import com.pb.tel.service.*;
import com.pb.tel.service.exception.ChannelsException;
import com.pb.tel.service.exception.FaceBookException;
import com.pb.tel.service.exception.TelegramException;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import com.pb.util.zvv.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 06.03.18.
 */

@Controller
public class TelegramRequestController {

    private final Logger log = Logger.getLogger(TelegramRequestController.class.getCanonicalName());

    @Resource(name="userAccountStore")
    private Storage<String, UserAccount> userAccountStore;

    @Resource
    private TelegramUpdateHandler telegramUpdateHandler;

    @Resource
    private FaceBookUpdateHandler faceBookUpdateHandler;

    @Resource
    private ChannelsUpdateHandler channelsUpdateHandler;

    @Autowired
    private TelegramConnector telegramConnector;

    @Autowired
    private FaceBookConnector faceBookConnector;

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
        userAccount.setReqId(Integer.toString(update.getUpdate_id()));
        userAccount.setPhone(user.getPhone());
        userAccount.setMessenger(user.getMessenger());
        userAccount.setContactId(user.getContactId());
        telegramUpdateHandler.registrateUser(userAccount);
        userAccountStore.putValue(user.getId(), userAccount, Utils.getDateAfterSeconds(180));

        if(userAccount.getUserState() == UserState.JOIN_TO_DIALOG){
            channelsConnector.doRequest(channelsUpdateHandler.deligateMessage(userAccount), PropertiesUtil.getProperty("channels_api_request_url") + userAccount.getToken());
        }else {
            TelegramResponse response = telegramConnector.sendRequest(telegramUpdateHandler.getTelegramRequest(user.getId()));
            telegramUpdateHandler.analyseTelegramResponse(response, userAccount);
        }
    }

    @RequestMapping(value = "/channels/update")
    @ResponseBody
    public void channelsUpdate(@RequestBody ChannelsRequest channelsRequest) throws Exception{
        UserAccount userAccount = channelsUpdateHandler.getUserAccountByChannelId(((Data)channelsRequest.getData()).getChannelId());
        if("msg".equals(channelsRequest.getAction()) && "o".equals(channelsRequest.getData().getUser().getType())) {
            userAccount.setUserText(channelsRequest.getData().getText());
            if("Telegram".equals(userAccount.getMessenger())) {
                telegramConnector.sendRequest(telegramUpdateHandler.deligateMessage(userAccount));
            }else if("Messenger".equals(userAccount.getMessenger())){
                faceBookConnector.sendRequest(faceBookUpdateHandler.deligateMessage(userAccount));
            }else{
                return;
            }
        }else if("channelCreate".equals(channelsRequest.getAction()) && "u".equals(channelsRequest.getData().getUser().getType())){
            userAccount.setSessionId(channelsRequest.getData().getSessionId());
            userAccount.setSessionStartTime(new Date().getTime());
        }else if("channelLeave".equals(channelsRequest.getAction()) && "o".equals(channelsRequest.getData().getUser().getType())){
            userAccount.setSessionEndTime(new Date().getTime());
            if("Telegram".equals(userAccount.getMessenger())) {
                TelegramResponse telegramResponse = telegramConnector.sendRequest(telegramUpdateHandler.leaveDialog(userAccount));
                if (telegramResponse.getOk()) {
                    userAccount.setUserState(UserState.LEAVING_DIALOG);
                    userAccountStore.putValue(userAccount.getId(), userAccount, Utils.getDateAfterSeconds(3600));
                }
            }else if("Messenger".equals(userAccount.getMessenger())){
                FaceBookResponse faceBookResponse = (FaceBookResponse) faceBookConnector.sendRequest(faceBookUpdateHandler.leaveDialog(userAccount));
                if(userAccount.getId().equals(faceBookResponse.getRecipient_id()) && faceBookResponse.getMessage_id() != null){
                    userAccount.setUserState(UserState.LEAVING_DIALOG);
                    userAccountStore.putValue(userAccount.getId(), userAccount, Utils.getDateAfterSeconds(3600));
                }
            }else{
                return;
            }
        }
    }

    @RequestMapping(value = "/facebook/update")
    @ResponseBody
    public String faceBookUpdate(@RequestBody(required = false) FaceBookRequest faceBookRequest,
                                 @RequestParam(value = "hub.mode", required = false) String mode,
                                 @RequestParam(value = "hub.verify_token", required = false) String verify_toke,
                                 @RequestParam(value = "hub.challenge", required = false) String challenge) throws Exception{

        if("subscribe".equals(mode) && PropertiesUtil.getProperty("facebook_update_token").equals(verify_toke) && challenge != null){
            log.log(Level.INFO, "subscribe mode : true");
            return challenge;
        }
        UserAccount userAccount = faceBookUpdateHandler.getUserFromRequest(faceBookRequest);
        userAccountStore.putValue(userAccount.getId(), userAccount, Utils.getDateAfterSeconds(180));

        if(userAccount.getUserState() == UserState.JOIN_TO_DIALOG){
            channelsConnector.doRequest(channelsUpdateHandler.deligateMessage(userAccount), PropertiesUtil.getProperty("channels_api_request_url") + userAccount.getToken());

        }else {
            FaceBookResponse response = (FaceBookResponse) faceBookConnector.sendRequest(faceBookUpdateHandler.getFaceBookRequest(userAccount.getId()));
            faceBookUpdateHandler.analyseFaceBookResponse(response, userAccount);
        }

        return challenge;
    }

    @ExceptionHandler(UnresponsibleException.class)
    @ResponseBody
    public void unresponsibleException(UnresponsibleException e){
        channelsUpdateHandler.flushUserState(e.getId());
        log.log(Level.WARNING, e.getDescription(), e);
    }

    @ExceptionHandler(TelegramException.class)
    @ResponseBody
    public void telegramExceptionHandler(TelegramException e) throws Exception {
        TelegramRequest message = new TelegramRequest(e.getUserId(), e.getDescription());
        message.setReply_markup(e.getInlineKeyboardMarkup());
        telegramConnector.sendRequest(message);
    }

    @ExceptionHandler(FaceBookException.class)
    @ResponseBody
    public void faceBookExceptionHandler(FaceBookException e) throws Exception {
        com.pb.tel.data.facebook.Message message = new Message();
        Messaging messaging = new Messaging();
        Participant recipient = new Participant();
        recipient.setId(e.getUserId());
        messaging.setRecipient(recipient);
        if(e.getAttachment() != null){
            e.getAttachment().getPayload().setText(e.getDescription());
            message.setAttachment(e.getAttachment());
        }else{
            message.setText(e.getDescription());
        }
        messaging.setMessage(message);
        faceBookConnector.sendRequest(messaging);
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
