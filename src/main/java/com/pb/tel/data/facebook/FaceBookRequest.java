package com.pb.tel.data.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pb.tel.data.Request;

import java.util.List;

/**
 * Created by vladimir on 11.04.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FaceBookRequest{

    public FaceBookRequest(){}

    private String object;

    private List<Entry> entry;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FaceBookRequest that = (FaceBookRequest) o;

        if (object != null ? !object.equals(that.object) : that.object != null) return false;
        return entry != null ? entry.equals(that.entry) : that.entry == null;
    }

    @Override
    public int hashCode() {
        int result = object != null ? object.hashCode() : 0;
        result = 31 * result + (entry != null ? entry.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FaceBookRequest{" +
                "object='" + object + '\'' +
                ", entry=" + entry +
                '}';
    }
}
