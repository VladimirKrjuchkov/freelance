package com.pb.tel.controller;

import com.pb.tel.data.AdminAccount;
import com.pb.tel.data.RegisterRequest;
import com.pb.tel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


@Controller
public class RestController {

    @Autowired
    private Environment environment;

    private final Logger log = Logger.getLogger(RestController.class.getCanonicalName());


    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
    @ResponseBody
    public void adminRegister(@RequestBody RegisterRequest registerRequest, HttpServletResponse response){// <<<--- вместо всей этой дряни надо прикрутить basic- авторизацию!!!
        log.info("start registration, login: " + registerRequest.getLogin());
        AdminAccount adminAccount = new AdminAccount(UUID.randomUUID().toString());
        Utils.setCookie(response, "sessionId", adminAccount.getSessionId(), "/", environment.getProperty("main.domain"), true, 60*10000);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public void exceptionHandler(Exception e){
        log.log(Level.SEVERE, "ManagingRequestController :: exceptionHandler", e);
    }

}
