package com.abm.pos.ABMPos.manager;

import com.abm.pos.ABMPos.dao.CustomerDao;
import com.abm.pos.ABMPos.dao.EmployeeDao;
import com.abm.pos.ABMPos.dao.StoreCreditDao;
import com.abm.pos.ABMPos.dao.TransactionDao;
import com.abm.pos.ABMPos.repository.CustomerRepository;
import com.abm.pos.ABMPos.repository.StoreCreditRepository;
import com.abm.pos.ABMPos.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by apatel2 on 5/17/17.
 */

@Component
public class CustomerManager {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreCreditRepository storeCreditRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public void addCustomer(CustomerDao customerDao) {

        customerRepository.save(customerDao);
    }

    public List<CustomerDao> getCustomer() {

        return customerRepository.findAll();
    }

    public String deleteCustomer(String phoneNo) {

        // Here before deleting customer i need to check whether this customer has any transaction or not, if yes then can not delete for now.
        // TODO need to figure out active and in active scenario, Same as product. Also need to check for store credit as well loyalty points.
        String response;
        TransactionDao transactionDao =  transactionRepository.findFirstByCustomerPhoneno(phoneNo);


       if(null == transactionDao)
       {
           customerRepository.delete(phoneNo);
           response = "Customer Deleted Successfully!!";
       }
       else
       {
           response = "Sorry, Can not Delete This Customer, Because this Customer Has Previous Sales!!";
       }

       return response;

    }

    public void addCustomerStoreCredit(StoreCreditDao storeCreditDao) {

        storeCreditRepository.save(storeCreditDao);

        // After Adding store credit credit into store_credit table i need to add or update store credit for that particular customer

        CustomerDao customerDao = new CustomerDao();

        if(null != storeCreditDao)
        {
            customerDao = customerRepository.findByPhoneNo(storeCreditDao.getCustomerPhoneno());

            if(null != customerDao)
            {
                customerDao.setStoreCredit(customerDao.getStoreCredit() + storeCreditDao.getAmount());
                customerRepository.save(customerDao);
            }
        }





    }

    public List<StoreCreditDao> getCustomerStoreCreditHistory(String phoneNo) {

        return storeCreditRepository.findAllByCustomerPhoneno(phoneNo);
    }

    public CustomerDao getCustomerByPhoneNo(String phoneNo) {

       return customerRepository.findByPhoneNo(phoneNo);
    }
}
