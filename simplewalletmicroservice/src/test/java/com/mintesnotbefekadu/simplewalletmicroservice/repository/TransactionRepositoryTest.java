package com.mintesnotbefekadu.simplewalletmicroservice.repository;

import com.mintesnotbefekadu.simplewalletmicroservice.model.Transaction;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Transactions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
    }

    @Test
    @DisplayName("Happy path check for custom repository method find by account id")
    void byExistingAccountFindByAccountId_returnTransactions() {
        //given
        Transaction transaction = new Transaction(
                2000, 200.0, "debit", 1001L);
        Transaction transaction2 = new Transaction(
                2001, 200.0, "debit", 1001L);
        transactionRepository.save(transaction);
        transactionRepository.save(transaction2);
        //when
        Transactions transactionListActual =
                new Transactions(transactionRepository.findByAccountId(1001L));
        Transactions transactionListExpected = new Transactions();
        transactionListExpected.setTransactions(Arrays.asList(transaction, transaction2));
        //then
        assertThat(transactionListActual).usingRecursiveComparison().
                isEqualTo(transactionListExpected);
    }

    @Test
    @DisplayName("Negative test for custom repository method find by account id")
    void notExistingAccountFindByAccountId_doesNotReturnTransactions() {
        //given
        Transaction transaction = new Transaction(
                2000, 200.0, "debit", 1001L);
        Transaction transaction2 = new Transaction(
                2001, 200.0, "debit", 1001L);
        transactionRepository.save(transaction);
        transactionRepository.save(transaction2);
        //when
        Transactions transactionListActual =
                new Transactions(transactionRepository.findByAccountId(1002L));
        Transactions transactionListExpected = new Transactions();
        transactionListExpected.setTransactions(Arrays.asList(transaction, transaction2));
        //then
        assertThat(transactionListActual).usingRecursiveComparison()
                .isNotEqualTo(transactionListExpected);
    }
}