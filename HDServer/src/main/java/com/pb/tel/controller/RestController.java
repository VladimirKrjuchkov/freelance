package com.pb.tel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Level;
import java.util.logging.Logger;


@Controller
public class RestController {

    private final Logger log = Logger.getLogger(RestController.class.getCanonicalName());


    @RequestMapping(value = "/simple")
    @ResponseBody
    public void simple(){}

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public void exceptionHandler(Exception e){
        log.log(Level.SEVERE, "ManagingRequestController :: exceptionHandler", e);
    }

}
