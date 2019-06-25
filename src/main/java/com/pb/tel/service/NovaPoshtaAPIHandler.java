//package com.pb.tel.service;
//
//import com.pb.tel.data.UserAccount;
//import com.pb.tel.data.enums.Action;
//import com.pb.tel.data.novaposhta.Document;
//import com.pb.tel.data.novaposhta.MethodPropertie;
//import com.pb.tel.data.novaposhta.NovaPoshtaRequest;
//import com.pb.tel.data.novaposhta.NovaPoshtaResponse;
//import com.pb.tel.service.exception.LogicException;
//import com.pb.util.zvv.PropertiesUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Logger;
//
///**
// * Created by vladimir on 12.03.18.
// */
//
//@Service("novaPoshtaAPIHandler")
//public class NovaPoshtaAPIHandler implements Tracker{
//
//    private final Logger log = Logger.getLogger(NovaPoshtaAPIHandler.class.getCanonicalName());
//
//    @Autowired
//    private NovaPoshtaConnector novaPoshtaConnector;
//
//    @Autowired
//    protected EventHandler eventHandler;
//
//    public String getTrackingByTTN(UserAccount userAccount) throws Exception {
//        NovaPoshtaRequest request = new NovaPoshtaRequest();
//        request.setModelName("TrackingDocument");
//        request.setCalledMethod("getStatusDocuments");
//        request.setLanguage(userAccount.getLocale().getCode());
//        MethodPropertie methodPropertie = new MethodPropertie();
//        Document document = new Document();
//        document.setDocumentNumber(makeCorrectTTN(userAccount.getUserText()));
//        List<Document> documents = new ArrayList<Document>();
//        documents.add(document);
//        methodPropertie.setDocuments(documents);
//        request.setMethodProperties(methodPropertie);
//        NovaPoshtaResponse response = novaPoshtaConnector.sendRequest(request);
//        String message = "(Нова пошта), ";
//        if(response.getSuccess() && userAccount.getUserText().equals(response.getData().get(0).getNumber())){
//            if("1".equals(response.getData().get(0).getStatusCode())) {
//                message += "Нова пошта очікує надходження від відправника";
//            }else if("2".equals(response.getData().get(0).getStatusCode())) {
//                message += "Видалено";
//            }else if("3".equals(response.getData().get(0).getStatusCode())){
//                message += "Номер не знайдено";
//                throw new LogicException("not_found", message);
//
//            }else if("4".equals(response.getData().get(0).getStatusCode())) {
//                message += "Відправлення у місті " + response.getData().get(0).getCitySender() + ". Орієнтовна дата доставки у місто " + response.getData().get(0).getCityRecipient() + " " + response.getData().get(0).getScheduledDeliveryDate();
//            }else if("41".equals(response.getData().get(0).getStatusCode())){
//                message += "Відправлення у місті " + response.getData().get(0).getCitySender() + ". Орієнтовна дата доставки " + response.getData().get(0).getScheduledDeliveryDate();
//            }else if("5".equals(response.getData().get(0).getStatusCode())){
//                message += "Відправлення прямує до міста " + response.getData().get(0).getCityRecipient() + ". Орієнтовна дата доставки " + response.getData().get(0).getScheduledDeliveryDate();
//            }else if("6".equals(response.getData().get(0).getStatusCode())){
//                message += "Відправлення у місті " + response.getData().get(0).getCityRecipient() + ", орієнтовна дата доставки до ВІДДІЛЕННЯ " + response.getData().get(0).getWarehouseRecipientNumber() + " " + response.getData().get(0).getScheduledDeliveryDate();
//            }else if("7".equals(response.getData().get(0).getStatusCode()) || "8".equals(response.getData().get(0).getStatusCode())){
//                message += "Відправлення прибуло у відділення, запрошуємо отримати";
//            }else if("9".equals(response.getData().get(0).getStatusCode())){
//                message += "Відправлення отримано";
//            }else if("10".equals(response.getData().get(0).getStatusCode())){
//                message += "Відправлення отримано " + response.getData().get(0).getRecipientDateTime() + ". Протягом доби ви одержите SMS-повідомлення про надходження грошового переказу та зможете отримати його в касі відділення «Нова пошта».";
//            }else if("11".equals(response.getData().get(0).getStatusCode())){
//                message += "Відправлення отримано " + response.getData().get(0).getRecipientDateTime() + ". Грошовий переказ видано одержувачу.";
//            }else if("14".equals(response.getData().get(0).getStatusCode())){
//                message += "Відправлення передано до огляду отримувачу";
//            }else if("101".equals(response.getData().get(0).getStatusCode())){
//                message += "На шляху до одержувача";
//            }else if("102".equals(response.getData().get(0).getStatusCode()) || "103".equals(response.getData().get(0).getStatusCode()) || "108".equals(response.getData().get(0).getStatusCode())){
//                message += "Відмова одержувача";
//            }else if("104".equals(response.getData().get(0).getStatusCode())){
//                message += "Змінено адресу";
//            }else if("105".equals(response.getData().get(0).getStatusCode())){
//                message += "Припинено зберігання";
//            }else if("106".equals(response.getData().get(0).getStatusCode())){
//                message += "Одержано і є ТТН грошовий переказ";
//            }else if("107".equals(response.getData().get(0).getStatusCode())){
//                message += "Нараховується плата за зберігання";
//            }
//            eventHandler.addEvent(userAccount, Action.trackSuccess);
//        }else{
//            if(PropertiesUtil.getProperty("bad_ttn").equals(response.getErrorCodes().get(0))){
//                throw new LogicException("bad_ttn_error", MessageHandler.getMessage(userAccount.getLocale(), "bad_ttn_error"));
//            }
//            throw new LogicException("tracking_error", MessageHandler.getMessage(userAccount.getLocale(), "tracking_error"));
//        }
//        return message;
//    }
//
//    private String makeCorrectTTN(String ttn){
//        return ttn.replaceAll("/n", "").trim();
//    }
//}
