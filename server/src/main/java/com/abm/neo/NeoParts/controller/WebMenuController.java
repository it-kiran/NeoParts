package com.abm.neo.NeoParts.controller;

import com.abm.neo.NeoParts.dto.MenuDto;
import com.abm.neo.NeoParts.manager.WebMenuManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@CrossOrigin(origins = {"*"})
public class WebMenuController {

    @Autowired
    WebMenuManager webMenuManager;

    @RequestMapping(value = "/getWebMenu", method = RequestMethod.GET)
    public MenuDto getWebMenu() {
        return webMenuManager.getWebMenu();
    }

}
