package com.pb.tel.service;

import com.pb.tel.data.UserAccount;
import com.pb.tel.data.channels.ChannelsRequest;
import com.pb.tel.data.channels.Data;
import com.pb.tel.data.telegram.ReplyKeyboardHide;
import com.pb.tel.data.telegram.TelegramRequest;
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
public class ChannelsUpdateHandler {

    private final Logger log = Logger.getLogger(ChannelsUpdateHandler.class.getCanonicalName());

    @Resource(name="channelIdByUserIdStore")
    private Storage<String, Integer> channelIdByUserIdStore;

    @Resource(name="userAccountStore")
    private Storage<Integer, UserAccount> userAccountStore;

    public TelegramRequest deligateMessageToTelegram(ChannelsRequest channelsRequest) throws UnresponsibleException {
        Integer userId = channelIdByUserIdStore.getValue(((Data)channelsRequest.getData()).getChannelId());
        if(userId == null){
            throw  new UnresponsibleException("USER02", PropertiesUtil.getProperty("USER02"));
        }
        UserAccount userAccount = userAccountStore.getValue(userId);
        if(userAccount == null){
            throw  new UnresponsibleException("USER03", PropertiesUtil.getProperty("USER03"));
        }
        TelegramRequest message = new TelegramRequest(userAccount.getId(), ((Data) channelsRequest.getData()).getText());
        message.setReply_markup(new ReplyKeyboardHide());
        return message;
    }

}
