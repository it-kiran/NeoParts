package com.abm.pos.ABMPos.dao;

import javax.persistence.*;

/**
 * Created by apatel2 on 9/28/17.
 */

@Entity
@Table(name = "transaction_payment")
public class PaymentDao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int transactionPaymentId;

    private int transactionComId;
    private String status;
    private String date;
    private double amount;
    private String type;
    private String note;
    private String username;
    private String updatedTimestamp;

    public int getTransactionPaymentId() {
        return transactionPaymentId;
    }

    public void setTransactionPaymentId(int transactionPaymentId) {
        this.transactionPaymentId = transactionPaymentId;
    }

    public int getTransactionComId() {
        return transactionComId;
    }

    public void setTransactionComId(int transactionComId) {
        this.transactionComId = transactionComId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(String updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }
}
