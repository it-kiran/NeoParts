package com.abm.neo.NeoParts.controller;


import com.abm.neo.NeoParts.dto.ProductEcomerceDto;
import com.abm.neo.NeoParts.entity.ProductDao;
import com.abm.neo.NeoParts.manager.ProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("")
@CrossOrigin(origins = {"*"})
public class ProductController {

    @Autowired
    private ProductManager productManager;

    @RequestMapping(value = "/getAllProduct", method = RequestMethod.GET)
    public List<ProductEcomerceDto> getAllProduct() {

        return productManager.getAllProduct();
    }

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
    public List<ProductEcomerceDto> getEcommerceProductsByModel(@RequestParam int modelId) {

        return productManager.getEcommerceProductsByModel(modelId);
    }
    @RequestMapping(value = "/insertProductImage", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void insertProductImage(@RequestParam String productNo, @RequestParam("file") MultipartFile file) throws IOException {

        System.out.println(file.getSize());
        productManager.insertProductImage(productNo,file);
    }

}
