package com.mintesnotbefekadu.simplewalletmicroservice.service;

import com.mintesnotbefekadu.simplewalletmicroservice.model.Account;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Transaction;
import com.mintesnotbefekadu.simplewalletmicroservice.repository.AccountRepository;
import com.mintesnotbefekadu.simplewalletmicroservice.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WalletServiceTest {
    @InjectMocks
    private WalletService walletService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private AutoCloseable closeable;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    void getAccountBalance() {
        long accountId = 1001;
        double balance = 100.0;
        Account account = new Account(balance);
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        assertEquals(walletService.getAccountBalance(accountId), balance);
    }

    @Test
    void updateAccountBalance() {
    }

    @Test
    void saveOrUpdateAccount() {
    }

    @Test
    void getTransactionHistory() {
    }

    @Test
    void checkTransactionId() {
    }

    @Test
    void checkAvailableBalance() {
    }

    // TODO not finished
    @Test
    void makeTransaction() {
        long transactionId = 2000;
        double amount = 200.0;
        String type = "DEBIT";
        long accountId = 1001;
        double balance = 1000.0;

        Transaction transaction = new Transaction(transactionId, amount, type, accountId);

        Mockito.when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        Account account = new Account(balance);
        Mockito.when(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(account));

        walletService.makeTransaction(transaction);
    }
}