package com.pb.tel.data.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 31.07.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JWTKey {

    public JWTKey(){};

    public JWTKey(boolean show, String lable, String value){
        this.show = show;
        this.lable = lable;
        this.value = value;
    }

    boolean show;

    String lable;

    String value;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JWTKey jwtKey = (JWTKey) o;

        if (show != jwtKey.show) return false;
        if (lable != null ? !lable.equals(jwtKey.lable) : jwtKey.lable != null) return false;
        return value != null ? value.equals(jwtKey.value) : jwtKey.value == null;
    }

    @Override
    public int hashCode() {
        int result = (show ? 1 : 0);
        result = 31 * result + (lable != null ? lable.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JWTKey{" +
                "show=" + show +
                ", lable='" + lable + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
