package com.pb.tel.service;

import com.pb.tel.data.Request;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.enums.TelegramButtons;
import com.pb.tel.data.enums.UserState;
import com.pb.tel.data.facebook.*;
import com.pb.tel.service.exception.FaceBookException;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by vladimir on 12.04.18.
 */

@Service("faceBookUpdateHandler")
public class FaceBookUpdateHandler extends AbstractUpdateHandler{

    private final Logger log = Logger.getLogger(FaceBookUpdateHandler.class.getCanonicalName());

    @Autowired
    private FaceBookConnector faceBookConnector;

    public UserAccount getUserFromRequest(FaceBookRequest faceBookRequest) throws Exception {
        String id = null;
        try {
            id = faceBookRequest.getEntry().get(0).getMessaging().get(0).getSender().getId();

        }catch (Exception e){
            throw new UnresponsibleException("USER04", PropertiesUtil.getProperty("USER04"));
        }
        if(id == null){
            throw new UnresponsibleException("USER01", PropertiesUtil.getProperty("USER01"));
        }
        UserAccount userAccount = userAccountStore.getValue(id);
        if(userAccount == null) {
            userAccount = new UserAccount(id);
            FaceBookUser faceBookUser = faceBookConnector.getUserProfileData(id);
            userAccount.setFirstName(faceBookUser.getFirst_name());
            userAccount.setLastName(faceBookUser.getLast_name());
            userAccount.setMessenger("Messenger");
            userAccount.setRegistered(true);
            userAccount.setUdid(id);
            userAccount.setReqId(UUID.randomUUID().toString());
        }
        if(faceBookRequest.getEntry().get(0).getMessaging().get(0).getPostback() != null){
            if(TelegramButtons.tracking.getCode().equals(faceBookRequest.getEntry().get(0).getMessaging().get(0).getPostback().getPayload())) {
                userAccount.setCallBackData(TelegramButtons.tracking.getButton());
            }else if(TelegramButtons.callOper.getCode().equals(faceBookRequest.getEntry().get(0).getMessaging().get(0).getPostback().getPayload())){
                userAccount.setCallBackData(TelegramButtons.callOper.getButton());
            }else if(TelegramButtons.yes.getCode().equals(faceBookRequest.getEntry().get(0).getMessaging().get(0).getPostback().getPayload())){
                userAccount.setCallBackData(TelegramButtons.yes.getButton());
            }else if(TelegramButtons.no.getCode().equals(faceBookRequest.getEntry().get(0).getMessaging().get(0).getPostback().getPayload())){
                userAccount.setCallBackData(TelegramButtons.no.getButton());
            }
        }
        if(faceBookRequest.getEntry().get(0).getMessaging().get(0).getMessage() != null){
            userAccount.setUserText(faceBookRequest.getEntry().get(0).getMessaging().get(0).getMessage().getText());
        }
        return userAccount;
    }

    public Messaging getFaceBookRequest(String id) throws Exception {
        UserAccount userAccount = userAccountStore.getValue(id);
        checkUserAnswer(userAccount);
        com.pb.tel.data.facebook.Message message = new Message();
        Messaging messaging = new Messaging();
        try {
            Participant recipient = new Participant();
            recipient.setId(id);
            messaging.setRecipient(recipient);
            String text = null;
            text = messageHandler.getMessage(userAccount);
            if (userAccount.getUserState() == UserState.NEW) {
                Attachment attachment = new Attachment();
                attachment.setType("template");
                Payload payload = new Payload();
                payload.setTemplate_type("button");
                payload.setText(text);
                List<Buttons> buttons = new ArrayList<Buttons>();

                Buttons tracking = new Buttons();
                Buttons callOper = new Buttons();

                tracking.setType("postback");
                tracking.setTitle(TelegramButtons.tracking.getButton());
                tracking.setPayload(TelegramButtons.tracking.getCode());

                callOper.setType("postback");
                callOper.setTitle(TelegramButtons.callOper.getButton());
                callOper.setPayload(TelegramButtons.callOper.getCode());

                buttons.add(tracking);
                buttons.add(callOper);

                payload.setButtons(buttons);
                attachment.setPayload(payload);
                message.setAttachment(attachment);
            }else{
                message.setText(text);
            }
            messaging.setMessage(message);

        }catch (LogicException e){
            Attachment attachment = null;
            if("bad_ttn_error".equals(e.getCode()) || "tracking_error".equals(e.getCode())) {
                attachment = new Attachment();
                attachment.setType("template");
                Payload payload = new Payload();
                payload.setTemplate_type("button");
                List<Buttons> buttons = new ArrayList<Buttons>();
                Buttons callOper = new Buttons();
                callOper.setType("postback");
                callOper.setTitle(TelegramButtons.callOper.getButton());
                callOper.setPayload(TelegramButtons.callOper.getCode());
                buttons.add(callOper);
                payload.setButtons(buttons);
                attachment.setPayload(payload);
            }
            throw new FaceBookException(e.getDescription(), userAccount.getId(), attachment);
        }

        return messaging;
    };

    @Override
    public Request deligateMessage(UserAccount userAccount) throws UnresponsibleException {
        com.pb.tel.data.facebook.Message message = new Message();
        Messaging messaging = new Messaging();
        Participant recipient = new Participant();
        recipient.setId(userAccount.getId());
        messaging.setRecipient(recipient);
        message.setText(userAccount.getUserText());
        messaging.setMessage(message);
        return messaging;
    }

    @Override
    public Request leaveDialog(UserAccount userAccount) throws UnresponsibleException {
        com.pb.tel.data.facebook.Message message = new Message();
        Messaging messaging = new Messaging();
        Participant recipient = new Participant();
        recipient.setId(userAccount.getId());
        messaging.setRecipient(recipient);
        Attachment attachment = new Attachment();
        attachment.setType("template");
        Payload payload = new Payload();
        payload.setTemplate_type("button");
        payload.setText(PropertiesUtil.getProperty("after_oper_leave_dialog"));
        List<Buttons> buttons = new ArrayList<Buttons>();

        Buttons yes = new Buttons();
        Buttons no = new Buttons();

        yes.setType("postback");
        yes.setTitle(TelegramButtons.yes.getButton());
        yes.setPayload(TelegramButtons.yes.getCode());

        no.setType("postback");
        no.setTitle(TelegramButtons.no.getButton());
        no.setPayload(TelegramButtons.no.getCode());

        buttons.add(yes);
        buttons.add(no);

        payload.setButtons(buttons);
        attachment.setPayload(payload);
        message.setAttachment(attachment);
        messaging.setMessage(message);
        return messaging;
    }

    public void analyseFaceBookResponse(FaceBookResponse faceBookResponse, UserAccount userAccount){
        if(userAccount.getId().equals(faceBookResponse.getRecipient_id()) && faceBookResponse.getMessage_id() != null) {
            updateUserState(userAccount);
        }
    }
}
