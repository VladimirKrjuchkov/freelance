package com.pb.tel.controller;

import com.pb.tel.data.Mes;
import com.pb.tel.data.telegram.TelegramRequest;
import com.pb.tel.data.telegram.TelegramResponse;
import com.pb.tel.data.telegram.Update;
import com.pb.tel.service.TelegramConnector;
import com.pb.tel.service.TelegramUpdateHandler;
import com.pb.tel.service.exception.TelegramException;
import com.pb.tel.service.exception.UnresponsibleException;
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
    public void update(@RequestBody Update update) throws Exception{
        TelegramResponse response = telegramConnector.sendRequest(telegramUpdateHandler.getTelegramRequest(update));
        telegramUpdateHandler.analyseResponse(response, update);
    }

    @ExceptionHandler(UnresponsibleException.class)
    @ResponseBody
    public void unresponsibleException(UnresponsibleException e){
        telegramUpdateHandler.flushUserState(e.getId());
        log.log(Level.SEVERE, e.getDescription(), e);
    }

    @ExceptionHandler(TelegramException.class)
    @ResponseBody
    public void telegramExceptionHandler(TelegramException e) throws Exception {
        TelegramRequest message = new TelegramRequest(e.getUserId(), e.getDescription());
        telegramConnector.sendRequest(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public void exceptionHandler(Exception e){
        log.log(Level.SEVERE, "ManagingRequestController :: exceptionHandler", e);
    }

}
