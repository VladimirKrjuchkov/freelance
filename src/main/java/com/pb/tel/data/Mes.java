package com.pb.tel.data;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 01.03.18.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Mes {
    public Mes(){}

    public Mes(MesState state, String description){
        this.state = state;
        this.code = code;
        this.description = description;
    }

    public Mes(MesState state, String code, String description){
        this.state = state;
        this.code = code;
        this.description = description;
    }

    public static enum MesState{
        ok,
        err,
        processing;
    }

    private MesState state;

    private String code;

    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mes mes = (Mes) o;

        if (state != mes.state) return false;
        if (code != null ? !code.equals(mes.code) : mes.code != null) return false;
        return description != null ? description.equals(mes.description) : mes.description == null;
    }

    @Override
    public int hashCode() {
        int result = state != null ? state.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Mes{" +
                "state=" + state +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
