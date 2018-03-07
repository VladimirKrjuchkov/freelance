package com.pb.tel.data.telegram;

/**
 * Created by vladimir on 07.03.18.
 */
public abstract class TelegramUser {

    private Integer id;

    public abstract String getCall_back_data();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TelegramUser that = (TelegramUser) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TelegramUser{" +
                "id=" + id +
                '}';
    }
}
