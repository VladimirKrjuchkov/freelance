package com.pb.tel.controller;

import com.pb.tel.data.File;
import com.pb.tel.data.Request;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.channels.ChannelsRequest;
import com.pb.tel.data.channels.Data;
import com.pb.tel.data.enums.MessageContent;
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
import com.pb.util.zvv.storage.StorageExpiry;
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
    private StorageExpiry<String, UserAccount> userAccountStore;

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
    public Request update(@RequestBody Update update,
                          @RequestParam(value = "mode", required = false) String mode) throws Exception{
        User user = telegramUpdateHandler.getUserFromUpdate(update);
        UserAccount userAccount = userAccountStore.getValue(user.getId()+UserState.JOIN_TO_DIALOG.getCode());
        if(userAccount == null) {
            userAccount = userAccountStore.getValue(user.getId());
        }
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
        userAccount.setMessageContent(user.getMessageContent());
        userAccount.setMode(mode);
        userAccount.setFile(telegramUpdateHandler.getFileFromUser(user));
        telegramUpdateHandler.registrateUser(userAccount);


        Request request = null;
        if(userAccount.getUserState() == UserState.JOIN_TO_DIALOG){
            request = channelsUpdateHandler.deligateMessage(userAccount);
            channelsConnector.doRequest(request, PropertiesUtil.getProperty("channels_api_request_url") + userAccount.getToken());
        }else {
            request = telegramUpdateHandler.getTelegramRequest(userAccount);
            TelegramResponse response = telegramConnector.sendRequest(request, "sendMessage");
            telegramUpdateHandler.analyseTelegramResponse(response, userAccount);
        }
        return request;
    }

    @RequestMapping(value = "/channels/update")
    @ResponseBody
    public void channelsUpdate(@RequestBody ChannelsRequest channelsRequest) throws Exception{
//        if(channelsRequest.getData().getTo() != null && !channelsRequest.getData().getTo().getSend()){
        if(channelsRequest.getData().getTo() != null && "o".equals(channelsRequest.getData().getTo().getType())){
            log.log(Level.INFO, channelsRequest.getData().getText() + " This message not for User, only for Operator!");
            return;
        }
        UserAccount userAccount = channelsUpdateHandler.getUserAccountByChannelId(((Data)channelsRequest.getData()).getChannelId());
        if(("msg".equals(channelsRequest.getAction()) || "msgFile".equals(channelsRequest.getAction())) && "o".equals(channelsRequest.getData().getUser().getType()) && userAccount.getUserState()==UserState.JOIN_TO_DIALOG) {
            userAccount.setUserText(channelsRequest.getData().getText());
            if(channelsRequest.getData().getFiles() != null && channelsRequest.getData().getFiles().size() > 0) {
                File file = new File(channelsRequest.getData().getFiles().get(0).getUrl());
                file.setType(channelsRequest.getData().getFiles().get(0).getType());
                userAccount.setFile(file);
                if(file.getType().contains("application")){
                    userAccount.setMessageContent(MessageContent.DOCUMENT);
                }else if(file.getType().contains("image")){
                    userAccount.setMessageContent(MessageContent.PICTURE);
                }
            }else{
                userAccount.setFile(null);
            }
            if("Telegram".equals(userAccount.getMessenger())) {
                if("msg".equals(channelsRequest.getAction())) {
                    telegramConnector.sendRequest(telegramUpdateHandler.deligateMessage(userAccount), "sendMessage");

                }else if("msgFile".equals(channelsRequest.getAction())){
                    if(userAccount.getMessageContent() == MessageContent.PICTURE) {
                        telegramConnector.sendRequest(telegramUpdateHandler.deligateMessage(userAccount), "sendPhoto");
                    }else if(userAccount.getMessageContent() == MessageContent.DOCUMENT){
                        telegramConnector.sendRequest(telegramUpdateHandler.deligateMessage(userAccount), "sendDocument");
                    }
                }
            }else if("Messenger".equals(userAccount.getMessenger())){
                faceBookConnector.sendRequest(faceBookUpdateHandler.deligateMessage(userAccount));
            }else{
                return;
            }
        }else if("channelCreate".equals(channelsRequest.getAction()) && "u".equals(channelsRequest.getData().getUser().getType())){
            userAccount.setSessionId(channelsRequest.getData().getSessionId());
            userAccount.setSessionStartTime(new Date().getTime());
        }else if("channelLeave".equals(channelsRequest.getAction()) && "o".equals(channelsRequest.getData().getUser().getType())) {
            if(channelsUpdateHandler.checkDialogStatus(userAccount) == 0) {
                userAccount.setSessionEndTime(new Date().getTime());
                if ("Telegram".equals(userAccount.getMessenger())) {
                    TelegramResponse telegramResponse = telegramConnector.sendRequest(telegramUpdateHandler.leaveDialog(userAccount), "sendMessage");
                       if (telegramResponse.getOk()) {
                          userAccount.setUserState(UserState.LEAVING_DIALOG);
                       }

                } else if ("Messenger".equals(userAccount.getMessenger())) {
                    FaceBookResponse faceBookResponse = (FaceBookResponse) faceBookConnector.sendRequest(faceBookUpdateHandler.leaveDialog(userAccount));
                    if (userAccount.getId().equals(faceBookResponse.getRecipient_id()) && faceBookResponse.getMessage_id() != null) {
                        userAccount.setUserState(UserState.LEAVING_DIALOG);
                    }

                } else {
                    return;
                }
            }
        }
    }

    @RequestMapping(value = "/facebook/update")
    @ResponseBody
    public Object faceBookUpdate(@RequestBody(required = false) FaceBookRequest faceBookRequest,
                                 @RequestParam(value = "hub.mode", required = false) String mode,
                                 @RequestParam(value = "hub.verify_token", required = false) String verify_toke,
                                 @RequestParam(value = "hub.challenge", required = false) String challenge,
                                 @RequestParam(value = "testMode", required = false) Boolean testMode) throws Exception{

        if("subscribe".equals(mode) && PropertiesUtil.getProperty("facebook_update_token").equals(verify_toke) && challenge != null){
            log.log(Level.INFO, "subscribe mode : true");
            return challenge;
        }
        UserAccount userAccount = faceBookUpdateHandler.getUserFromRequest(faceBookRequest);
        if(testMode != null && testMode){
            userAccount.setMode("test");
        }
        faceBookUpdateHandler.registrateUser(userAccount);
        Messaging request = null;
        if(userAccount.getUserState() == UserState.JOIN_TO_DIALOG){
            channelsConnector.doRequest(channelsUpdateHandler.deligateMessage(userAccount), PropertiesUtil.getProperty("channels_api_request_url") + userAccount.getToken());

        }else {
            request = faceBookUpdateHandler.getFaceBookRequest(userAccount);
            FaceBookResponse response = (FaceBookResponse) faceBookConnector.sendRequest(request);
            faceBookUpdateHandler.analyseFaceBookResponse(response, userAccount);
        }

        return request;
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
        telegramConnector.sendRequest(message, "sendMessage");
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
