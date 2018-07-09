package com.pb.tel.service;

import com.pb.tel.data.UserAccount;
import com.pb.tel.data.chatonline.ChatRequest;
import com.pb.tel.data.chatonline.ChatResponse;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 18.04.18.
 */

@Service("chatOnlineHandler")
public class ChatOnlineHandler{

    private final Logger log = Logger.getLogger(ChatOnlineHandler.class.getCanonicalName());

    @Autowired
    ChatOnlineConnector chatOnlineConnector;

    public void sendStatistic(UserAccount userAccount){
        Runnable r = () -> {
                try {
                    log.info("\n==========================   START SEND STATISTIK FOR: " + userAccount.getId() + "    ==========================" + com.pb.util.zvv.logging.MessageHandler.startMarker);
                    ChatRequest chatRequest = new ChatRequest();
                    chatRequest.setMark(userAccount.getMark());
                    chatRequest.setAction("channelSessionAnalytics");
                    chatRequest.setChannelId(userAccount.getChannelId());
                    chatRequest.setSessionId(userAccount.getSessionId());
                    chatRequest.setSessionStartTime(userAccount.getSessionStartTime());
                    chatRequest.setSessionEndTime(userAccount.getSessionEndTime());
                    ChatResponse chatResponse = chatOnlineConnector.sendRequest(chatRequest);
                    if(!"success".equals(chatResponse.getResult())){
                        throw new UnresponsibleException("CO01", PropertiesUtil.getProperty("CO01"));
                    }

                } catch (UnresponsibleException e) {
                    log.log(Level.SEVERE, e.getDescription(), e);
                } catch (Exception e) {
                    log.log(Level.SEVERE, "ERROR WHILE SEND STATISTIK TO CHAT ONLINE :: ", e);
                }finally {
                    log.info(com.pb.util.zvv.logging.MessageHandler.finishMarker);
                }
            };
        new Thread(r).start();
    }
}
