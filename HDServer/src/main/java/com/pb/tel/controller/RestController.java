package com.pb.tel.controller;

import com.pb.tel.data.AdminAccount;
import com.pb.tel.data.RegisterRequest;
import com.pb.tel.storage.Storage;
import com.pb.tel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


@Controller
public class RestController {

    @Autowired
    private Environment environment;

    @Resource(name="adminStorage")
    private Storage adminStorage;

    @Resource(name="freeOpers")
    private List<String> freeOpers;

    private final Logger log = Logger.getLogger(RestController.class.getCanonicalName());


    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
    @ResponseBody
    public void adminRegister(@RequestBody RegisterRequest request, HttpServletResponse response){// <<<--- вместо всей этой дряни надо прикрутить basic- авторизацию!!!
        log.info("start registration, login: " + request.getLogin());
        AdminAccount adminAccount = new AdminAccount(UUID.randomUUID().toString());
        adminAccount.setLogin(request.getLogin());
        adminStorage.putValue(adminAccount.getSessionId(), adminAccount, Utils.getDateAfterSeconds(Integer.valueOf(environment.getProperty("session.expiry"))));
        freeOpers.add(adminAccount.getSessionId());
        Utils.setCookie(response, "sessionId", adminAccount.getSessionId(), null, environment.getProperty("main.domain"), true, Integer.valueOf(environment.getProperty("session.expiry")));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public void exceptionHandler(Exception e){
        log.log(Level.SEVERE, "ManagingRequestController :: exceptionHandler", e);
    }

}
