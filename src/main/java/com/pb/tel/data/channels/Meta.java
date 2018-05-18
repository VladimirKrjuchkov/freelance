package com.pb.tel.data.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 16.05.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Meta {

    public Meta(){}

    private Integer width;

    private Integer height;

    private Preview preview;

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Preview getPreview() {
        return preview;
    }

    public void setPreview(Preview preview) {
        this.preview = preview;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Meta meta = (Meta) o;

        if (width != null ? !width.equals(meta.width) : meta.width != null) return false;
        if (height != null ? !height.equals(meta.height) : meta.height != null) return false;
        return preview != null ? preview.equals(meta.preview) : meta.preview == null;
    }

    @Override
    public int hashCode() {
        int result = width != null ? width.hashCode() : 0;
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (preview != null ? preview.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "width=" + width +
                ", height=" + height +
                ", preview=" + preview +
                '}';
    }
}
