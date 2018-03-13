package com.pb.tel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pb.tel.data.novaposhta.NovaPoshtaRequest;
import com.pb.tel.data.novaposhta.NovaPoshtaResponse;
import com.pb.tel.data.telegram.TelegramResponse;
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
 * Created by vladimir on 12.03.18.
 */
@Service("novaPoshtaConnector")
public class NovaPoshtaConnector {

    private final Logger log = Logger.getLogger(NovaPoshtaConnector.class.getCanonicalName());

    private static ObjectMapper jacksonObjectMapper = new ObjectMapper();

    public NovaPoshtaResponse sendRequest(NovaPoshtaRequest request) throws Exception {
        RequestHTTPS requestHTTP = new RequestHTTPS(Integer.parseInt(PropertiesUtil.getProperty("connectTimeout")), Integer.parseInt(PropertiesUtil.getProperty("readTimeout")));
        String url = PropertiesUtil.getProperty("novaposhta_api_url");
        log.log(Level.INFO, "URL OF REQUEST : " + url);
        Map<String, String> requestProperties = new HashMap<String, String>();
        requestProperties.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        requestProperties.put("Accept", MediaType.APPLICATION_JSON_VALUE);
        requestHTTP.setRequestProperties(requestProperties);
        DetailedAnswer answer = requestHTTP.performQueryDetailedResponse(jacksonObjectMapper.writeValueAsString(request), url);
        return jacksonObjectMapper.readValue(answer.getBody().getBytes(), NovaPoshtaResponse.class);
    }
}
