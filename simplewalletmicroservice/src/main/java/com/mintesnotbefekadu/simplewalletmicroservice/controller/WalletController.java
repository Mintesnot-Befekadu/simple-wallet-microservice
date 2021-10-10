package com.mintesnotbefekadu.simplewalletmicroservice.controller;

import com.mintesnotbefekadu.simplewalletmicroservice.model.Account;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Transaction;
import com.mintesnotbefekadu.simplewalletmicroservice.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class provide the API endpoint for balanceInquiry, transaction, and
 * transaction history. One additional API endpoint is providers only for test
 * purposes for this simple wallet micro service.
 *
 * @author mintesnotbefekadu
 */
@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    /**
     * @param accountId players account id
     * @return the account information of the submitted acount id
     */
    @GetMapping("/balanceInquiry/{accountId}")
    public double getAccountBalance(@PathVariable("accountId") Long accountId) {
        return walletService.getAccountBalance(accountId);
    }

    /**
     * Getting transaction history for this specific player account id
     *
     * @param accountId players account id
     * @return list of transaction done by this player account id
     */
    @GetMapping("/transactionHistory/{accountId}")
    public List<Transaction> getTransactionHistory(@PathVariable("accountId") Long accountId) {
        return walletService.getTransactionHistory(accountId);
    }

    /**
     * Transaction post request with
     *
     * @param transaction the post method request contains transaction object which
     *                    contains transaction id, account, amount and transaction
     *                    type
     * @return Transaction result will be returned.
     */
    @PostMapping("/transaction")
    private ResponseEntity<String> makeTransaction(@RequestBody Transaction transaction) {
        walletService.makeTransaction(transaction);
        return new ResponseEntity<>("Transaction Approved", HttpStatus.CREATED);
    }

    /**
     * This method is for test purpose to insert new account to account table
     *
     * @param account this method accept account object which is populated from the
     *                request body
     * @return the account id of the created account
     */
    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    private long saveAccount(@RequestBody Account account) {
        walletService.saveOrUpdateAccount(account);
        return account.getPlayerAccountId();
    }

}
