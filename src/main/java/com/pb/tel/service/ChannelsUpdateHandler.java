package com.pb.tel.service;

import com.pb.tel.data.Request;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.channels.*;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import com.pb.util.zvv.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
        Random generator = new Random();
        ChannelsRequest channelsRequest = new ChannelsRequest();
        channelsRequest.setReqId(userAccount.getReqId());
        Data data = new Data();
        data.setCompanyId(PropertiesUtil.getProperty("channels_company_id"));
        data.setChannelId(userAccount.getChannelId());
        if(userAccount.getFile() != null){
            channelsRequest.setAction("msgFile");
            List<File> files = new ArrayList<File>();
            File file = new File();
            file.setUrl(userAccount.getFile().getUrl());
            file.setType(userAccount.getFile().getType());
            file.setSize((userAccount.getFile().getSize()==null) ? 999 : userAccount.getFile().getSize());
            file.setName((userAccount.getFile().getName()==null) ? ("unknown_file_" + generator.nextInt(10)) : userAccount.getFile().getName());
            file.setHeight(userAccount.getFile().getHeight());
            Meta meta = new Meta();
            meta.setWidth(userAccount.getFile().getWidth());
            meta.setHeight(userAccount.getFile().getHeight());
            Preview preview = new Preview();
            preview.setWidth(userAccount.getFile().getWidth());
            preview.setSize(userAccount.getFile().getSize());
            preview.setUrl(userAccount.getFile().getUrl());
            preview.setHeight(userAccount.getFile().getHeight());
            meta.setPreview(preview);
            file.setMeta(meta);
            files.add(file);
            data.setFiles(files);
        }else{
            channelsRequest.setAction("msg");
            data.setText(userAccount.getUserText());
        }
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
