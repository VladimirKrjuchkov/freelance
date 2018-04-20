package com.pb.tel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pb.tel.data.Mes;
import com.pb.tel.data.Request;
import com.pb.tel.data.Response;
import com.pb.tel.data.channels.ChannelsRequest;
import com.pb.tel.data.channels.ChannelsResponse;
import com.pb.tel.data.novaposhta.NovaPoshtaResponse;
import com.pb.uniwin.atm.host.DetailedAnswer;
import com.pb.uniwin.atm.host.RequestHTTPS;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 16.03.18.
 */

@Service("channelsConnector")
public class ChannelsConnector implements Connector{

    private final Logger log = Logger.getLogger(ChannelsConnector.class.getCanonicalName());

    private static ObjectMapper jacksonObjectMapper = new ObjectMapper();

    public ChannelsResponse doRequest(Request channelsRequest, String url) throws Exception {
        RequestHTTPS requestHTTP = new RequestHTTPS(Integer.parseInt(PropertiesUtil.getProperty("connectTimeout")), Integer.parseInt(PropertiesUtil.getProperty("readTimeout")));
        Map<String, String> requestProperties = new HashMap<String, String>();
        requestProperties.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        requestProperties.put("Accept", MediaType.APPLICATION_JSON_VALUE);
        requestHTTP.setRequestProperties(requestProperties);
        DetailedAnswer answer = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(channelsRequest), url);
        return jacksonObjectMapper.readValue(answer.getBody().getBytes(), ChannelsResponse.class);
    }

    @Override
    public Mes webHook(String oper) throws Exception {
        return null;
    }

    @Override
    public Response doWebHookRequest(String webHookUrl) throws Exception {
        return null;
    }

    @Override
    public Response sendRequest(Request request) throws Exception {
        return null;
    }
}
