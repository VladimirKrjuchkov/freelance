package com.pb.tel.adapter;

import com.pb.tel.service.MeestAPIHandler;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 07.06.18.
 */
public class DateAdapter extends XmlAdapter<String, Date> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final Logger log = Logger.getLogger(DateAdapter.class.getCanonicalName());


    @Override
    public String marshal(Date v) throws Exception {
        if(v == null)
            return null;
        return dateFormat.format(v);
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        if(v == null || v.equals(""))
            return null;
        return dateFormat.parse(v);
    }

}

