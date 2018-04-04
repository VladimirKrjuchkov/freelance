package com.pb.tel.dao;

import com.pb.tel.data.privatmarket.Customer;
import com.pb.tel.service.exception.TelegramException;
import com.pb.util.zvv.PropertiesUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vladimir on 02.04.18.
 */

@Repository("customerDaoImpl")
@Transactional
public class CustomerDaoImpl implements CustomerDao{

    private static final Logger log = Logger.getLogger(CustomerDaoImpl.class.getCanonicalName());

    @PersistenceContext
    private EntityManager em;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true, propagation= Propagation.SUPPORTS)
    public List<Customer> getAll(){
        long start = System.currentTimeMillis();
        List<Customer> result = em.createNamedQuery("Customers.findAll", Customer.class).getResultList();
        log.log(Level.INFO, "Customers.findAll at "+(System.currentTimeMillis() - start)+"ms");
        return result;
    }

    @Transactional(readOnly = true, propagation= Propagation.SUPPORTS)
    public List<Customer> getById(String id){
        long start = System.currentTimeMillis();
        TypedQuery<Customer> query = em.createNamedQuery("Customers.findByExtId", Customer.class);
        query.setParameter("extId", id);
        List<Customer> result = query.getResultList();
        log.log(Level.INFO, "Customers.findByExtId at "+(System.currentTimeMillis() - start)+"ms");
        return result;
    }

    @Transactional(rollbackFor=Exception.class)
    public Customer addCustomer(Customer customer) throws TelegramException {
        try {
            long start = System.currentTimeMillis();
            em.persist(customer);
            log.log(Level.INFO, "setCustomer at " + (System.currentTimeMillis() - start) + "ms");
        }catch (Exception e){
            throw new TelegramException(PropertiesUtil.getProperty("ident_error"), Integer.parseInt(customer.getExtId()));
        }
        return customer;
    }
}
