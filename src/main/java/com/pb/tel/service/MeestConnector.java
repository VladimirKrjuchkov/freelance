package com.pb.tel.service;

import com.pb.tel.data.meest.MeestRequest;
import com.pb.tel.data.meest.MeestResponse;
import com.pb.tel.data.novaposhta.NovaPoshtaRequest;
import com.pb.tel.data.novaposhta.NovaPoshtaResponse;
import com.pb.uniwin.atm.host.DetailedAnswer;
import com.pb.uniwin.atm.host.RequestHTTPS;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 05.06.18.
 */

@Service("meestConnector")
public class MeestConnector {

    private final Logger log = Logger.getLogger(MeestConnector.class.getCanonicalName());

    @Autowired
    private Jaxb2Marshaller jaxbMarshaller;

    private HashMap<String, Object> properties = new HashMap<String, Object>();


    public MeestResponse sendRequest(MeestRequest request) throws Exception {
        RequestHTTPS requestHTTP = new RequestHTTPS(Integer.parseInt(PropertiesUtil.getProperty("connectTimeout")), Integer.parseInt(PropertiesUtil.getProperty("readTimeout")));
        String url = PropertiesUtil.getProperty("meest_api_url");
        log.log(Level.INFO, "URL OF REQUEST : " + url);
        Map<String, String> requestProperties = new HashMap<String, String>();
        requestProperties.put("Content-Type", MediaType.APPLICATION_XML_VALUE);
        requestProperties.put("Accept", MediaType.APPLICATION_XML_VALUE);
        requestHTTP.setRequestProperties(requestProperties);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        jaxbMarshaller.marshal(request, new StreamResult(baos));
        String resp = requestHTTP.performQuery(baos.toString(), url, "tracking by Meest express");
        ByteArrayInputStream bais = new ByteArrayInputStream(resp.getBytes());
        return (MeestResponse)jaxbMarshaller.unmarshal(new StreamSource(bais));
    }
}
