package com.pb.tel.data.meest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by vladimir on 08.06.18.
 */

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "errors")
public class Errors {

    public Errors(){}

    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Errors errors = (Errors) o;

        if (code != null ? !code.equals(errors.code) : errors.code != null) return false;
        return name != null ? name.equals(errors.name) : errors.name == null;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Errors{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
