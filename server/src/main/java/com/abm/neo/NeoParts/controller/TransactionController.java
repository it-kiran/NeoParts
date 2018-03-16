package com.abm.neo.NeoParts.controller;

import com.abm.neo.NeoParts.entity.WebTransactionDao;
import com.abm.neo.NeoParts.entity.WebTransactionLineItemDao;
import com.abm.neo.NeoParts.manager.WebTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@CrossOrigin(origins = {"*"})
public class TransactionController {

    @Autowired
    private WebTransactionManager webTransactionManager;

    @RequestMapping(value = "/addToCart", method = RequestMethod.POST, consumes = "application/json")
    public WebTransactionDao addTransactionToDB(@RequestBody WebTransactionDao webTransactionDao)
    {
       return webTransactionManager.addTransaction(webTransactionDao);
    }
    @RequestMapping(value = "/addCartItem", method = RequestMethod.POST, consumes = "application/json")
    public WebTransactionLineItemDao addCartItem(@RequestBody WebTransactionLineItemDao webTransactionDao)
    {
        return webTransactionManager.addCartItem(webTransactionDao);
    }
    @RequestMapping(value = "/getCartItem", method = RequestMethod.GET, produces = "application/json")
    public List<WebTransactionLineItemDao> getCartItem(String phoneNo)
    {
        return webTransactionManager.getCartItem(phoneNo);
    }
    @RequestMapping(value = "/deleteCartItem", method = RequestMethod.POST)
    public void deleteCartItem(@RequestBody WebTransactionLineItemDao webTransactionDao)
    {
         webTransactionManager.deleteCartItem(webTransactionDao);
    }

//    @RequestMapping(value = "/getCartDetails", method = RequestMethod.GET, produces = "application/json")
//    public WebTransactionDao getTransactionByPhoneNo(String phoneNo)
//    {
//        return webTransactionManager.getTransactionByPhoneNo(phoneNo);
//    }

}
