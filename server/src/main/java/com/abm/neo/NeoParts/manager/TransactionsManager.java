package com.abm.neo.NeoParts.manager;

import com.abm.neo.NeoParts.dto.CustomerDao;
import com.abm.neo.NeoParts.entity.TransactionDao;
import com.abm.neo.NeoParts.entity.TransactionLineItemDao;
import com.abm.neo.NeoParts.entity.WebTransactionLineItemDao;
import com.abm.neo.NeoParts.repository.CustomerRepository;
import com.abm.neo.NeoParts.repository.TransactionLineItemRepository;
import com.abm.neo.NeoParts.repository.TransactionRepository;
import com.abm.neo.NeoParts.repository.WebTransactionLineItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by apatel2 on 5/18/17.
 */
@Component
public class TransactionsManager {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionLineItemRepository transactionLineItemRepository;

    @Autowired
    private WebTransactionLineItemRepository webTransactionLineItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

//    @Autowired
//    private Utility utility;
//
//    @Autowired
//    private ProductRepository productRepository;

    @Autowired
    private CustomerManager customerManager;


    public TransactionDao addTransaction(List<WebTransactionLineItemDao> webTransactionLineItemDaos) {

        List<TransactionLineItemDao> transactionLineItemList = new ArrayList<>();
        TransactionDao transactionDao = new TransactionDao();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        if(null !=webTransactionLineItemDaos && webTransactionLineItemDaos.size() > 0){

            String currentDate = dtf.format(now);
            int totalQuantity = 0;
            double totalAmount = 0.00;
            String customerName;
            String customerPhoneNo = null;

            for(WebTransactionLineItemDao lineItem: webTransactionLineItemDaos){

                TransactionLineItemDao transactionLineItemDao = new TransactionLineItemDao();

                transactionLineItemDao.setProductId(lineItem.getProductId());
                transactionLineItemDao.setProductNo(lineItem.getProductNo());
                transactionLineItemDao.setDescription(lineItem.getDescription());
                transactionLineItemDao.setDate(currentDate);
                transactionLineItemDao.setRetail(lineItem.getRetail());
                transactionLineItemDao.setRetailWithDiscount(lineItem.getRetail());
                transactionLineItemDao.setSaleQuantity(lineItem.getSaleQuantity());
                transactionLineItemDao.setTotalProductPrice(lineItem.getRetail() * lineItem.getSaleQuantity());
                transactionLineItemDao.setStatus(lineItem.getStatus());
                
                totalQuantity = +totalQuantity +lineItem.getSaleQuantity();
                totalAmount =+ totalAmount +lineItem.getRetail();
                
                customerPhoneNo = lineItem.getCustomerPhoneNo();
                
                transactionLineItemList.add(transactionLineItemDao);
            }

             CustomerDao customerDao = customerRepository.findByPhoneNo(customerPhoneNo);
            
            // Now set transactionDao
            transactionDao.setCustomerPhoneno(customerPhoneNo);
            if(null != customerDao)
            {
                transactionDao.setCustomerFirstLastName(customerDao.getName());
            }
            transactionDao.setDate(currentDate);
            transactionDao.setStatus("Online");
            transactionDao.setSubtotal(totalAmount);
            transactionDao.setTotalAmount(totalAmount);
            transactionDao.setQuantity(totalQuantity);
            transactionDao.setTransactionLineItemDaoList(transactionLineItemList);
            
            transactionDao =  transactionRepository.save(transactionDao);

            // This mean Online order successfully inserted into main transaction table, so i need to delete line item from web line item table.
            if(null != transactionDao && transactionDao.getTransactionComId() > 0) {
                webTransactionLineItemRepository.deleteAllByCustomerPhoneNo(transactionDao.getCustomerPhoneno());

                // TODO also need to send an email to customer with order details.
            }

            
        }
        
        return transactionDao;
    }
    
    
}

