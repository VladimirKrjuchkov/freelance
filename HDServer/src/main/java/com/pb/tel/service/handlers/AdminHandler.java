package com.pb.tel.service.handlers;

import com.pb.tel.data.Account;
import com.pb.tel.data.enumerators.Status;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by vladimir on 17.07.19.
 */

@Service("adminHandler")
public class AdminHandler {

    @Resource(name="registredOpers")
    private List<Account> registredOpers;

    public Account getMostFreeOper(){
        Comparator<Account> ocomp = new AdminComporator();
        TreeSet<Account> freeOpers = new TreeSet(ocomp);
        registredOpers.stream().filter(registredOpers -> Status.ONLINE.equals(registredOpers.getStatus())).forEach(oper -> freeOpers.add(oper));
        if(freeOpers.size() <= 0){
            return null;
        }else{
            return freeOpers.first();
        }
    }

    class AdminComporator implements Comparator<Account> {

        public int compare(Account a, Account b){

            if(a.getConnectedAccounts().size()> b.getConnectedAccounts().size())
                return 1;
            else if(a.getConnectedAccounts().size()< b.getConnectedAccounts().size())
                return -1;
            else
                return 0;
        }
    }
}
