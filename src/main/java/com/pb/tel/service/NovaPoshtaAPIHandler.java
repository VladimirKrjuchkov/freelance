package com.pb.tel.service;

import com.pb.tel.data.novaposhta.Document;
import com.pb.tel.data.novaposhta.MethodPropertie;
import com.pb.tel.data.novaposhta.NovaPoshtaRequest;
import com.pb.tel.data.novaposhta.NovaPoshtaResponse;
import com.pb.tel.data.telegram.User;
import com.pb.tel.service.exception.TelegramException;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by vladimir on 12.03.18.
 */

@Service("novaPoshtaAPIHandler")
public class NovaPoshtaAPIHandler {

    private final Logger log = Logger.getLogger(NovaPoshtaAPIHandler.class.getCanonicalName());

    @Autowired
    private NovaPoshtaConnector novaPoshtaConnector;

    public String getTrackingByTTN(String ttn, User user) throws Exception {
        NovaPoshtaRequest request = new NovaPoshtaRequest();
        request.setModelName("TrackingDocument");
        request.setCalledMethod("getStatusDocuments");
        request.setLanguage("ua");
        MethodPropertie methodPropertie = new MethodPropertie();
        Document document = new Document();
        document.setDocumentNumber(ttn);
        List<Document> documents = new ArrayList<Document>();
        documents.add(document);
        methodPropertie.setDocuments(documents);
        request.setMethodProperties(methodPropertie);
        NovaPoshtaResponse response = novaPoshtaConnector.sendRequest(request);
        String message = null;
        if(response.getSuccess() && ttn.equals(response.getData().get(0).getNumber())){
            message = response.getData().get(0).getStatus();
        }else{
            if(PropertiesUtil.getProperty("bad_ttn").equals(response.getErrorCodes().get(0))){
                throw new TelegramException(PropertiesUtil.getProperty("bad_ttn"), user.getId());
            }
            throw new TelegramException("tracking_error", user.getId());
        }
        return message;
    }
}
