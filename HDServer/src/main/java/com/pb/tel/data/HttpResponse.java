package com.pb.tel.data;

import java.io.Serializable;

/**
 * Created by vladimir on 05.07.19.
 */
public class HttpResponse implements Serializable {

    public HttpResponse(){};

    public HttpResponse(boolean ok, String message){
        this.ok = ok;
        this.message = message;
    };

    public static HttpResponse getErrorResponse(String message){
        return new HttpResponse(false, message);
    }

    public static HttpResponse getSuccessResponse(String message){
        return new HttpResponse(true, message);
    }

    private boolean ok;

    private String message;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpResponse that = (HttpResponse) o;

        if (ok != that.ok) return false;
        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override
    public int hashCode() {
        int result = (ok ? 1 : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "ok=" + ok +
                ", message='" + message + '\'' +
                '}';
    }
}
