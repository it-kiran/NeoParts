package com.abm.neo.NeoParts.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by apatel2 on 5/16/17.
 */

@Entity
@Table(name = "category")
public class CategoryDao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int categoryId;
    private String name;
    private String description;

    @Transient
    private int noOfProducts;

//    @OneToMany
//    @JoinColumn(name="categoryId")
//    private Set<ProductDao> productDaoSet;
//
//    public Set<ProductDao> getProductDaoSet() {
//        return productDaoSet;
//    }


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNoOfProducts() {
        return noOfProducts;
    }

    public void setNoOfProducts(int noOfProducts) {
        this.noOfProducts = noOfProducts;
    }
}
