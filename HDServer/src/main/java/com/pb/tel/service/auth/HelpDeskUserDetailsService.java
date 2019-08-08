package com.pb.tel.service.auth;

import com.pb.tel.dao.OperatorDaoImpl;
import com.pb.tel.data.Operator;
import com.pb.tel.utils.MessageUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by vladimir on 29.07.19.
 */

@Service("helpDeskUserDetailsService")
public class HelpDeskUserDetailsService implements UserDetailsService {

    @Resource(name="operatorDaoImpl")
    private OperatorDaoImpl operatorDaoImpl;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        login = login.split(",")[1];
        Operator user = operatorDaoImpl.getUserByLogin(login);
        if(user == null)
            throw new UsernameNotFoundException(MessageUtil.getMessage("auth.AUTH06"));
        return user;
    }

}
