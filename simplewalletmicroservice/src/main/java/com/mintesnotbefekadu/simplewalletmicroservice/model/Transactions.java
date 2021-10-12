package com.mintesnotbefekadu.simplewalletmicroservice.model;

import java.util.List;

public class Transactions {

    List<Transaction> transactions;

    public Transactions() {

    }

    public Transactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
