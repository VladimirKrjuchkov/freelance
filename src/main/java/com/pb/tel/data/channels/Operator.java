package com.pb.tel.data.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by vladimir on 28.03.18.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Operator {

    public Operator(){};

    private String status;

    private String role;

    private String photo;

    private String name;

    private String id;

    private Integer chatsCount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getChatsCount() {
        return chatsCount;
    }

    public void setChatsCount(Integer chatsCount) {
        this.chatsCount = chatsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Operator operator = (Operator) o;

        if (status != null ? !status.equals(operator.status) : operator.status != null) return false;
        if (role != null ? !role.equals(operator.role) : operator.role != null) return false;
        if (photo != null ? !photo.equals(operator.photo) : operator.photo != null) return false;
        if (name != null ? !name.equals(operator.name) : operator.name != null) return false;
        if (id != null ? !id.equals(operator.id) : operator.id != null) return false;
        return chatsCount != null ? chatsCount.equals(operator.chatsCount) : operator.chatsCount == null;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (chatsCount != null ? chatsCount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "status='" + status + '\'' +
                ", role='" + role + '\'' +
                ", photo='" + photo + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", chatsCount=" + chatsCount +
                '}';
    }
}
