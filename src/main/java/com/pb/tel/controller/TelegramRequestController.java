package com.pb.tel.controller;

import com.pb.tel.data.Mes;
import com.pb.tel.data.telegram.TelegramRequest;
import com.pb.tel.data.telegram.TelegramResponse;
import com.pb.tel.data.telegram.Update;
import com.pb.tel.service.TelegramConnector;
import com.pb.tel.service.TelegramUpdateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 06.03.18.
 */

@Controller
public class TelegramRequestController {

    private final Logger log = Logger.getLogger(TelegramRequestController.class.getCanonicalName());

    @Autowired
    private TelegramUpdateHandler telegramUpdateHandler;

    @Autowired
    private TelegramConnector telegramConnector;

    @RequestMapping(value = "/update")
    @ResponseBody
    public void update(@RequestBody Update update) throws Exception {
        String message = telegramUpdateHandler.getResponseMessage(update);
        TelegramRequest telegramRequest = telegramUpdateHandler.getTelegramRequest(message, update.getMessage().getFrom().getId());
        TelegramResponse response = telegramConnector.sendRequest(telegramRequest);
        telegramUpdateHandler.analyseResponse(response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Mes exceptionHandler(Exception e){
        log.log(Level.SEVERE, "ManagingRequestController :: exceptionHandler", e);
        Mes message = new Mes(Mes.MesState.err, e.getMessage());
        return message;
    }

}
