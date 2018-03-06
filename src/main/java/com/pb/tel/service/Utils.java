package com.pb.tel.service;

import java.util.Date;

/**
 * Created by vladimir on 06.03.18.
 */
public class Utils {

    public static final String encode = "UTF-8";

    public static int getSecondsToDate(Date date){
        int ttlValue = (int)(date.getTime() - System.currentTimeMillis())/1000;
        if(ttlValue<0)
            ttlValue = 0;
        return ttlValue;
    }

    public static Date getDateAfterSeconds(int afterSeconds){
        return new Date(System.currentTimeMillis() + afterSeconds*1000);
    }

}
