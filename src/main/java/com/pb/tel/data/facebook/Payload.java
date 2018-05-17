package com.pb.tel.data.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by vladimir on 17.04.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Payload {

    public Payload(){};

    private String template_type;

    private String text;

    private String url;

    List<Buttons> buttons;

    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Buttons> getButtons() {
        return buttons;
    }

    public void setButtons(List<Buttons> buttons) {
        this.buttons = buttons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payload payload = (Payload) o;

        if (template_type != null ? !template_type.equals(payload.template_type) : payload.template_type != null)
            return false;
        if (text != null ? !text.equals(payload.text) : payload.text != null) return false;
        if (url != null ? !url.equals(payload.url) : payload.url != null) return false;
        return buttons != null ? buttons.equals(payload.buttons) : payload.buttons == null;
    }

    @Override
    public int hashCode() {
        int result = template_type != null ? template_type.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (buttons != null ? buttons.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "template_type='" + template_type + '\'' +
                ", text='" + text + '\'' +
                ", url='" + url + '\'' +
                ", buttons=" + buttons +
                '}';
    }
}
