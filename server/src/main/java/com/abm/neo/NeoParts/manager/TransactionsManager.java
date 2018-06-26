package com.abm.neo.NeoParts.manager;

import com.abm.neo.NeoParts.entity.TransactionDao;
import com.abm.neo.NeoParts.entity.TransactionLineItemDao;
import com.abm.neo.NeoParts.entity.WebTransactionLineItemDao;
import com.abm.neo.NeoParts.repository.TransactionLineItemRepository;
import com.abm.neo.NeoParts.repository.TransactionRepository;
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
            
            // Now set transactionDao
            transactionDao.setCustomerPhoneno(customerPhoneNo);
            transactionDao.setDate(currentDate);
            transactionDao.setStatus("Online");
            transactionDao.setSubtotal(totalAmount);
            transactionDao.setTotalAmount(totalAmount);
            transactionDao.setQuantity(totalQuantity);
            transactionDao.setTransactionLineItemDaoList(transactionLineItemList);
            
            transactionDao =  transactionRepository.save(transactionDao);
            
        }
        
        return transactionDao;
    }
    
    
}

