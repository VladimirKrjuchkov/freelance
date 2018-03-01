package com.pb.tel.controller;

import com.pb.tel.data.Message;
import com.pb.tel.service.TelegramConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Level;
import java.util.logging.Logger;


@Controller
public class ManagingRequestController {

    @Autowired
    private TelegramConnector telegramConnector;

    private final Logger log = Logger.getLogger(ManagingRequestController.class.getCanonicalName());

    @RequestMapping(value = "/start", method=RequestMethod.GET)
    @ResponseBody
    public void start() throws Exception {
        telegramConnector.setWebHook();
    }

    @RequestMapping(value = "/webhook/{telegram_bot_token}")
    @ResponseBody
    public void webhook(@PathVariable(value="telegram_bot_token") String telegramBotToken) {

    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Message exceptionHandler(Exception e){
        log.log(Level.SEVERE, "ManagingRequestController :: exceptionHandler", e);
        Message message = new Message("error", e.getMessage());
        return message;
    }

}
