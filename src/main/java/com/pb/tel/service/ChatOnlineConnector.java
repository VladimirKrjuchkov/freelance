package com.pb.tel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pb.tel.data.Request;
import com.pb.tel.data.channels.ChannelsResponse;
import com.pb.tel.data.chatonline.ChatResponse;
import com.pb.tel.data.telegram.TelegramResponse;
import com.pb.uniwin.atm.host.DetailedAnswer;
import com.pb.uniwin.atm.host.RequestHTTPS;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by vladimir on 18.04.18.
 */

@Service("chatOnlineConnector")
public class ChatOnlineConnector {

    private final Logger log = Logger.getLogger(ChatOnlineConnector.class.getCanonicalName());

    private static ObjectMapper jacksonObjectMapper = new ObjectMapper();

    public ChatResponse sendRequest(Request request) throws Exception {
        RequestHTTPS requestHTTP = new RequestHTTPS(Integer.parseInt(PropertiesUtil.getProperty("connectTimeout")), Integer.parseInt(PropertiesUtil.getProperty("readTimeout")));
        String url = PropertiesUtil.getProperty("chat_online_url");
        Map<String, String> requestProperties = new HashMap<String, String>();
        requestProperties.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        requestProperties.put("Accept", MediaType.APPLICATION_JSON_VALUE);
        requestHTTP.setRequestProperties(requestProperties);
        DetailedAnswer answer = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(request), url);
        return jacksonObjectMapper.readValue(answer.getBody().getBytes(), ChatResponse.class);
    }
}
