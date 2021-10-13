package com.mintesnotbefekadu.simplewalletmicroservice.service;

import com.mintesnotbefekadu.simplewalletmicroservice.exception.AccountNotFoundException;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Account;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Transaction;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Transactions;
import com.mintesnotbefekadu.simplewalletmicroservice.repository.AccountRepository;
import com.mintesnotbefekadu.simplewalletmicroservice.repository.TransactionRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
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
        Account account = new Account(accountId, balance);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        assertEquals(walletService.getAccountBalance(accountId), balance);
    }

    //TODO
    @Test
    @Disabled
    @DisplayName("Update account balance unit Test when the account exist")
    void updateAccountBalanceTest() {
        long accountId = 1003;
        double newBalance = 100.0;
        Account account = new Account(accountId, newBalance);

        walletService.updateAccountBalance(accountId, newBalance);

        ArgumentCaptor<Long> accountIdCapture = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Double> newBalanceCapture = ArgumentCaptor.forClass(Double.class);
        verify(accountRepository)
                .findById(accountIdCapture.capture())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"))
                .setBalance(newBalanceCapture.capture());

        assertEquals(accountId, accountIdCapture.getValue());
        assertEquals(newBalance, newBalanceCapture.getValue());

    }

    @Test
    @DisplayName("Save account unit Test when the account exist")
    void saveOrUpdateAccountTest() {
        // given
        long accountId = 1003;
        double balance = 100.0;
        Account account = new Account(accountId, balance);
        //when
        walletService.saveOrUpdateAccount(account);
        //then
        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());
        assertThat(accountArgumentCaptor.getValue()).isEqualTo(account);
    }

    //TODO
    @Test
    @Disabled
    @DisplayName("Get transaction history unit Test when the account exist")
    void getTransactionHistoryTest() {
        Transaction transaction1 = new Transaction(
                2000, 200.0, "debit", 1001L);
        Transaction transaction2 = new Transaction(
                2001, 200.0, "debit", 1001L);


        List<Transaction> transactionList = Arrays.asList(transaction1, transaction2);
        Transactions transactions = new Transactions(transactionList);

        when(transactionRepository.findByAccountId(anyLong())).thenReturn(transactionList);
        assertEquals(walletService.getTransactionHistory(1001L), transactions);
    }

    @Test
    @DisplayName("check transaction id test, when the transaction id do not exist")
    void checkTransactionIdTest() {
        boolean expected = transactionRepository.findById(1002L).isPresent();
        boolean actual = walletService.checkTransactionId(1002L);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("check available account balance unit Test when the account exist")
    void checkAvailableBalanceTest() {
        long accountId = 1003;
        double balance = 100.0;
        double amount = 150.0;
        Account account = new Account(accountId, balance);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        boolean expected = walletService.getAccountBalance(accountId) < amount;
        boolean actual = walletService.checkAvailableBalance(accountId,amount);

        assertThat(actual).isEqualTo(expected);
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

        Account account = new Account(accountId, balance);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        walletService.makeTransaction(transaction);
    }
}