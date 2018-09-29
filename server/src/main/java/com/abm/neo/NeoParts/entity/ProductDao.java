package com.abm.neo.NeoParts.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by apatel2 on 5/16/17.
 */

@Entity
@Table(name = "product")
public class ProductDao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int productId;

    private String productNo;
    private String description;

    private String categoryId;

    private String brandId;

    private String vendorId;
    private String modelId;
    private String alternetNo;

    private double cost;
    private double retail;
    private int quantity;
    private int minQuantity;

    //this is just to send as 1 when we sell the product default
    @Transient
    private int saleQuantity;

   // private String image;

    private boolean tax;
    private boolean variant;
    private boolean active;
    private boolean ecommerce;
    private boolean relatedProduct;
    private boolean newProduct;
    private boolean onSale;
    private boolean featured;

    private String returnRule;
    private double customLoyaltyAmount;
    private String createdTimestamp;


//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "productNo")
//    private List<ProductInventoryDao> productInventoryDaoList;


    @Transient
    private String transactionComId;
    @Transient
    private String date;
    @Transient
    private String status;
    @Transient
    private double discount;
    @Transient
    private double retailWithDiscount;
    @Transient
    private double totalProductPrice;
    @Transient
    private double taxAmountOnProduct;
    @Transient
    private String imeiNo;


    // This helps to know, that it is add or update operation, need to do it for product inventory problem
    @Transient
    private String operationType;


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getAlternetNo() {
        return alternetNo;
    }

    public void setAlternetNo(String alternetNo) {
        this.alternetNo = alternetNo;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getRetail() {
        return retail;
    }

    public void setRetail(double retail) {
        this.retail = retail;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getSaleQuantity() {
        return saleQuantity;
    }

    public void setSaleQuantity(int saleQuantity) {
        this.saleQuantity = saleQuantity;
    }

    //    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }

    public boolean isTax() {
        return tax;
    }

    public void setTax(boolean tax) {
        this.tax = tax;
    }

    public boolean isVariant() {
        return variant;
    }

    public void setVariant(boolean variant) {
        this.variant = variant;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isEcommerce() {
        return ecommerce;
    }

    public void setEcommerce(boolean ecommerce) {
        this.ecommerce = ecommerce;
    }

    public boolean isRelatedProduct() {
        return relatedProduct;
    }

    public void setRelatedProduct(boolean relatedProduct) {
        this.relatedProduct = relatedProduct;
    }

    public String getReturnRule() {
        return returnRule;
    }

    public void setReturnRule(String returnRule) {
        this.returnRule = returnRule;
    }

    public double getCustomLoyaltyAmount() {
        return customLoyaltyAmount;
    }

    public void setCustomLoyaltyAmount(double customLoyaltyAmount) {
        this.customLoyaltyAmount = customLoyaltyAmount;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

//    public List<ProductInventoryDao> getProductInventoryDaoList() {
//        return productInventoryDaoList;
//    }
//
//    public void setProductInventoryDaoList(List<ProductInventoryDao> productInventoryDaoList) {
//        this.productInventoryDaoList = productInventoryDaoList;
//    }

    public String getTransactionComId() {
        return transactionComId;
    }

    public void setTransactionComId(String transactionComId) {
        this.transactionComId = transactionComId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getRetailWithDiscount() {
        return retailWithDiscount;
    }

    public void setRetailWithDiscount(double retailWithDiscount) {
        this.retailWithDiscount = retailWithDiscount;
    }

    public double getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(double totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    public double getTaxAmountOnProduct() {
        return taxAmountOnProduct;
    }

    public void setTaxAmountOnProduct(double taxAmountOnProduct) {
        this.taxAmountOnProduct = taxAmountOnProduct;
    }

    public String getImeiNo() {
        return imeiNo;
    }

    public void setImeiNo(String imeiNo) {
        this.imeiNo = imeiNo;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public boolean isNewProduct() {
        return newProduct;
    }

    public void setNewProduct(boolean newProduct) {
        this.newProduct = newProduct;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }
}
