package com.abm.neo.NeoParts.entity;

import javax.persistence.*;

    @Entity
    @Table(name = "web_transaction_line_item")
    public class WebTransactionLineItemDao {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int transactionLineItemId;

        private String date;
        private String status;
        private String productNo;
//        private int productVariantNo;
        private int saleQuantity;
        private double cost;
        private double retail;
        private double retailWithDiscount;
        private double discount;
        private double totalProductPrice;
        private String imeiNo;
        private String customerPhoneNo;

        @Transient
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

        public int getSaleQuantity() {
            return saleQuantity;
        }

        public void setSaleQuantity(int saleQuantity) {
            this.saleQuantity = saleQuantity;
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

        public double getRetailWithDiscount() {
            return retailWithDiscount;
        }

        public void setRetailWithDiscount(double retailWithDiscount) {
            this.retailWithDiscount = retailWithDiscount;
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

        public String getCustomerPhoneNo() {
            return customerPhoneNo;
        }

        public void setCustomerPhoneNo(String customerPhoneNo) {
            this.customerPhoneNo = customerPhoneNo;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
