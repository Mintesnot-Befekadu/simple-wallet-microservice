package com.mintesnotbefekadu.simplewalletmicroservice.model;

import javax.persistence.*;

/**
 * Account Table definition
 *
 * @author mintesnotbefekadu
 */
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue
    private long playerAccountId;

    @Column(name = "balance")
    private double balance;

    public Account(long playerAccountId, double balance) {
        this.playerAccountId = playerAccountId;
        this.balance = balance;
    }

    public Account() {
    }

    public long getPlayerAccountId() {
        return playerAccountId;
    }

    public void setPlayerAccountId(long playerAccountId) {
        this.playerAccountId = playerAccountId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AccountEntity [playerAccountId=" + playerAccountId + ", balance=" + balance + "]";
    }

}
