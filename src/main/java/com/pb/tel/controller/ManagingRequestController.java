package com.pb.tel.controller;

import com.pb.tel.data.Mes;
import com.pb.tel.data.telegram.Message;
import com.pb.tel.service.TelegramConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;


@Controller
public class ManagingRequestController {

    @Autowired
    private TelegramConnector telegramConnector;

    private final Logger log = Logger.getLogger(ManagingRequestController.class.getCanonicalName());

    @RequestMapping(value = "/setWebhook", method=RequestMethod.GET)
    @ResponseBody
    public void start() throws Exception {
        telegramConnector.setWebHook();
    }

    @RequestMapping(value = "/webhook")
    @ResponseBody
    public void webhook(@RequestBody Message request) {

    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Mes exceptionHandler(Exception e){
        log.log(Level.SEVERE, "ManagingRequestController :: exceptionHandler", e);
        Mes message = new Mes("error", e.getMessage());
        return message;
    }

}
