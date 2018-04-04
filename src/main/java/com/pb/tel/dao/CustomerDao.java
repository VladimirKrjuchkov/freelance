package com.pb.tel.dao;

import com.pb.tel.data.privatmarket.Customer;
import com.pb.tel.service.exception.TelegramException;

import java.util.List;

/**
 * Created by vladimir on 02.04.18.
 */
public interface CustomerDao {

    public List<Customer> getAll();

    public List<Customer> getById(String id);

    public Customer addCustomer(Customer customer) throws TelegramException;
}
