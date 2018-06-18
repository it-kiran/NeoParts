package com.abm.neo.NeoParts.manager;

import com.abm.neo.NeoParts.dto.CustomerDao;
import com.abm.neo.NeoParts.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by apatel2 on 5/17/17.
 */

@Component
public class CustomerManager {

    @Autowired
    private CustomerRepository customerRepository;


    public void addCustomer(CustomerDao customerDao) {

        customerRepository.save(customerDao);
    }

    public List<CustomerDao> getCustomer() {

        return customerRepository.findAll();
    }

    public CustomerDao getUserLoginDetails(String username, String password) {

        CustomerDao customerDao = new CustomerDao();

         customerDao = this.customerRepository.findByEmailAndPassword(username, password);
        if(null != customerDao && customerDao.getEmail().equalsIgnoreCase(username)) {
            customerDao.setValidUser(true);
            return customerDao;
        }
        else if(null == customerDao){
            CustomerDao customerDao1 = new CustomerDao();
            customerDao1.setValidUser(false);
            return customerDao1;
        }

        return new CustomerDao();
    }

    public CustomerDao getCustomerDetailsByEmail(String email) {

        if(null != email){
            return this.customerRepository.findByEmail(email);
        }
        return null;
    }
}

