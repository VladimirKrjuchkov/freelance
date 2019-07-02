package com.pb.tel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;


@Controller
public class RestController {

    private final Logger log = Logger.getLogger(RestController.class.getCanonicalName());


    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
    @ResponseBody
    public void adminRegister(@RequestBody  RegisterRequest registerRequest){
        log.info("*** *** *** INSIDE SIMPLE *** *** ***");
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public void exceptionHandler(Exception e){
        log.log(Level.SEVERE, "ManagingRequestController :: exceptionHandler", e);
    }

}
