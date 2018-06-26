package com.abm.neo.NeoParts.dto;

/**
 * Created by asp5045 on 11/21/16.
 */


public class ProductEcomerceDto {


    private int productId;
    private String productNo;
    private String description;
    private String categoryId;
    private String brandId;
    private String vendorId;
    private String modelId;
    private double cost;
    private double retail;
    private double tier1;
    private double tier2;
    private double tier3;
    private int quantity;
    private boolean ecommerce;
    private boolean tax;
    private byte[] image;

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

    public boolean isEcommerce() {
        return ecommerce;
    }

    public void setEcommerce(boolean ecommerce) {
        this.ecommerce = ecommerce;
    }

    public boolean isTax() {
        return tax;
    }

    public void setTax(boolean tax) {
        this.tax = tax;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public double getTier1() {
        return tier1;
    }

    public void setTier1(double tier1) {
        this.tier1 = tier1;
    }

    public double getTier2() {
        return tier2;
    }

    public void setTier2(double tier2) {
        this.tier2 = tier2;
    }

    public double getTier3() {
        return tier3;
    }

    public void setTier3(double tier3) {
        this.tier3 = tier3;
    }
}
