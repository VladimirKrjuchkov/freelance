package com.pb.tel.data.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 16.05.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class File {

    public File(){};

    private String url;

    private String type;

    private Integer size;

    private String name;

    private Integer height;

    private Meta meta;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        File file = (File) o;

        if (url != null ? !url.equals(file.url) : file.url != null) return false;
        if (type != null ? !type.equals(file.type) : file.type != null) return false;
        if (size != null ? !size.equals(file.size) : file.size != null) return false;
        if (name != null ? !name.equals(file.name) : file.name != null) return false;
        if (height != null ? !height.equals(file.height) : file.height != null) return false;
        return meta != null ? meta.equals(file.meta) : file.meta == null;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (meta != null ? meta.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "File{" +
                "url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", name='" + name + '\'' +
                ", height=" + height +
                ", meta=" + meta +
                '}';
    }
}
