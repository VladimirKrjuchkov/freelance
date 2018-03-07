package com.pb.tel.data.telegram;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by vladimir on 07.03.18.
 */
public class SimpleTelegramResponse extends TelegramResponse{

    public SimpleTelegramResponse(){};

    @JsonIgnore
    Boolean result;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SimpleTelegramResponse that = (SimpleTelegramResponse) o;

        return result != null ? result.equals(that.result) : that.result == null;
    }

    @Override
    public int hashCode() {
        int result1 = super.hashCode();
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        return result1;
    }

    @Override
    public String toString() {
        return "SimpleTelegramResponse{" +
                "result=" + result +
                '}';
    }
}
