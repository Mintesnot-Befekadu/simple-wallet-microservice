package com.mintesnotbefekadu.simplewalletmicroservice.service;

import com.mintesnotbefekadu.simplewalletmicroservice.exception.AccountNotFoundException;
import com.mintesnotbefekadu.simplewalletmicroservice.exception.BalanceNotAvailableException;
import com.mintesnotbefekadu.simplewalletmicroservice.exception.TransactionIdNotUniqueException;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Account;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Transaction;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Transactions;
import com.mintesnotbefekadu.simplewalletmicroservice.repository.AccountRepository;
import com.mintesnotbefekadu.simplewalletmicroservice.repository.TransactionRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    @DisplayName("Update account balance unit Test when the account exist")
    void updateAccountBalanceTest() {
        // given
        long accountId = 1003;
        double balance = 100.0;
        double newBalance = 50.0;
        Account account = new Account(accountId, balance);
        //when
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        walletService.updateAccountBalance(accountId,newBalance);
        //then
        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository,times(1)).save(accountArgumentCaptor.capture());
        assertThat(accountArgumentCaptor.getValue()).isEqualTo(account);
        assertThat(accountArgumentCaptor.getValue().getBalance()).isEqualTo(newBalance);
    }

    @Test
    @DisplayName("Update account balance unit Test when the account do not exist")
    void updateAccountBalanceTest_WhenThrowsAccountNotFoundException() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> walletService.updateAccountBalance(1003L, 20.0));
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

    @Test
    @DisplayName("Get transaction history unit Test when the account exist")
    void getTransactionHistoryTest() {
        List<Transaction> transactionList = Arrays.asList(
                new Transaction(2000, 200.0, "debit", 1001L),
                new Transaction(2001, 200.0, "debit", 1001L));
        Transactions transactions = new Transactions(transactionList);

        when(accountRepository.existsById(anyLong())).thenReturn(true);
        when(transactionRepository.findByAccountId(anyLong())).thenReturn(transactionList);

        assertThat(walletService.getTransactionHistory(1001L))
                .usingRecursiveComparison()
                .isEqualTo(transactions);
    }

    @Test
    @DisplayName("check transaction id test, when the transaction id do not exist")
    void checkTransactionIdTest() {
        when(transactionRepository.findById(1002L)).thenReturn(Optional.empty());

        boolean expected = transactionRepository.findById(1002L).isPresent();
        boolean actual = walletService.checkTransactionId(1002L);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("check available account balance unit Test when the account exist")
    void checkAvailableBalanceTest() {
        long accountId = 1003;
        double balance = 100.0;
        double amount = 50.0;
        Account account = new Account(accountId, balance);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        walletService.checkAvailableBalance(accountId, amount);

        assertThat(walletService.checkAvailableBalance(accountId, amount)).isFalse();
    }

    //TODO not completed
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

    @Test
    @DisplayName("make transaction method unit Test when balance not available")
    void makeTransactionTest__WhenThrowsBalanceNotAvailableException() {
        long transactionId = 2000;
        double amount = 1200.0;
        String type = "debit";
        long accountId = 1001;
        double balance = 1000.0;

        Transaction transaction = new Transaction(transactionId, amount, type, accountId);
        Account account = new Account(accountId, balance);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        assertThrows(BalanceNotAvailableException.class, () ->
                walletService.makeTransaction(transaction));
    }

    @Test
    @DisplayName("make transaction method unit Test when transaction id is not unique")
    void makeTransactionTest__WhenThrowsTransactionIdNotUniqueException() {
        long transactionId = 2000;
        double amount = 200.0;
        String type = "debit";
        long accountId = 1001;
        double balance = 1000.0;

        Transaction transaction = new Transaction(transactionId, amount, type, accountId);

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));

        assertThrows(TransactionIdNotUniqueException.class, () ->
                walletService.makeTransaction(transaction));
    }
}