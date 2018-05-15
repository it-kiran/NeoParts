package com.abm.neo.NeoParts.controller;

import com.abm.neo.NeoParts.dto.CustomerDao;
import com.abm.neo.NeoParts.manager.CustomerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("*")
@CrossOrigin(origins = {"*"})
public class CustomerController {
    @Autowired
    private CustomerManager customerManager;

//    @Autowired
//    BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/addCustomer", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity addCustomer(@RequestBody CustomerDao customerDao)
    {
        customerManager.addCustomer(customerDao);
        System.out.println("Customer Added or Updated Successfully!!");
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/validateUser", method = RequestMethod.GET)
    public CustomerDao getUserLoginDetails(@RequestParam String username, @RequestParam String password) throws SQLException {

        return customerManager.getUserLoginDetails(username,password);

    }


}
