package com.abm.pos.ABMPos.dao;

import javax.persistence.*;

/**
 * Created by apatel2 on 5/18/17.
 */

@Entity
@Table(name = "transaction_line_item")
public class TransactionLineItemDao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int transactionLineItemId;

    private String date;
    private String status;
    private int productId;
    private String productNo;
    private int productVariantNo;
    private int saleQuantity;
    private double cost;
    private double retail;
    private double retailWithDiscount;
    private double discount;
    private double totalProductPrice;
    private String imeiNo;
    private String description;


    public int getTransactionLineItemId() {
        return transactionLineItemId;
    }

    public void setTransactionLineItemId(int transactionLineItemId) {
        this.transactionLineItemId = transactionLineItemId;
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

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public int getProductVariantNo() {
        return productVariantNo;
    }

    public void setProductVariantNo(int productVariantNo) {
        this.productVariantNo = productVariantNo;
    }

    public int getSaleQuantity() {
        return saleQuantity;
    }

    public void setSaleQuantity(int saleQuantity) {
        this.saleQuantity = saleQuantity;
    }

    public double getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(double totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }


    public String getImeiNo() {
        return imeiNo;
    }

    public void setImeiNo(String imeiNo) {
        this.imeiNo = imeiNo;
    }

    public double getRetailWithDiscount() {
        return retailWithDiscount;
    }

    public void setRetailWithDiscount(double retailWithDiscount) {
        this.retailWithDiscount = retailWithDiscount;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
