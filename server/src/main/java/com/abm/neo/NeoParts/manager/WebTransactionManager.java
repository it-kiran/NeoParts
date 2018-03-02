package com.abm.neo.NeoParts.manager;

import com.abm.neo.NeoParts.entity.ProductDao;
import com.abm.neo.NeoParts.entity.WebTransactionDao;
import com.abm.neo.NeoParts.entity.WebTransactionLineItemDao;
import com.abm.neo.NeoParts.repository.ProductRepository;
import com.abm.neo.NeoParts.repository.WebTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WebTransactionManager {

    @Autowired
    private WebTransactionRepository webTransactionRepository;

    @Autowired
    private ProductRepository productRepository;


    public WebTransactionDao addTransaction(WebTransactionDao webTransactionDao) {
        return webTransactionRepository.save(webTransactionDao);
    }

    public WebTransactionDao getTransactionByPhoneNo(String phoneNo) {

        List<WebTransactionLineItemDao> webTransactionLineItemDaoList = new ArrayList<>();
        WebTransactionDao webTransactionDao =  webTransactionRepository.findOneByCustomerPhoneno(phoneNo);

        if(null != webTransactionDao)
        {
            for(WebTransactionLineItemDao lineItem: webTransactionDao.getWebTransactionLineItemDaoList()){

                ProductDao productDao = productRepository.findOneByProductNo(lineItem.getProductNo());
                if (null != productDao) {
                    lineItem.setDescription(productDao.getDescription());
                    webTransactionLineItemDaoList.add(lineItem);
                }

            }

            webTransactionDao.setWebTransactionLineItemDaoList(webTransactionLineItemDaoList);
        }

        return webTransactionDao;
    }
}
