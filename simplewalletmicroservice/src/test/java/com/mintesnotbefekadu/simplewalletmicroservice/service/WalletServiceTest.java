package com.mintesnotbefekadu.simplewalletmicroservice.service;

import com.mintesnotbefekadu.simplewalletmicroservice.model.Account;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Transaction;
import com.mintesnotbefekadu.simplewalletmicroservice.repository.AccountRepository;
import com.mintesnotbefekadu.simplewalletmicroservice.repository.TransactionRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {
    @InjectMocks
    private WalletService walletService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    @DisplayName("Get Balance unit Test when the account exist")
    void getAccountBalanceTest() {
        long accountId = 1003;
        double balance = 100.0;
        Account account = new Account(accountId,balance);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        assertEquals(walletService.getAccountBalance(accountId), balance);
    }

    //TODO
    @Test
    @Disabled
    @DisplayName("Update account balance unit Test when the account exist")
    void updateAccountBalanceTest() {
        long accountId = 1003;
        double balance = 100.0;
        Account account = new Account(accountId,balance);
        //doNothing().when(accountRepository.findById(accountId).get().setBalance(balance)).equals(account.getBalance()));
        walletService.updateAccountBalance(accountId,500.0);
        assertEquals(account.getBalance(),500.0);
    }

    //TODO
    @Test
    @Disabled
    @DisplayName("Save account unit Test when the account exist")
    void saveOrUpdateAccountTest() {
    }

    //TODO
    @Test
    @Disabled
    @DisplayName("Get transaction history unit Test when the account exist")
    void getTransactionHistoryTest() {
    }

    //TODO
    @Test
    @Disabled
    @DisplayName("check transaction id test")
    void checkTransactionIdTest() {
    }

    //TODO
    @Test
    @Disabled
    @DisplayName("check available account balance unit Test when the account exist")
    void checkAvailableBalanceTest() {
    }

    //TODO not finished
    @Test
    @Disabled
    @DisplayName("make transaction method unit Test")
    void makeTransactionTest() {
        long transactionId = 2000;
        double amount = 200.0;
        String type = "debit";
        long accountId = 1001;
        double balance = 1000.0;

        Transaction transaction = new Transaction(transactionId, amount, type, accountId);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        Account account = new Account(accountId,balance);
        when(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(account));

        walletService.makeTransaction(transaction);
    }
}