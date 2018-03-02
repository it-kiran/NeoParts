package com.abm.neo.NeoParts.controller;

import com.abm.neo.NeoParts.entity.WebTransactionDao;
import com.abm.neo.NeoParts.manager.WebTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@CrossOrigin(origins = {"*"})
public class TransactionController {

    @Autowired
    private WebTransactionManager webTransactionManager;

    @RequestMapping(value = "/addTransaction", method = RequestMethod.POST, consumes = "application/json")
    public WebTransactionDao addTransactionToDB(@RequestBody WebTransactionDao webTransactionDao)
    {
       return webTransactionManager.addTransaction(webTransactionDao);
    }

    @RequestMapping(value = "/getTransactionByPhoneNo", method = RequestMethod.GET, produces = "application/json")
    public WebTransactionDao getTransactionByPhoneNo(String phoneNo)
    {
        return webTransactionManager.getTransactionByPhoneNo(phoneNo);
    }

}
