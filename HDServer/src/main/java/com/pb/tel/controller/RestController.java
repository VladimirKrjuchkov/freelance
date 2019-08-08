package com.pb.tel.controller;

import com.pb.tel.data.Mes;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.service.handlers.AccountHandler;
import com.pb.tel.storage.Storage;
import com.pb.tel.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 31.07.19.
 */

@Controller
public class RestController {

    private static final Logger log = Logger.getLogger(RestController.class.getCanonicalName());

    @Autowired
    private Storage sessionStorage;

    @Autowired
    private AccountHandler accountHandler;

    @RequestMapping(value = "/sidCheck", method = RequestMethod.POST)
    @ResponseBody
    public Mes getSidCheck(HttpServletResponse response, @CookieValue(required=false) String storageKey)throws LogicException {
        log.info("Start getSidCheck storageKey = " + storageKey);
        if(storageKey != null){
            sessionStorage.removeValue(storageKey);
        }
        accountHandler.startEntrance(response);
        Mes result = new Mes();
        return result;
    }

    @ExceptionHandler(LogicException.class)
    @ResponseBody
    public Mes errorHanler(LogicException e){
        log.info("iN errorHanlr LogicException e = "+e);
        log.log(Level.WARNING, "iN errorHanler", e);
        SecurityContextHolder.clearContext();
        return Mes.createErrorMes(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Mes errorHanler(Exception e){
        log.info("iN errorHanlr Exception e = "+e);
        log.log(Level.SEVERE, "", e);
        SecurityContextHolder.clearContext();
        return Mes.createErrorMes("UNKN01", MessageUtil.getMessage("UNKN01"));
    }
}
