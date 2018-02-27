package com.abm.neo.NeoParts.controller;


import com.abm.neo.NeoParts.dto.ProductEcomerceDto;
import com.abm.neo.NeoParts.entity.ProductDao;
import com.abm.neo.NeoParts.manager.ProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("")
@CrossOrigin(origins = {"*"})
public class ProductController {

    @Autowired
    private ProductManager productManager;

    @RequestMapping(value = "/getAllProductForSearch", method = RequestMethod.GET)
    public List<ProductEcomerceDto> getProduct() {

        return productManager.getProductDetails();
    }

    @RequestMapping(value = "/getProductForSearch", method = RequestMethod.GET)
    public List<ProductEcomerceDto> getProductForSearch(@RequestParam String searchInput) {
        return productManager.getProductForSearch(searchInput);
    }

    @RequestMapping(value = "/getProductsByCategory", method = RequestMethod.GET)
    public List<ProductEcomerceDto> getProductsByCategory(@RequestParam int categoryId) throws SQLException {

        return productManager.getEcommerceProductsByCategory(categoryId);
    }

    @RequestMapping(value = "/getEcommerceProductsByModel", method = RequestMethod.GET)
    public List<ProductEcomerceDto> getEcommerceProductsByBrand(@RequestParam int modelId) {

        return productManager.getEcommerceProductsByBrand(modelId);
    }

}
