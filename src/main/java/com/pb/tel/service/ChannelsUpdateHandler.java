package com.pb.tel.service;

import com.pb.tel.data.Request;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.channels.ChannelsRequest;
import com.pb.tel.data.channels.ChannelsResponse;
import com.pb.tel.data.channels.Data;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import com.pb.util.zvv.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * Created by vladimir on 29.03.18.
 */

@Service("channelsUpdateHandler")
public class ChannelsUpdateHandler extends AbstractUpdateHandler {

    private final Logger log = Logger.getLogger(ChannelsUpdateHandler.class.getCanonicalName());


    @Autowired
    ChannelsConnector channelsConnector;

    @Override
    public Request deligateMessage(UserAccount userAccount){
        ChannelsRequest channelsRequest = new ChannelsRequest();
        channelsRequest.setAction("msg");
        channelsRequest.setReqId(userAccount.getReqId());
        Data data = new Data();
        data.setCompanyId(PropertiesUtil.getProperty("channels_company_id"));
        data.setChannelId(userAccount.getChannelId());
        data.setText(userAccount.getUserText());
        channelsRequest.setData(data);
        return channelsRequest;
    }

    public Integer checkDialogStatus(UserAccount userAccount) throws Exception {
        Integer status = 0;
        ChannelsRequest channelsRequest = new ChannelsRequest();
        channelsRequest.setAction("channelInfo");
        channelsRequest.setReqId(userAccount.getReqId());
        Data data = new Data();
        data.setCompanyId(PropertiesUtil.getProperty("channels_company_id"));
        data.setChannelId(userAccount.getChannelId());
        channelsRequest.setData(data);
        ChannelsResponse channelsResponse = channelsConnector.doRequest(channelsRequest, PropertiesUtil.getProperty("channels_api_request_url")+userAccount.getToken());
        if("error".equals(channelsResponse.getResult())){
            throw new UnresponsibleException("CHAN01", PropertiesUtil.getProperty("CHAN01"));

        }else{
            if(channelsResponse.getData().getUsers().size()>1){
                status = 1;
            }
        }

        return status;
    }

    @Override
    public Request leaveDialog(UserAccount userAccount) throws UnresponsibleException {
        return null;
    }

}
