package com.abm.neo.NeoParts.manager;

import com.abm.neo.NeoParts.repository.TransactionLineItemRepository;
import com.abm.neo.NeoParts.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by apatel2 on 5/18/17.
 */
@Component
public class TransactionsManager {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionLineItemRepository transactionLineItemRepository;

//    @Autowired
//    private Utility utility;
//
//    @Autowired
//    private ProductRepository productRepository;

    @Autowired
    private CustomerManager customerManager;

    
    public void addTransaction() {


    }
}

