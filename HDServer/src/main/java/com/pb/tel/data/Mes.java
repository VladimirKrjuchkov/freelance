package com.pb.tel.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by vladimir on 26.07.19.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Mes implements Serializable {

    public Mes(){}

    public Mes(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static Mes createErrorMes(String code, String desc){
        Mes result = new Mes(code, desc);
        result.setState(MesState.err);
        return result;
    }

    public static enum MesState{
        ok,
        err,
        processing;
    }

    private MesState state = MesState.ok;

    private String code;

    private String desc;

    private Operator user;

    public MesState getState() {
        return state;
    }

    public void setState(MesState state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Operator getUser() {
        return user;
    }

    public void setUser(Operator user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mes mes = (Mes) o;

        if (state != mes.state) return false;
        if (code != null ? !code.equals(mes.code) : mes.code != null) return false;
        if (desc != null ? !desc.equals(mes.desc) : mes.desc != null) return false;
        return user != null ? user.equals(mes.user) : mes.user == null;
    }

    @Override
    public int hashCode() {
        int result = state != null ? state.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Mes{" +
                "state=" + state +
                ", code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                ", user=" + user +
                '}';
    }
}
