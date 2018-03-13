package com.pb.tel.data.novaposhta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by vladimir on 12.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NovaPoshtaRequest {

    public NovaPoshtaRequest(){}

    String modelName;

    String calledMethod;

    String language;

    MethodPropertie methodProperties;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getCalledMethod() {
        return calledMethod;
    }

    public void setCalledMethod(String calledMethod) {
        this.calledMethod = calledMethod;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public MethodPropertie getMethodProperties() {
        return methodProperties;
    }

    public void setMethodProperties(MethodPropertie methodProperties) {
        this.methodProperties = methodProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NovaPoshtaRequest request = (NovaPoshtaRequest) o;

        if (modelName != null ? !modelName.equals(request.modelName) : request.modelName != null) return false;
        if (calledMethod != null ? !calledMethod.equals(request.calledMethod) : request.calledMethod != null)
            return false;
        if (language != null ? !language.equals(request.language) : request.language != null) return false;
        return methodProperties != null ? methodProperties.equals(request.methodProperties) : request.methodProperties == null;
    }

    @Override
    public int hashCode() {
        int result = modelName != null ? modelName.hashCode() : 0;
        result = 31 * result + (calledMethod != null ? calledMethod.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (methodProperties != null ? methodProperties.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NovaPoshtaRequest{" +
                "modelName='" + modelName + '\'' +
                ", calledMethod='" + calledMethod + '\'' +
                ", language='" + language + '\'' +
                ", methodProperties=" + methodProperties +
                '}';
    }
}
