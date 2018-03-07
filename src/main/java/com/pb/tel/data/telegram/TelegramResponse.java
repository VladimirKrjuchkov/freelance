package com.pb.tel.data.telegram;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by vladimir on 06.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TelegramResponse {

    public TelegramResponse(){};

    Boolean ok;

    Message result;

    String description;

    String error_code;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public Message getResult() {
        return result;
    }

    public void setResult(Message result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    @Override
    public String toString() {
        return "TelegramResponse{" +
                "ok=" + ok +
                ", result=" + result +
                ", description='" + description + '\'' +
                ", error_code='" + error_code + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TelegramResponse that = (TelegramResponse) o;

        if (ok != null ? !ok.equals(that.ok) : that.ok != null) return false;
        if (result != null ? !result.equals(that.result) : that.result != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return error_code != null ? error_code.equals(that.error_code) : that.error_code == null;
    }

    @Override
    public int hashCode() {
        int result1 = ok != null ? ok.hashCode() : 0;
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        result1 = 31 * result1 + (description != null ? description.hashCode() : 0);
        result1 = 31 * result1 + (error_code != null ? error_code.hashCode() : 0);
        return result1;
    }
}
