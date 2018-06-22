package com.abm.pos.ABMPos.dao;

import com.abm.pos.ABMPos.dto.PaymentDetails;

import javax.persistence.*;
import java.util.List;

/**
 * Created by apatel2 on 5/18/17.
 */

@Entity
@Table(name = "transaction")
public class TransactionDao {

    @Id
    @Column(name = "transaction_com_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int transactionComId;

    private String date;
    private double totalAmount;
    private double tax;
    private double totalDiscount;
    private double  subtotal;
    private double shipping;
    private int quantity;
    private String customerPhoneno;
    private String status;
    private double previousBalance;
    private double transactionBalance;
    private double totalBalanceDue;
    private String username;
    private String customerFirstLastName;
    private String note;
    private boolean rma;
    private boolean parkSale;

    //this is need to handle store credit scenario
    @Transient
    private int previousTransactionId;

    @Transient
    private double totalReturn;

    @Transient
    private StoreSetupDao storeSetupDao;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_com_id")
    private List<TransactionLineItemDao> transactionLineItemDaoList;

    @Transient
    private List<PaymentDao> paymentDao;

//    @Transient
//    private List<PaymentDetails> paymentDetails;

    // TODO Need to figure this out what this guys is doing. cause i already have almost same name on top.
    @Transient
    private Double totalDueBalance;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "transaction_com_id")
//    private List<PaymentDao> paymentDao;



    public int getTransactionComId() {
        return transactionComId;
    }

    public void setTransactionComId(int transactionComId) {
        this.transactionComId = transactionComId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCustomerPhoneno() {
        return customerPhoneno;
    }

    public void setCustomerPhoneno(String customerPhoneno) {
        this.customerPhoneno = customerPhoneno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(double previousBalance) {
        this.previousBalance = previousBalance;
    }

    public double getTransactionBalance() {
        return transactionBalance;
    }

    public void setTransactionBalance(double transactionBalance) {
        this.transactionBalance = transactionBalance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCustomerFirstLastName() {
        return customerFirstLastName;
    }

    public void setCustomerFirstLastName(String customerFirstLastName) {
        this.customerFirstLastName = customerFirstLastName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<TransactionLineItemDao> getTransactionLineItemDaoList() {
        return transactionLineItemDaoList;
    }

    public void setTransactionLineItemDaoList(List<TransactionLineItemDao> transactionLineItemDaoList) {


        for(TransactionLineItemDao t: transactionLineItemDaoList)
        {
           //t.setTransactionLineItemId(0);
        }
        this.transactionLineItemDaoList = transactionLineItemDaoList;
    }


    public List<PaymentDao> getPaymentDao() {
        return paymentDao;
    }

    public void setPaymentDao(List<PaymentDao> paymentDao) {
        this.paymentDao = paymentDao;
    }

    public int getPreviousTransactionId() {
        return previousTransactionId;
    }

    public void setPreviousTransactionId(int previousTransactionId) {
        this.previousTransactionId = previousTransactionId;
    }

    public double getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(double totalReturn) {
        this.totalReturn = totalReturn;
    }

    public StoreSetupDao getStoreSetupDao() {
        return storeSetupDao;
    }

    public void setStoreSetupDao(StoreSetupDao storeSetupDao) {
        this.storeSetupDao = storeSetupDao;
    }

    public double getShipping() {
        return shipping;
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    public boolean isRma() {
        return rma;
    }

    public void setRma(boolean rma) {
        this.rma = rma;
    }

    public boolean isParkSale() {
        return parkSale;
    }

    public void setParkSale(boolean parkSale) {
        this.parkSale = parkSale;
    }

//    public List<PaymentDetails> getPaymentDetails() {
//        return paymentDetails;
//    }
//
//    public void setPaymentDetails(List<PaymentDetails> paymentDetails) {
//        this.paymentDetails = paymentDetails;
//    }

    public Double getTotalDueBalance() {
        return totalDueBalance;
    }

    public void setTotalDueBalance(Double totalDueBalance) {
        this.totalDueBalance = totalDueBalance;
    }

    public double getTotalBalanceDue() {
        return totalBalanceDue;
    }

    public void setTotalBalanceDue(double totalBalanceDue) {
        this.totalBalanceDue = totalBalanceDue;
    }
}
