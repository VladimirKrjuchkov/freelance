package com.pb.tel.data.novaposhta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by vladimir on 13.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NovaPoshtaResponse {

    public NovaPoshtaResponse(){};

    Boolean success;

    List<Data> data;

    List<String> errorCodes;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<String> errorCodes) {
        this.errorCodes = errorCodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NovaPoshtaResponse response = (NovaPoshtaResponse) o;

        if (success != null ? !success.equals(response.success) : response.success != null) return false;
        if (data != null ? !data.equals(response.data) : response.data != null) return false;
        return errorCodes != null ? errorCodes.equals(response.errorCodes) : response.errorCodes == null;
    }

    @Override
    public int hashCode() {
        int result = success != null ? success.hashCode() : 0;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (errorCodes != null ? errorCodes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NovaPoshtaResponse{" +
                "success=" + success +
                ", data=" + data +
                ", errorCodes=" + errorCodes +
                '}';
    }
}
