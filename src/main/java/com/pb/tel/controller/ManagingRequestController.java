package com.pb.tel.controller;

import com.pb.tel.data.Mes;
import com.pb.tel.data.telegram.Message;
import com.pb.tel.data.telegram.Update;
import com.pb.tel.service.TelegramConnector;
import com.pb.tel.service.TelegramUpdateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;


@Controller
public class ManagingRequestController {

    private final Logger log = Logger.getLogger(ManagingRequestController.class.getCanonicalName());

    @Autowired
    private TelegramConnector telegramConnector;

    @RequestMapping(value = "/webhook/{oper}", method=RequestMethod.GET)
    @ResponseBody
    public Mes setWebhook(@PathVariable(value="oper") String oper) throws Exception {
        return telegramConnector.webHook(oper);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Mes exceptionHandler(Exception e){
        log.log(Level.SEVERE, "ManagingRequestController :: exceptionHandler", e);
        Mes message = new Mes(Mes.MesState.err, e.getMessage());
        return message;
    }

}
