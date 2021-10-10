package com.mintesnotbefekadu.simplewalletmicroservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Transaction Table definition
 *
 * @author mintesnotbefekadu
 */
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    private long transactionId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "transaction_type")
    private String transactionType;

    // TODO join with account table using account_id as foreign key
    @Column(name = "account_id")
    private long accountId;

    public Transaction(long transactionId, double amount, String transactionType, long accountId) {
        super();
        this.transactionId = transactionId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.accountId = accountId;
    }

    public Transaction() {

    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "Transaction [transactionId=" + transactionId + ", amount=" + amount + ", transactionType="
                + transactionType + ", accountId=" + accountId + "]";
    }

}
