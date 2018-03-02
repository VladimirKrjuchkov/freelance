package com.pb.tel.data;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 01.03.18.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Mes {
    public Mes(){}

    public Mes(String state, String description){
        this.state = state;
        this.code = code;
        this.description = description;
    }

    public Mes(String state, String code, String description){
        this.state = state;
        this.code = code;
        this.description = description;
    }

    private String state;

    private String code;

    private String description;

    public String getState() {
        return state;
    }

    public void setState(String state) {
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

        if (!state.equals(mes.state)) return false;
        if (!code.equals(mes.code)) return false;
        return description.equals(mes.description);
    }

    @Override
    public int hashCode() {
        int result = state.hashCode();
        result = 31 * result + code.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Mes{" +
                "state='" + state + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
