package com.pb.tel.dao;

import com.pb.tel.data.privatmarket.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
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
        log.info("PrivateMarketCustomers.findAll at "+(System.currentTimeMillis() - start)+"ms");
        return result;
    }
}
