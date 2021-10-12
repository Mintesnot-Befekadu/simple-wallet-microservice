package com.mintesnotbefekadu.simplewalletmicroservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Account;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Transaction;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Transactions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WalletControllerTest {

    private final long ACCOUNT_ID_IN_DB = 1001;
    private final double INITIAL_BALANCE = 200.0;
    private final long TRANSACTION_ID_IN_DB = 2000;

    @Autowired
    private TestRestTemplate restTemplate;
    private JacksonTester<Transactions> json;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, objectMapper);
    }

    @AfterEach
    void tearDown() {
        // removes data from account and transaction table after each test
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "account", "transaction");
    }

    @Test
    @Sql("/insert_to_account.sql")
    @DisplayName("Successful Get Balance Test")
    void getAccountBalance() {

        double actual = restTemplate.getForObject("/balanceInquiry/{accountId}", Double.class, ACCOUNT_ID_IN_DB);
        assertEquals(INITIAL_BALANCE, actual);
    }

    @Test
    @DisplayName("Get Balance Test when the account is not found")
    void getAccountBalance_accountNotFound() {
        long accountId = 1005;

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity("/balanceInquiry/{accountId}", String.class, accountId);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
        assertTrue(responseEntity.getBody().contains("Account not found"));
    }

    @Test
    @SqlGroup({@Sql("/insert_to_account.sql"), @Sql("/insert_to_transaction.sql")})
    @DisplayName("Successful Get Transaction History Test")
    void getTransactionHistory() throws IOException {
        Transactions transactions =
                restTemplate.getForObject("/transactionHistory/{accountId}",
                        Transactions.class, ACCOUNT_ID_IN_DB);

        assertThat(json.write(transactions)).isEqualToJson("/expected.json");
    }

    @Test
    @DisplayName("Get Balance Test when the account is not found")
    void getTransactionHistory_accountNotFound() {
        long accountId = 1005;

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity("/transactionHistory/" + accountId, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
        assertTrue(responseEntity.getBody().contains("Account not found"));
    }

    @Test
    @DisplayName("Successful save account")
    void saveAccount() {
        Account account = new Account(1L, 0.0);

        ResponseEntity<Long> responseEntity = restTemplate.postForEntity("/account", account, Long.class);
        // Auto Generated Id
        assertEquals(1, responseEntity.getBody());
    }

    @Test
    @Sql("/insert_to_account.sql")
    @DisplayName("Successful CREDIT Transaction")
    void makeTransaction_credit() {
        double amount = 500;
        String type = "CREDIT";

        Transaction transaction = new Transaction(TRANSACTION_ID_IN_DB, amount, type, ACCOUNT_ID_IN_DB);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/transaction", transaction, String.class);

        assertEquals("Transaction Approved", responseEntity.getBody());
        assertEquals(INITIAL_BALANCE + amount,
                restTemplate.getForObject("/balanceInquiry/{accountId}", Double.class, ACCOUNT_ID_IN_DB));
    }

    @Test
    @Sql("/insert_to_account.sql")
    @DisplayName("Successful DEBIT Transaction")
    void makeTransaction_debit() {
        double amount = 100;
        String type = "DEBIT";

        Transaction transaction = new Transaction(TRANSACTION_ID_IN_DB, amount, type, ACCOUNT_ID_IN_DB);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/transaction", transaction, String.class);

        assertEquals("Transaction Approved", responseEntity.getBody());
        assertEquals(INITIAL_BALANCE - amount,
                restTemplate.getForObject("/balanceInquiry/{accountId}", Double.class, ACCOUNT_ID_IN_DB));
    }

    @Test
    @Sql("/insert_to_account.sql")
    @DisplayName("Unsuccessful DEBIT Transaction because of not enough balance for credit")
    void makeTransaction_balanceNotAvailable() {
        double amount = INITIAL_BALANCE + 100;
        String type = "DEBIT";

        Transaction transaction = new Transaction(TRANSACTION_ID_IN_DB, amount, type, ACCOUNT_ID_IN_DB);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/transaction", transaction, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(responseEntity.getBody().contains("Balance Not Available"));
    }

    @Test
    @SqlGroup({@Sql("/insert_to_account.sql"), @Sql("/insert_to_transaction.sql")})
    @DisplayName("Unsuccessful Transaction because of repeated transactionId")
    void makeTransaction_usedTransactionId() {
        double amount = 100;
        String type = "DEBIT";

        Transaction transaction = new Transaction(TRANSACTION_ID_IN_DB, amount, type, ACCOUNT_ID_IN_DB);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/transaction", transaction, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.FORBIDDEN);
        assertTrue(responseEntity.getBody().contains("Transaction Id should be unique"));
    }

    @Test
    @SqlGroup({@Sql("/insert_to_account.sql"), @Sql("/insert_to_transaction.sql")})
    @DisplayName("Unsuccessful Transaction because of incorrect transaction type")
    void makeTransaction_incorrectTransactionType() {
        double amount = 100;
        String type = "TYPE";

        Transaction transaction = new Transaction(TRANSACTION_ID_IN_DB + 1, amount, type, ACCOUNT_ID_IN_DB);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/transaction", transaction, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.FORBIDDEN);
        assertTrue(responseEntity.getBody().contains("Transaction type should be debit or credit"));
    }
}