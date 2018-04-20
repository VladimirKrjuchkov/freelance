package com.pb.tel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pb.tel.data.Mes;
import com.pb.tel.data.Request;
import com.pb.tel.data.Response;
import com.pb.tel.data.facebook.FaceBookResponse;
import com.pb.tel.data.facebook.FaceBookUser;
import com.pb.uniwin.atm.host.DetailedAnswer;
import com.pb.uniwin.atm.host.RequestHTTPS;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by vladimir on 11.04.18.
 */

@Service("faceBookConnector")
public class FaceBookConnector implements Connector{

    private final Logger log = Logger.getLogger(FaceBookConnector.class.getCanonicalName());

    private static ObjectMapper jacksonObjectMapper = new ObjectMapper();

    @Override
    public Mes webHook(String oper) throws Exception {
        return null;
    }

    @Override
    public Response doWebHookRequest(String webHookUrl) throws Exception {
        return null;
    }

    public Response sendRequest(Request request) throws Exception {
        RequestHTTPS requestHTTP = new RequestHTTPS(Integer.parseInt(PropertiesUtil.getProperty("connectTimeout")), Integer.parseInt(PropertiesUtil.getProperty("readTimeout")));
        String url = PropertiesUtil.getProperty("facebook_api_url") + PropertiesUtil.getProperty("facebook_update_token");
        Map<String, String> requestProperties = new HashMap<String, String>();
        requestProperties.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        requestProperties.put("Accept", MediaType.APPLICATION_JSON_VALUE);
        requestHTTP.setRequestProperties(requestProperties);
        requestHTTP.setRequestMethod("POST");
        DetailedAnswer answer = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(request), url);
        return jacksonObjectMapper.readValue(answer.getBody().getBytes(), FaceBookResponse.class);
    }

    public FaceBookUser getUserProfileData(String id) throws Exception {
        RequestHTTPS requestHTTP = new RequestHTTPS(Integer.parseInt(PropertiesUtil.getProperty("connectTimeout")), Integer.parseInt(PropertiesUtil.getProperty("readTimeout")));
        String url = PropertiesUtil.getProperty("face_book_user_info_url") + id + "?fields=first_name,last_name,profile_pic&access_token=" + PropertiesUtil.getProperty("facebook_update_token");
        requestHTTP.setRequestMethod("GET");
        DetailedAnswer answer = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(null), url);
        return jacksonObjectMapper.readValue(answer.getBody().getBytes(), FaceBookUser.class);
    }

}
