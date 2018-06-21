package com.abm.neo.NeoParts.entity;

import javax.persistence.*;

/**
 * Created by apatel2 on 9/26/17.
 */

@Entity
@Table(name = "store_setup")
public class StoreSetupDao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private double tax;
    private String street;
    private String city;
    private String state;
    private String zipcode;
    private String email;
    private String phoneNo;
    private byte[] logo;
    private String receiptFooter;
    private Integer receiptType;
    private boolean loyaltyEnabled;
    private double loyaltyAmountForDollar;
    private String emailPassword;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getReceiptFooter() {
        return receiptFooter;
    }

    public void setReceiptFooter(String receiptFooter) {
        this.receiptFooter = receiptFooter;
    }

    public Integer getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(Integer receiptType) {
        this.receiptType = receiptType;
    }

    public boolean isLoyaltyEnabled() {
        return loyaltyEnabled;
    }

    public void setLoyaltyEnabled(boolean loyaltyEnabled) {
        this.loyaltyEnabled = loyaltyEnabled;
    }

    public double getLoyaltyAmountForDollar() {
        return loyaltyAmountForDollar;
    }

    public void setLoyaltyAmountForDollar(double loyaltyAmountForDollar) {
        this.loyaltyAmountForDollar = loyaltyAmountForDollar;

    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }
}
