package com.pb.tel.service;

import com.pb.tel.data.UserAccount;
import com.pb.tel.data.channels.*;
import com.pb.tel.service.exception.TelegramException;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 16.03.18.
 */

@Service("channelsAPIHandler")
public class ChannelsAPIHandler {

    private final Logger log = Logger.getLogger(ChannelsAPIHandler.class.getCanonicalName());

    @Autowired
    ChannelsConnector channelsConnector;

    @Autowired
    TelegramUpdateHandler telegramUpdateHandler;

    @Autowired
    private MessageHandler messageHandler;

    public void createChannel(UserAccount userAccount) throws Exception {
        userAccount.setToken(createToken(userAccount));
        ChannelsRequest channelsRequest = new ChannelsRequest();
        channelsRequest.setAction("channelCreate");
        channelsRequest.setReqId(Integer.toString(userAccount.getReqId()));
        ChannelCreate channelCreate = new ChannelCreate();
        channelCreate.setCompanyId(PropertiesUtil.getProperty("channels_api_key"));
        channelsRequest.setData(channelCreate);
        ChannelsResponse channelsResponse = channelsConnector.doRequest(channelsRequest, PropertiesUtil.getProperty("channels_api_request_url")+userAccount.getToken());
        if("error".equals(channelsResponse.getResult())){
            String message = messageHandler.fillInMessageByUserData(PropertiesUtil.getProperty("channels_create_token_error"), userAccount);
            telegramUpdateHandler.flushUserState(userAccount.getId());
            throw new TelegramException(message, userAccount.getId());
        }
    }

    private String createToken(UserAccount userAccount) throws Exception {
        ChannelsRequest channelsRequest = new ChannelsRequest();
        channelsRequest.setAction("tokenCreate");
        channelsRequest.setReqId(Integer.toString(userAccount.getReqId()));
        TokenCreate tokenCreate = new TokenCreate();
        tokenCreate.setUdid(Integer.toString(userAccount.getUdid()));
        tokenCreate.setSsoToken(Utils.createJWT(userAccount));
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setType("web");
        deviceInfo.setLang("ru");
        tokenCreate.setDeviceInfo(deviceInfo);
        channelsRequest.setData(tokenCreate);
        ChannelsResponse channelsResponse = channelsConnector.doRequest(channelsRequest, PropertiesUtil.getProperty("channels_api_token_url"));
        if("error".equals(channelsResponse.getResult())){
            String message = messageHandler.fillInMessageByUserData(PropertiesUtil.getProperty("channels_create_token_error"), userAccount);
            telegramUpdateHandler.flushUserState(userAccount.getId());
            throw new TelegramException(message, userAccount.getId());
        }
        return channelsResponse.getData().getToken();
    }
}
