package com.pb.tel.dao;

import com.pb.tel.data.analytics.Event;
import com.pb.tel.data.enums.Locale;
import com.pb.tel.data.privatmarket.Customer;
import com.pb.tel.service.MessageHandler;
import com.pb.tel.service.Utils;
import com.pb.tel.service.exception.TelegramException;
import com.pb.tel.service.exception.UnresponsibleException;
import org.apache.http.client.utils.DateUtils;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 11.06.18.
 */

@Repository("eventDaoImpl")
@Transactional
public class EventDaoImpl implements EventDao{

    private static final Logger log = Logger.getLogger(EventDaoImpl.class.getCanonicalName());

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true, propagation= Propagation.SUPPORTS)
    public List<Event> getByDay(){
        long start = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        TypedQuery<Event> query = em.createNamedQuery("Event.findByDay", Event.class);
        Date dateTo = cal.getTime();
        log.log(Level.INFO, "dateTo = " + dateTo);
        query.setParameter("dateTo", dateTo);
        cal.add(Calendar.DATE, -1);
        Date dateFrom = cal.getTime();
        query.setParameter("dateFrom", dateFrom);
        log.log(Level.INFO, "dateFrom = " + dateFrom);
        List<Event> result = query.getResultList();
        log.log(Level.INFO, "Event.findByDay at "+(System.currentTimeMillis() - start)+"ms");
        return result;
    }

    @Transactional(rollbackFor=Exception.class)
    public Event addEvent(Event event) throws Exception{
        log.log(Level.INFO, "EVENT TO ADD : " + event);
        long start = System.currentTimeMillis();
        em.persist(event);
        log.log(Level.INFO, "addEvent at " + (System.currentTimeMillis() - start) + "ms");
        return event;
    }
}
