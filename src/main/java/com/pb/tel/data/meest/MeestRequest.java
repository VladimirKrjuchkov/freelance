package com.pb.tel.data.meest;

import javax.xml.bind.annotation.*;

/**
 * Created by vladimir on 05.06.18.
 */

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "param")
public class MeestRequest{

    public MeestRequest(){}

    @XmlElement
    private String login;

    @XmlElement
    private String function;

    @XmlElement
    private String where;

    @XmlElement
    private String sign;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeestRequest that = (MeestRequest) o;

        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (function != null ? !function.equals(that.function) : that.function != null) return false;
        if (where != null ? !where.equals(that.where) : that.where != null) return false;
        return sign != null ? sign.equals(that.sign) : that.sign == null;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (function != null ? function.hashCode() : 0);
        result = 31 * result + (where != null ? where.hashCode() : 0);
        result = 31 * result + (sign != null ? sign.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MeestRequest{" +
                "login='" + login + '\'' +
                ", function='" + function + '\'' +
                ", where='" + where + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
