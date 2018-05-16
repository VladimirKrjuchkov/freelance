package com.pb.tel.data.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by vladimir on 15.05.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class InputFile {

    public InputFile(){}

    private String file_id;

    private Integer file_size;

    private String file_path;

    private Integer width;

    private Integer height;

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public Integer getFile_size() {
        return file_size;
    }

    public void setFile_size(Integer file_size) {
        this.file_size = file_size;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InputFile inputFile = (InputFile) o;

        if (file_id != null ? !file_id.equals(inputFile.file_id) : inputFile.file_id != null) return false;
        if (file_size != null ? !file_size.equals(inputFile.file_size) : inputFile.file_size != null) return false;
        if (file_path != null ? !file_path.equals(inputFile.file_path) : inputFile.file_path != null) return false;
        if (width != null ? !width.equals(inputFile.width) : inputFile.width != null) return false;
        return height != null ? height.equals(inputFile.height) : inputFile.height == null;
    }

    @Override
    public int hashCode() {
        int result = file_id != null ? file_id.hashCode() : 0;
        result = 31 * result + (file_size != null ? file_size.hashCode() : 0);
        result = 31 * result + (file_path != null ? file_path.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InputFile{" +
                "file_id='" + file_id + '\'' +
                ", file_size=" + file_size +
                ", file_path='" + file_path + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
