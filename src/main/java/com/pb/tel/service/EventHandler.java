package com.pb.tel.service;


import com.pb.tel.dao.CustomerDao;
import com.pb.tel.dao.EventDao;
import com.pb.tel.dao.EventDaoImpl;
import com.pb.tel.data.UserAccount;
import com.pb.tel.data.analytics.Event;
import com.pb.tel.data.enums.Action;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 11.06.18.
 */

@Service("eventHandler")
public class EventHandler {

    private static final Logger log = Logger.getLogger(EventHandler.class.getCanonicalName());

    @Resource(name="")
    private EventDao eventDaoIml;

    public void addEvent(UserAccount userAccount, Action action){
        Runnable r = () -> {
            try{
                    log.info("\n========================== ADD EVENT, ACTION: " + action.getCode() +" ==========================" + com.pb.util.zvv.logging.MessageHandler.startMarker);
                    eventDaoIml.addEvent(getEventFromUserAccount(userAccount, action));

                }catch (Exception e){
                    log.log(Level.SEVERE, "Error while add new event ", e);

                }finally {
                    log.info(com.pb.util.zvv.logging.MessageHandler.finishMarker);
                }
        };
        new Thread(r).start();
    }

    private Event getEventFromUserAccount(UserAccount userAccount, Action action){
        Event event = new Event();
        event.setEventId(UUID.randomUUID().toString());
        event.setAction(action.getCode());
        event.setChannel(userAccount.getMessenger());
        event.setExtId(userAccount.getId());
        event.setDate(new Date());
        if(Action.trackError == action) {
            event.setDescription(userAccount.getUserText());
        }
        return event;
    }
}
