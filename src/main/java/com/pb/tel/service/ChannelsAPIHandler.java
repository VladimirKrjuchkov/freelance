package com.pb.tel.service;

import com.pb.tel.data.UserAccount;
import com.pb.tel.data.channels.*;
import com.pb.tel.service.exception.TelegramException;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public void callOper(UserAccount userAccount) throws Exception {
        userAccount.setChannelId(createChannel(userAccount));
        userAccount.setOperName(addOperToChannel(userAccount));
    }

    private String addOperToChannel(UserAccount userAccount) throws Exception {
        ChannelsRequest channelsRequest = new ChannelsRequest();
        channelsRequest.setAction("channelInvite");
        channelsRequest.setReqId(Integer.toString(userAccount.getReqId()));
        ChannelCreate channelCreate = new ChannelCreate();
        channelCreate.setCompanyId(PropertiesUtil.getProperty("channels_company_id"));
        channelCreate.setChannelId(userAccount.getChannelId());
        List<String> invites = new ArrayList<String>();
        invites.add(userAccount.getOperId());
        channelCreate.setInvites(invites);
        channelsRequest.setData(channelCreate);
        ChannelsResponse channelsResponse = channelsConnector.doRequest(channelsRequest, PropertiesUtil.getProperty("channels_api_request_url")+userAccount.getToken());
        if("error".equals(channelsResponse.getResult()) || channelsResponse.getData().getUsers().size() <=0){
            String message = messageHandler.fillInMessageByUserData(PropertiesUtil.getProperty("channels_create_token_error"), userAccount);
            telegramUpdateHandler.flushUserState(userAccount.getId());
            throw new TelegramException(message, userAccount.getId());
        }
        return channelsResponse.getData().getUsers().get(0).getName();
    }

    private String createChannel(UserAccount userAccount) throws Exception {
        userAccount.setOperId(getFreeOper(userAccount));
        userAccount.setToken(createToken(userAccount));
        ChannelsRequest channelsRequest = new ChannelsRequest();
        channelsRequest.setAction("channelCreate");
        channelsRequest.setReqId(Integer.toString(userAccount.getReqId()));
        ChannelCreate channelCreate = new ChannelCreate();
        channelCreate.setType("help");
        channelCreate.setCompanyId(PropertiesUtil.getProperty("channels_company_id"));
        channelsRequest.setData(channelCreate);
        ChannelsResponse channelsResponse = channelsConnector.doRequest(channelsRequest, PropertiesUtil.getProperty("channels_api_request_url")+userAccount.getToken());
        if("error".equals(channelsResponse.getResult())){
            String message = messageHandler.fillInMessageByUserData(PropertiesUtil.getProperty("channels_create_token_error"), userAccount);
            telegramUpdateHandler.flushUserState(userAccount.getId());
            throw new TelegramException(message, userAccount.getId());
        }
        return channelsResponse.getData().getChannelId();
    }

    private String getFreeOper(UserAccount userAccount) throws Exception {
        ChannelsRequest channelsRequest = new ChannelsRequest();
        channelsRequest.setToken(PropertiesUtil.getProperty("bot_static_token"));
        ChannelCreate channelCreate = new ChannelCreate();
        channelCreate.setCompanyId(PropertiesUtil.getProperty("channels_company_id"));
        channelsRequest.setData(channelCreate);
        channelsRequest.setAction("botOperatorsGet");
        ChannelsResponse channelsResponse = channelsConnector.doRequest(channelsRequest, PropertiesUtil.getProperty("bots_api_request_url"));
        if("error".equals(channelsResponse.getResult())){
            String message = messageHandler.fillInMessageByUserData(PropertiesUtil.getProperty("channels_create_token_error"), userAccount);
            telegramUpdateHandler.flushUserState(userAccount.getId());
            throw new TelegramException(message, userAccount.getId());
        }else {
            return analyseAndGetOperId(channelsResponse.getData().getOperators(), userAccount);
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

    private String analyseAndGetOperId(List<Operator> opers, UserAccount userAccount) throws TelegramException {
        Comparator<Operator> ocomp = new OpersComporator();
        TreeSet<Operator> freeOpers = new TreeSet(ocomp);
        for(Operator oper: opers){
            if("online".equals(oper.getStatus())){
                freeOpers.add(oper);
            }
        }
        if(freeOpers.size() <= 0){
            String message = messageHandler.fillInMessageByUserData(PropertiesUtil.getProperty("channels_call_oper_error"), userAccount);
            telegramUpdateHandler.flushUserState(userAccount.getId());
            throw new TelegramException(message, userAccount.getId());
        }

        return freeOpers.first().getId();
    }

    class OpersComporator implements Comparator<Operator> {

        public int compare(Operator a, Operator b){

            if(a.getChatsCount()> b.getChatsCount())
                return 1;
            else if(a.getChatsCount()< b.getChatsCount())
                return -1;
            else
                return 0;
        }
    }
}
