package com.pb.tel.data.meest;

import javax.xml.bind.annotation.*;

/**
 * Created by vladimir on 05.06.18.
 */

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "return")
public class MeestResponse {

    public MeestResponse(){}

    @XmlElement
    private String api;

    @XmlElement
    private String apiversion;

    @XmlElement
    private ResultTable result_table;

    @XmlElement
    private Errors errors;

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getApiversion() {
        return apiversion;
    }

    public void setApiversion(String apiversion) {
        this.apiversion = apiversion;
    }

    public ResultTable getResult_table() {
        return result_table;
    }

    public void setResult_table(ResultTable result_table) {
        this.result_table = result_table;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeestResponse that = (MeestResponse) o;

        if (api != null ? !api.equals(that.api) : that.api != null) return false;
        if (apiversion != null ? !apiversion.equals(that.apiversion) : that.apiversion != null) return false;
        if (result_table != null ? !result_table.equals(that.result_table) : that.result_table != null) return false;
        return errors != null ? errors.equals(that.errors) : that.errors == null;
    }

    @Override
    public int hashCode() {
        int result = api != null ? api.hashCode() : 0;
        result = 31 * result + (apiversion != null ? apiversion.hashCode() : 0);
        result = 31 * result + (result_table != null ? result_table.hashCode() : 0);
        result = 31 * result + (errors != null ? errors.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MeestResponse{" +
                "api='" + api + '\'' +
                ", apiversion='" + apiversion + '\'' +
                ", result_table=" + result_table +
                ", errors=" + errors +
                '}';
    }
}
