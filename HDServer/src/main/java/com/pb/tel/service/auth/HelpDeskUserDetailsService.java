package com.pb.tel.service.auth;

import com.pb.tel.dao.UserDaoImpl;
import com.pb.tel.data.User;
import com.pb.tel.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CasheProxyHelpDeskUserDetailsService casheProxyPaperlessUserDetailsService;


    @Resource(name="userDaoImpl")
    private UserDaoImpl userDaoImpl;

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
//		Object result = casheProxyPaperlessUserDetailsService.cachableLoadUserByUsername(username);
//		return (UserDetails)result;
//	}

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        login = login.split(",")[1];
        User user = userDaoImpl.getUserByLogin(login);
        if(user == null)
            throw new UsernameNotFoundException(MessageUtil.getMessage("auth.AUTH06"));
        return user;
    }

}
