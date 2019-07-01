package com.pb.tel.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * Created by vladimir on 25.06.19.
 */

public class Utils {
    @Autowired
    private Environment environment;

    public static Environment property;

    @PostConstruct
    private void init(){
        property = environment;
    }

}
