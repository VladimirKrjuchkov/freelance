package com.pb.tel.service.exception;

/**
 * Created by vladimir on 07.03.18.
 */
public class UnresponsibleException extends Exception{

    private static final long serialVersionUID = -3155559670065775765L;

    Integer id;

    private String code;

    private String description;

    public UnresponsibleException(Integer id, String code, String description){
        this.id = id;
        this.code = code;
        this.description = description;
    }

    public UnresponsibleException(Integer id, String description, Exception e){
        super(e);
        this.id = id;
        this.description = description;
    }


    public UnresponsibleException(String code, String description){
        this.code = code;
        this.description = description;
    }

    public Integer getId(){ return id; }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "UnresponsibleException{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
