package com.pb.tel.controller;

import com.pb.tel.data.Mes;
import com.pb.tel.data.Operator;
import com.pb.tel.data.UserAccount;
import com.pb.tel.service.Reference;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.utils.MessageUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 31.07.19.
 */

@Controller
public class RestController {

    private static final Logger log = Logger.getLogger(RestController.class.getCanonicalName());

    @RequestMapping(value = "/checked/authCheck", method = RequestMethod.GET)
    @ResponseBody
    public Mes authCheck(@CookieValue(required=false) String sessionId) throws LogicException, IOException {
        log.info("Start authCheck, sessionId = " + sessionId);
        UserAccount userAccount = Reference.getAccountFromContext();
        log.info("userAccount: " + userAccount);
        if(userAccount != null){
            return new Mes(((Operator)(userAccount.getUser())).getLogin());
        }else{
            return Mes.createErrorMes();
        }
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
