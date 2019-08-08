package com.pb.tel.service.auth;

import com.pb.tel.dao.OperatorDaoImpl;
import com.pb.tel.data.Operator;
import com.pb.tel.utils.MessageUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * Created by vladimir on 29.07.19.
 */

@Service("casheProxyHelpDeskUserDetailsService")
public class CasheProxyHelpDeskUserDetailsService {

    private static final Logger log = Logger.getLogger(CasheProxyHelpDeskUserDetailsService.class.getCanonicalName());

    @Resource(name = "operatorDaoImpl")
    private OperatorDaoImpl operatorDaoImpl;


    @Cacheable(cacheManager = "cacheManager", cacheNames = "com.pb.tel.service.auth.HelpDeskUserCache")
    public Object cachableLoadUserByUsername(String username) {
        Operator details = null;
        details = operatorDaoImpl.getUserByLogin(username);
        if (details == null)
            throw new UsernameNotFoundException(MessageUtil.getMessage("auth.AUTH01", username));
        return details;
    }
}
