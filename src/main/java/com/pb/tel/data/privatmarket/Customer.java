package com.pb.tel.data.privatmarket;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by vladimir on 02.04.18.
 */

@Entity(name = "PrivateMarketCustomers")
@NamedQueries({@NamedQuery(name = "Customers.findAll", query = "SELECT w FROM PrivateMarketCustomers w "),
               @NamedQuery(name = "Customers.findByExtId", query = "SELECT w FROM PrivateMarketCustomers w where w.extId = :extId")})

@Table(name = "PrivateMarketCustomers")
public class Customer  implements Serializable{

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(Customer.class.getCanonicalName());

    public Customer(){};

    @Id
    @Column(name = "extId", nullable=false)
    private String extId;

    @Column(name = "idEkb", nullable=true)
    private Integer idEkb;

    @Column(name = "phone", nullable=false)
    private String phone;

    @Column(name = "messenger", nullable=false)
    private String messenger;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable=false)
    protected Date date = new Date();

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public Integer getIdEkb() {
        return idEkb;
    }

    public void setIdEkb(Integer idEkb) {
        this.idEkb = idEkb;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (extId != null ? !extId.equals(customer.extId) : customer.extId != null) return false;
        if (idEkb != null ? !idEkb.equals(customer.idEkb) : customer.idEkb != null) return false;
        if (phone != null ? !phone.equals(customer.phone) : customer.phone != null) return false;
        if (messenger != null ? !messenger.equals(customer.messenger) : customer.messenger != null) return false;
        return date != null ? date.equals(customer.date) : customer.date == null;
    }

    @Override
    public int hashCode() {
        int result = extId != null ? extId.hashCode() : 0;
        result = 31 * result + (idEkb != null ? idEkb.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (messenger != null ? messenger.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "extId='" + extId + '\'' +
                ", idEkb=" + idEkb +
                ", phone='" + phone + '\'' +
                ", messenger='" + messenger + '\'' +
                ", date=" + date +
                '}';
    }
}
