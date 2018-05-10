package com.pb.tel.service;

import com.pb.tel.data.UserAccount;
import com.pb.tel.data.channels.*;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import com.pb.util.zvv.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource(name="channelIdByUserIdStore")
    private Storage<String, String> channelIdByUserIdStore;

    public void callOper(UserAccount userAccount) throws Exception {
        userAccount.setChannelId(createChannel(userAccount));
        userAccount.setOperName(addOperToChannel(userAccount));
        sendUserInfo(userAccount);
        channelIdByUserIdStore.putValue(userAccount.getChannelId(), userAccount.getId(), Utils.getDateAfterSeconds(3600));
    }

    private void sendUserInfo(UserAccount userAccount) throws Exception {
        ChannelsRequest channelsRequest = new ChannelsRequest();
        channelsRequest.setAction("msg");
        channelsRequest.setReqId(userAccount.getReqId());
        Data data = new Data();
        data.setCompanyId(PropertiesUtil.getProperty("channels_company_id"));
        data.setChannelId(userAccount.getChannelId());
        String info = "ФИО: " + userAccount.getLastName() + " " + userAccount.getFirstName() + "\n"
                    + "ЕКБ: " + userAccount.getIdEkb() + "\n"
                    + "Мессенджер: " + userAccount.getMessenger() + "\n"
                    + "Язык общения: " + userAccount.getLocale() + "\n"
                    + "********************" + "\n"
                    + "\n"
                    + "\n";
        data.setText(info);
        channelsRequest.setData(data);
        channelsConnector.doRequest(channelsRequest, PropertiesUtil.getProperty("channels_api_request_url") + userAccount.getToken());
    }

    private String addOperToChannel(UserAccount userAccount) throws Exception {
        ChannelsRequest channelsRequest = new ChannelsRequest();
        channelsRequest.setAction("channelInvite");
        channelsRequest.setReqId(userAccount.getReqId());
        Data channelCreate = new Data();
        channelCreate.setCompanyId(PropertiesUtil.getProperty("channels_company_id"));
        channelCreate.setChannelId(userAccount.getChannelId());
        List<String> invites = new ArrayList<String>();
        invites.add(userAccount.getOperId());
        channelCreate.setInvites(invites);
        channelsRequest.setData(channelCreate);
        ChannelsResponse channelsResponse = channelsConnector.doRequest(channelsRequest, PropertiesUtil.getProperty("channels_api_request_url")+userAccount.getToken());
        if("error".equals(channelsResponse.getResult()) || channelsResponse.getData().getUsers().size() <=0){
            String message = messageHandler.fillInMessageByUserData(MessageHandler.getMessage(userAccount.getLocale(), "channels_create_token_error"), userAccount);
//            telegramUpdateHandler.flushUserState(userAccount.getId());
            throw new LogicException("channels_create_token_error", message);
        }
        return channelsResponse.getData().getUsers().get(0).getName();
    }

    private String createChannel(UserAccount userAccount) throws Exception {
        userAccount.setOperId(getFreeOper(userAccount));
        userAccount.setToken(createToken(userAccount));
        ChannelsRequest channelsRequest = new ChannelsRequest();
        channelsRequest.setAction("channelCreate");
        channelsRequest.setReqId(userAccount.getReqId());
        Data channelCreate = new Data();
        channelCreate.setType("help");
        channelCreate.setCompanyId(PropertiesUtil.getProperty("channels_company_id"));
        channelsRequest.setData(channelCreate);
        ChannelsResponse channelsResponse = channelsConnector.doRequest(channelsRequest, PropertiesUtil.getProperty("channels_api_request_url")+userAccount.getToken());
        if("error".equals(channelsResponse.getResult())){
            String message = messageHandler.fillInMessageByUserData(MessageHandler.getMessage(userAccount.getLocale(), "channels_create_token_error"), userAccount);
            telegramUpdateHandler.flushUserState(userAccount.getId());
            throw new LogicException("channels_create_token_error", message);
        }
        return channelsResponse.getData().getChannelId();
    }

    private String getFreeOper(UserAccount userAccount) throws Exception {
        ChannelsRequest channelsRequest = new ChannelsRequest();
        channelsRequest.setToken(PropertiesUtil.getProperty("bot_static_token"));
        Data channelCreate = new Data();
        channelCreate.setCompanyId(PropertiesUtil.getProperty("channels_company_id"));
        channelsRequest.setData(channelCreate);
        channelsRequest.setAction("botOperatorsGet");
        ChannelsResponse channelsResponse = channelsConnector.doRequest(channelsRequest, PropertiesUtil.getProperty("bots_api_request_url"));
        if("error".equals(channelsResponse.getResult())){
            String message = messageHandler.fillInMessageByUserData(MessageHandler.getMessage(userAccount.getLocale(), "channels_create_token_error"), userAccount);
            telegramUpdateHandler.flushUserState(userAccount.getId());
            throw new LogicException("channels_create_token_error", message);
        }else {
            return analyseAndGetOperId(channelsResponse.getData().getOperators(), userAccount);
        }
    }

    private String createToken(UserAccount userAccount) throws Exception {
        ChannelsRequest channelsRequest = new ChannelsRequest();
        channelsRequest.setAction("tokenCreate");
        channelsRequest.setReqId(userAccount.getReqId());
        Data tokenCreate = new Data();
        tokenCreate.setUdid(userAccount.getUdid());
        tokenCreate.setSsoToken(Utils.createJWT(userAccount));
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setType("web");
        deviceInfo.setLang("ru");
        tokenCreate.setDeviceInfo(deviceInfo);
        channelsRequest.setData(tokenCreate);
        ChannelsResponse channelsResponse = channelsConnector.doRequest(channelsRequest, PropertiesUtil.getProperty("channels_api_token_url"));
        if("error".equals(channelsResponse.getResult())){
            String message = messageHandler.fillInMessageByUserData(MessageHandler.getMessage(userAccount.getLocale(), "channels_create_token_error"), userAccount);
            telegramUpdateHandler.flushUserState(userAccount.getId());
            throw new LogicException("channels_create_token_error", message);
        }
        return channelsResponse.getData().getToken();
    }

    private String analyseAndGetOperId(List<Operator> opers, UserAccount userAccount) throws LogicException, UnresponsibleException {
        Comparator<Operator> ocomp = new OpersComporator();
        TreeSet<Operator> freeOpers = new TreeSet(ocomp);
        for(Operator oper: opers){
            if("online".equals(oper.getStatus())){
                freeOpers.add(oper);
            }
        }
        if(freeOpers.size() <= 0){
            String message = messageHandler.fillInMessageByUserData(MessageHandler.getMessage(userAccount.getLocale(), "channels_call_oper_error"), userAccount);
            telegramUpdateHandler.flushUserState(userAccount.getId());
            throw new LogicException("channels_call_oper_error", message);
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
