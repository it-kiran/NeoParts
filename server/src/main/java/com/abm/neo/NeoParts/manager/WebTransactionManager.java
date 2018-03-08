package com.abm.neo.NeoParts.manager;

import com.abm.neo.NeoParts.entity.ProductDao;
import com.abm.neo.NeoParts.entity.WebTransactionDao;
import com.abm.neo.NeoParts.entity.WebTransactionLineItemDao;
import com.abm.neo.NeoParts.repository.ProductRepository;
import com.abm.neo.NeoParts.repository.WebTransactionLineItemRepository;
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

    @Autowired
    private WebTransactionLineItemRepository webTransactionLineItemRepository;


    public WebTransactionDao addTransaction(WebTransactionDao webTransactionDao) {
        return webTransactionRepository.save(webTransactionDao);
    }

//    public WebTransactionDao getTransactionByPhoneNo(String phoneNo) {
//
//        List<WebTransactionLineItemDao> webTransactionLineItemDaoList = new ArrayList<>();
//        WebTransactionDao webTransactionDao =  webTransactionRepository.findOneByCustomerPhoneno(phoneNo);
//
////        if(null != webTransactionDao)
////        {
////            for(WebTransactionLineItemDao lineItem: webTransactionDao.getWebTransactionLineItemDaoList()){
////
////                ProductDao productDao = productRepository.findOneByProductNo(lineItem.getProductNo());
////                if (null != productDao) {
////                    lineItem.setDescription(productDao.getDescription());
////                    webTransactionLineItemDaoList.add(lineItem);
////                }
////            }
////            webTransactionDao.setWebTransactionLineItemDaoList(webTransactionLineItemDaoList);
////        }
//        return webTransactionDao;
//    }

    public WebTransactionLineItemDao addCartItem(WebTransactionLineItemDao webTransactionLineItemDao) {

       return webTransactionLineItemRepository.save(webTransactionLineItemDao);

    }

    public List<WebTransactionLineItemDao> getCartItem(String phoneNo) {

        List<Object[]> result = webTransactionLineItemRepository.getCartItemForCustomer(phoneNo);
        List<WebTransactionLineItemDao> webTransactionLineItemDaoList = new ArrayList<>();

        if(null != result)
        {
            for(Object[] j: result ){

                WebTransactionLineItemDao webTransactionLineItemDao = new WebTransactionLineItemDao();

                webTransactionLineItemDao.setTransactionLineItemId(Integer.parseInt(j[0].toString()));
                webTransactionLineItemDao.setDate(j[1].toString());
                webTransactionLineItemDao.setStatus(j[2].toString());
                webTransactionLineItemDao.setProductNo(j[3].toString());
                webTransactionLineItemDao.setSaleQuantity(Integer.parseInt(j[4].toString()));
                webTransactionLineItemDao.setRetail(Double.parseDouble(j[5].toString()));
                webTransactionLineItemDao.setRetailWithDiscount(Double.parseDouble(j[6].toString()));
                webTransactionLineItemDao.setTotalProductPrice(Double.parseDouble(j[7].toString()));
                webTransactionLineItemDao.setCustomerPhoneNo(j[8].toString());
                if(null != j[9]) {
                    webTransactionLineItemDao.setImage((byte[]) j[9]);
                }
                webTransactionLineItemDao.setDescription(j[10].toString());
                webTransactionLineItemDaoList.add(webTransactionLineItemDao);

            }
        }
        return webTransactionLineItemDaoList;

    }

    public void deleteCartItem(String phoneNo, int transactionLineItemId) {
        webTransactionLineItemRepository.deleteLineItemByCustomer(phoneNo, transactionLineItemId);
    }

}
