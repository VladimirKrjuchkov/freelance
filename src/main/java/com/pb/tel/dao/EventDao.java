package com.pb.tel.dao;

import com.pb.tel.data.analytics.Event;
import com.pb.tel.data.privatmarket.Customer;

import java.util.List;

/**
 * Created by vladimir on 11.06.18.
 */
public interface EventDao {

    public List<Event> getByDay();

    public Event addEvent(Event event) throws Exception;
}
