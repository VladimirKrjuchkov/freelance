package com.pb.tel.data.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 16.05.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Preview {

    public Preview(){}

    private Integer width;

    private Integer size;

    private Integer height;

    private String url;

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Preview preview = (Preview) o;

        if (width != null ? !width.equals(preview.width) : preview.width != null) return false;
        if (size != null ? !size.equals(preview.size) : preview.size != null) return false;
        if (height != null ? !height.equals(preview.height) : preview.height != null) return false;
        return url != null ? url.equals(preview.url) : preview.url == null;
    }

    @Override
    public int hashCode() {
        int result = width != null ? width.hashCode() : 0;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Preview{" +
                "width=" + width +
                ", size=" + size +
                ", height=" + height +
                ", url='" + url + '\'' +
                '}';
    }
}
