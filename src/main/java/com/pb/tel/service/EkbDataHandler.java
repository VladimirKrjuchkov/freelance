package com.pb.tel.service;

import com.pb.cis.ClientObject.ClientItems.ClientItem;
import com.pb.service.uniwin.ua.dao.EkbDao;
import com.pb.service.uniwin.ua.message.Card;
import com.pb.service.uniwin.ua.message.ServiceException;
import com.pb.service.uniwin.ua.message.Session;
import com.pb.service.uniwin.ua.util.ClientItemUW;
import com.pb.tel.data.UserAccount;
import com.pb.tel.service.exception.UnresponsibleException;
import com.pb.util.bpn.Sessions;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 04.04.18.
 */

@Service("ekbDataHandler")
public class EkbDataHandler {

    private final Logger log = Logger.getLogger(EkbDataHandler.class.getCanonicalName());

    @Autowired
    private EkbDao ekbDao;

    @Resource(name="prominSession")
    private Sessions prominSession;

    public Integer getEkbIdByPhone(String phone){
        Integer ekbId = null;
        com.pb.service.uniwin.ua.message.Customer customer = new com.pb.service.uniwin.ua.message.Customer();
        customer.setPhone("+"+phone);
//        customer.setPhone(phone);
        long start = System.currentTimeMillis();
        List<ClientItem> clientItems = null;
        try {
            clientItems = ekbDao.getClientItemsByCustomer(customer, new Session(prominSession.getSession(), null, null));
            if(clientItems == null || clientItems.size()<=0){
                throw new UnresponsibleException("EKB01", PropertiesUtil.getProperty("EKB01"));
            }else{
                ekbId = clientItems.get(0).getId();
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR WHILE GET CUSTOMER FROM EKB : ", e);
        }
        log.info("get data from EKB at "+(System.currentTimeMillis() - start)+"ms");
        return ekbId;
    }
}
