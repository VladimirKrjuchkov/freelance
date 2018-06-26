package com.pb.tel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pb.tel.data.Request;
import com.pb.tel.data.Response;
import com.pb.tel.data.analytics.Event;
import com.pb.tel.data.facebook.FaceBookResponse;
import com.pb.uniwin.atm.host.DetailedAnswer;
import com.pb.uniwin.atm.host.RequestHTTPS;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by vladimir on 23.06.18.
 */
@Service("eventConnector")
public class EventConnector {


    private final Logger log = Logger.getLogger(FaceBookConnector.class.getCanonicalName());

    private static ObjectMapper jacksonObjectMapper = new ObjectMapper();


    public Response sendRequest(Event request) throws Exception {
        RequestHTTPS requestHTTP = new RequestHTTPS(Integer.parseInt(PropertiesUtil.getProperty("connectTimeout")), Integer.parseInt(PropertiesUtil.getProperty("readTimeout")));
        String url = PropertiesUtil.getProperty("event_api_url");
        Map<String, String> requestProperties = new HashMap<String, String>();
        requestProperties.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        requestProperties.put("Accept", MediaType.APPLICATION_JSON_VALUE);
        requestHTTP.setRequestProperties(requestProperties);
        requestHTTP.setRequestMethod("POST");
        DetailedAnswer answer = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(request), url);
        return jacksonObjectMapper.readValue(answer.getBody().getBytes(), FaceBookResponse.class);
    }
}
