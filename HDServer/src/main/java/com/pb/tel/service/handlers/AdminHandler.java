package com.pb.tel.service.handlers;

import com.pb.tel.data.AdminAccount;
import com.pb.tel.data.enumerators.AdminStatus;
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
    private List<AdminAccount> registredOpers;

    public AdminAccount getMostFreeOper(){
        Comparator<AdminAccount> ocomp = new AdminComporator();
        TreeSet<AdminAccount> freeOpers = new TreeSet(ocomp);
        registredOpers.stream().filter(registredOpers -> AdminStatus.ONLINE.equals(registredOpers.getAdminStatus())).forEach(oper -> freeOpers.add(oper));
        if(freeOpers.size() <= 0){
            return null;
        }else{
            return freeOpers.first();
        }
    }

    class AdminComporator implements Comparator<AdminAccount> {

        public int compare(AdminAccount a, AdminAccount b){

            if(a.getClients().size()> b.getClients().size())
                return 1;
            else if(a.getClients().size()< b.getClients().size())
                return -1;
            else
                return 0;
        }
    }
}
