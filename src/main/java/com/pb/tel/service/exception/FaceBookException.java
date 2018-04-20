package com.pb.tel.service.exception;


import com.pb.tel.data.facebook.Attachment;

/**
 * Created by vladimir on 20.04.18.
 */
public class FaceBookException extends Exception{

    private static final long serialVersionUID = -3155559670065775765L;

    private String code;
    private String description;
    private String userId;
    private Attachment attachment;

    public FaceBookException(String code, String description, String userId){
        this.code = code;
        this.description = description;
        this.userId = userId;
    }

    public FaceBookException(String description, String userId){
        this.description = description;
        this.userId = userId;
    }

    public FaceBookException(String description, String userId, Attachment attachment){
        this.description = description;
        this.userId = userId;
        this.attachment = attachment;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FaceBookException that = (FaceBookException) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return attachment != null ? attachment.equals(that.attachment) : that.attachment == null;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (attachment != null ? attachment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FaceBookException{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", userId='" + userId + '\'' +
                ", attachment=" + attachment +
                '}';
    }
}
