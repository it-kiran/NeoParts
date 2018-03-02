package com.abm.neo.NeoParts.entity;

import javax.persistence.*;
import java.util.List;

@Entity
    @Table(name = "web_transaction")
    public class WebTransactionDao {

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
    private String username;
    private String customerFirstLastName;
    private String note;
    private boolean rmi;

    //this is need to handle store credit scenario
    @Transient
    private int previousTransactionId;

    @Transient
    private double totalReturn;

    @Transient
    private StoreSetupDao storeSetupDao;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_com_id")
    private List<WebTransactionLineItemDao> webTransactionLineItemDaoList;


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

    public double getShipping() {
        return shipping;
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
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

    public boolean isRmi() {
        return rmi;
    }

    public void setRmi(boolean rmi) {
        this.rmi = rmi;
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

    public List<WebTransactionLineItemDao> getWebTransactionLineItemDaoList() {
        return webTransactionLineItemDaoList;
    }

    public void setWebTransactionLineItemDaoList(List<WebTransactionLineItemDao> webTransactionLineItemDaoList) {
        this.webTransactionLineItemDaoList = webTransactionLineItemDaoList;
    }
}
