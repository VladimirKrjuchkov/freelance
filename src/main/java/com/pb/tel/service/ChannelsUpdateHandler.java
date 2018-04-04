package com.pb.tel.service;

import com.pb.tel.data.Request;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.channels.ChannelsRequest;
import com.pb.tel.data.channels.Data;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.zvv.PropertiesUtil;
import com.pb.util.zvv.storage.Storage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * Created by vladimir on 29.03.18.
 */

@Service("channelsUpdateHandler")
public class ChannelsUpdateHandler extends AbstractUpdateHandler {

    private final Logger log = Logger.getLogger(ChannelsUpdateHandler.class.getCanonicalName());

    @Override
    public Request deligateMessage(UserAccount userAccount){
        ChannelsRequest channelsRequest = new ChannelsRequest();
        channelsRequest.setAction("msg");
        channelsRequest.setReqId(Integer.toString(userAccount.getReqId()));
        Data data = new Data();
        data.setCompanyId(PropertiesUtil.getProperty("channels_company_id"));
        data.setChannelId(userAccount.getChannelId());
        data.setText(userAccount.getUserText());
        channelsRequest.setData(data);
        return channelsRequest;
    }

}
