package com.pb.tel.data.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 31.07.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JWTData {

    public JWTData(){};

    public JWTData(JWTKey login, JWTKey name){
        this.login = login;
        this.name = name;
    }

    JWTKey login;

    JWTKey name;

    public JWTKey getLogin() {
        return login;
    }

    public void setLogin(JWTKey login) {
        this.login = login;
    }

    public JWTKey getName() {
        return name;
    }

    public void setName(JWTKey name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JWTData jwtData = (JWTData) o;

        if (login != null ? !login.equals(jwtData.login) : jwtData.login != null) return false;
        return name != null ? name.equals(jwtData.name) : jwtData.name == null;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JWTData{" +
                "login=" + login +
                ", name=" + name +
                '}';
    }
}
