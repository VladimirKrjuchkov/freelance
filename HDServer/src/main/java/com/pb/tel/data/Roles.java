package com.pb.tel.data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by vladimir on 26.07.19.
 */
public enum Roles {
    ROLE_APP("ROLE_APP"),
    ROLE_USER("ROLE_USER"),
    ROLE_H2H("ROLE_H2H"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MAIN_AGENT("ROLE_MAIN_AGENT"),
    ROLE_READ_ADMIN("ROLE_READ_ADMIN"),
    ROLE_WRITE_ADMIN("ROLE_WRITE_ADMIN");


    private String roleCode;

    Roles(String roleCode){
        this.roleCode = roleCode;
    }

    public static Roles getByCode(String roleCode){
        if(roleCode!=null)
            for(Roles role : Roles.values()){
                if(role.getCode().equals(roleCode.trim()))
                    return role;
            }
        return null;
    }

    public String getCode() {
        return roleCode;
    }

    @Override
    public String toString(){
        return "Roles = "+this.roleCode;
    }




    public static List<String> convertAuthorityToString(Collection<? extends GrantedAuthority> authorities){
        List<String> result = new ArrayList<String>();
        for(GrantedAuthority auth : authorities){
            result.add(auth.getAuthority());
        }
        return result;
    }

    public static List<Roles> convertAuthorityToRoles(Collection<? extends GrantedAuthority>  authorities){
        List<Roles> result = new ArrayList<Roles>();
        for(GrantedAuthority auth : authorities){
            result.add(Roles.getByCode(auth.getAuthority()));
        }
        return result;
    }

    public static Collection<GrantedAuthority> convertRolesToAuthority(Collection<Roles> roles){
        List<GrantedAuthority> authority = new ArrayList<GrantedAuthority>();
        for(Roles role : roles)
            authority.add(new SimpleGrantedAuthority(role.getCode()));
        return authority;
    }

    public static String rolesAsString(Roles...roles){
        String result="";
        for(Roles role : roles)
            result+=","+role.getCode();
        return result.replaceFirst(",", "");
    }

    public static Set<Roles> stringAsRoles(String rolesValue){
        Set<Roles> result = Collections.emptySet();
        if(StringUtils.hasText(rolesValue)){
            result = new HashSet<Roles>();
            String [] roles = rolesValue.split(",");
            for(String role : roles)
                result.add(Roles.getByCode(role.trim()));
            result.remove(null);
        }
        return result;
    }
}
