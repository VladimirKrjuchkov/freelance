package com.pb.tel.utils.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by vladimir on 06.08.19.
 */
public class CustomJsonObjectMapper extends ObjectMapper {
    public CustomJsonObjectMapper() {
        this.findAndRegisterModules();
        this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}

