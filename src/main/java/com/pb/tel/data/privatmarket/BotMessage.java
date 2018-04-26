package com.pb.tel.data.privatmarket;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by vladimir on 24.04.18.
 */

@Entity(name = "BotMessages")
@NamedQueries({@NamedQuery(name = "BotMessage.findByLang", query = "SELECT w FROM BotMessages w where w.lang = :lang")})
@Table(name = "BotMessages")
public class BotMessage implements Serializable{

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(BotMessage.class.getCanonicalName());

    public BotMessage(){}

    @Id
    @Column(name = "id", nullable=false)
    private String id;

    @Column(name = "code", nullable=false)
    private String code;

    @Column(name = "message", nullable=false)
    private String message;

    @Column(name = "lang", nullable=false)
    private String lang;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BotMessage that = (BotMessage) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return lang != null ? lang.equals(that.lang) : that.lang == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BotMessage{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", lang='" + lang + '\'' +
                '}';
    }
}
