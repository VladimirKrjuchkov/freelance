package com.pb.tel.data.novaposhta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by vladimir on 12.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MethodPropertie {

    public MethodPropertie(){}

    @JsonProperty("Documents")
    List<Document> documents;

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodPropertie that = (MethodPropertie) o;

        return documents != null ? documents.equals(that.documents) : that.documents == null;
    }

    @Override
    public int hashCode() {
        return documents != null ? documents.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MethodPropertie{" +
                "documents=" + documents +
                '}';
    }
}
