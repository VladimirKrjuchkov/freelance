package com.pb.tel.service;

import com.pb.tel.data.UserAccount;
import com.pb.tel.data.channels.Operator;
import com.pb.tel.data.meest.Items;
import com.pb.tel.data.meest.MeestRequest;
import com.pb.tel.data.meest.MeestResponse;
import com.pb.tel.service.exception.LogicException;
import com.pb.util.zvv.PropertiesUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 05.06.18.
 */

@Service("meestAPIHandler")
public class MeestAPIHandler implements Tracker{

    @Autowired
    private MeestConnector meestConnector;

    private final Logger log = Logger.getLogger(MeestAPIHandler.class.getCanonicalName());


    public String getTrackingByTTN(UserAccount userAccount) throws Exception{
        MeestRequest meestRequest = new MeestRequest();
        meestRequest.setLogin(PropertiesUtil.getProperty("meest_login"));
        meestRequest.setFunction(PropertiesUtil.getProperty("meest_function"));
        meestRequest.setWhere("\"" + userAccount.getUserText() + "\"");
        meestRequest.setSign(DigestUtils.md5Hex(PropertiesUtil.getProperty("meest_login")+PropertiesUtil.getProperty("meest_password")+PropertiesUtil.getProperty("meest_function")+"\"" + userAccount.getUserText() + "\""));
        MeestResponse meestResponse = meestConnector.sendRequest(meestRequest);
        String message = null;
        if("000".equals(meestResponse.getErrors().getCode()) && meestResponse.getResult_table().getItems() != null) {
            List<Items> items = meestResponse.getResult_table().getItems();
            Collections.sort(items, new ItemsComporator());
            Items item = items.get(0);
            message = ("\n" + "Компанiя: Meest Express" +
                    "\n" + "Дата: " + Utils.dateFormat1.format(item.getDateTimeAction()) +
                    "\n" + "Час: " + Utils.dateFormat2.format(item.getDateTimeAction()) +
                    "\n" + "Країна: " + item.getCountryByLocale(userAccount.getLocale()) +
                    "\n" + "Місто: " + item.getCityByLocale(userAccount.getLocale()) +
                    "\n" + "Кількість місць: " + item.getDetailPlacesAction() +
                    "\n" + "Детальне повідомлення: " + item.getActionByLocale(userAccount.getLocale()));
        }else{
            message = MessageHandler.getMessage(userAccount.getLocale(), "not_found");
        }
        return message;
    }

    class  ItemsComporator implements Comparator<Items> {
        public int compare(Items a, Items b){
            Date dateA = a.getDateTimeAction();
            Date dateB = b.getDateTimeAction();
            return dateB.compareTo(dateA);
        }
    }
}
