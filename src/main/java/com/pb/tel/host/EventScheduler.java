package com.pb.tel.host;

import com.pb.tel.dao.EventDao;
import com.pb.tel.data.analytics.Event;
import com.pb.tel.data.enums.Action;
import com.pb.tel.service.EventConnector;
import com.pb.tel.service.FaceBookConnector;
import com.pb.util.zvv.logging.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 11.06.18.
 */

@Service
public class EventScheduler {

    @Resource
    private EventDao eventDaoIml;

    @Resource
    private EventConnector eventConnector;

    private static final Logger log = Logger.getLogger(EventScheduler.class.getName());

    public EventScheduler(){
        log.log(Level.INFO, "init scheduler...");
    }

    @Scheduled(cron = "0 10 00 ? * *")
    public void sendEvent(){
        log.log(Level.INFO, "START EXECUTE EVENTS"+ MessageHandler.startMarker);
        try {
            List<Event> events = eventDaoIml.getByDay();
            log.log(Level.INFO, "event count : " + events.size());
            for(Event event : events) {
                if(Action.trackError != Action.getByCode(event.getAction())){
                    event.setDescription(null);
                }
                eventConnector.sendRequest(event);
            }
        } catch (Exception e) {
               log.log(Level.SEVERE, "error while send event", e);
        }finally {
            log.log(Level.INFO, MessageHandler.finishMarker);
        }
    }
}
