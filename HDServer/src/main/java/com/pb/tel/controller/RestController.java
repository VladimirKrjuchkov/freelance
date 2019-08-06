package com.pb.tel.controller;

import com.pb.tel.config.SecurityConfig;
import com.pb.tel.data.Mes;
import com.pb.tel.data.UserAccount;
import com.pb.tel.service.auth.ClientDetailsServiceInterface;
import com.pb.tel.service.exception.LogicException;
import com.pb.tel.storage.Storage;
import com.pb.tel.utils.MessageUtil;
import com.pb.tel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;
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
    private ClientDetailsServiceInterface clientDetailsService;


    @RequestMapping(value = "/sidCheck", method = RequestMethod.POST)
    @ResponseBody
    public Mes getSidCheck(HttpServletRequest request, HttpServletResponse response, @CookieValue(required=false) String exac)throws LogicException {
        log.info("Start getSidCheck ! exac: "+exac);
        UserAccount userAccount = new UserAccount();
        userAccount.setClientId(clientDetailsService.getAdminClientDetails().getClientId());
        String storageKey = UUID.randomUUID().toString();
        userAccount.setStorageKey(storageKey);
        userAccount.setMaxPossibleSessionExpire(new Date(System.currentTimeMillis() + (long)8.64e+7));
        sessionStorage.putValue(storageKey, userAccount, userAccount.getMaxPossibleSessionExpire());
//        if(exac!=null)
//            sessionStorage.removeValue(exac);
//        UserAccount userAccount = loginUrlAuthenticationEntryPoint.prepareEntrance(request, response, clientDetailsService.getAdminClientDetails(), false);
//        userAccount.setAutorizeUrl(MessageUtil.getProperty("entranceLink"));
//        sessionStorage.putValue(userAccount.getWorkStationUserId(), userAccount, userAccount.getMaxPossibleSessionExpire());
        Utils.setCookie(response, "storageKey", storageKey, null/*"/PplsService/"*/, null/*MessageUtil.getProperty("mainDomain")*/, false, userAccount.getMaxInSecondPossibleSessionExpire()+60);
        Mes result = new Mes();
        return result;
    }

    @RequestMapping(value = "/checked/sayHellow", method = RequestMethod.GET)
    @ResponseBody
    public void sayHellow(HttpServletRequest request, HttpServletResponse response, @CookieValue(required=false) String exac)throws LogicException {
        log.info("*** *** *** HELLOW *** *** ***");
    }

    private static int computeMaxPossibleSessionExpire(){
        int maxSessionExpire = SecurityConfig.accessTokenValiditySeconds+SecurityConfig.refreshTokenValiditySeconds;
        int accessValiditySeconds = 0;
        int refreshValiditySeconds = 0;
        if(accessValiditySeconds!=0 || refreshValiditySeconds!=0){
            maxSessionExpire = accessValiditySeconds + refreshValiditySeconds;
            if(accessValiditySeconds==0)
                maxSessionExpire = SecurityConfig.accessTokenValiditySeconds + refreshValiditySeconds;
            else if(refreshValiditySeconds==0)
                maxSessionExpire = accessValiditySeconds + SecurityConfig.refreshTokenValiditySeconds;
        }
        return maxSessionExpire;
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
        return Mes.createErrorMes("UNKN01", MessageUtil.getMessage("unkn.UNKN01"));
    }
}
