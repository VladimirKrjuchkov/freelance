package com.pb.tel.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;

/**
 * Created by vladimir on 16.05.18.
 */
public class File {

    public File(){}

    public File(String url){
        this.url = url;
    }

    private String id;

    private String path;

    private byte[] data;

    private String type;

    private Integer size;

    private String name;

    private Integer height;

    private Integer width;

    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
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

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
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

        File file = (File) o;

        if (id != null ? !id.equals(file.id) : file.id != null) return false;
        if (path != null ? !path.equals(file.path) : file.path != null) return false;
        if (!Arrays.equals(data, file.data)) return false;
        if (type != null ? !type.equals(file.type) : file.type != null) return false;
        if (size != null ? !size.equals(file.size) : file.size != null) return false;
        if (name != null ? !name.equals(file.name) : file.name != null) return false;
        if (height != null ? !height.equals(file.height) : file.height != null) return false;
        if (width != null ? !width.equals(file.width) : file.width != null) return false;
        return url != null ? url.equals(file.url) : file.url == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(data);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "File{" +
                "id='" + id + '\'' +
                ", path='" + path + '\'' +
                ", data=" + Arrays.toString(data) +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", name='" + name + '\'' +
                ", height=" + height +
                ", width=" + width +
                ", url='" + url + '\'' +
                '}';
    }
}
