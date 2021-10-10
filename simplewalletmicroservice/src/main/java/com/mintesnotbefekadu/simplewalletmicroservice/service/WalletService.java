package com.mintesnotbefekadu.simplewalletmicroservice.service;

import com.mintesnotbefekadu.simplewalletmicroservice.exception.*;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Account;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Transaction;
import com.mintesnotbefekadu.simplewalletmicroservice.repository.AccountRepository;
import com.mintesnotbefekadu.simplewalletmicroservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * All this simple wallet microserive business logic is coded here
 *
 * @author mintesnotbefekadu
 */
@Service
public class WalletService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * @param accountId The account id of the player
     * @return Balance of the player
     * @throws AccountNotFoundException if the account is not found
     */
    public double getAccountBalance(Long accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found")).getBalance();
    }

    /**
     * @param accountId The account id of the player
     * @param newBalance The updated balance the new balance
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateAccountBalance(Long accountId, Double newBalance) {
        accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"))
                .setBalance(newBalance);
    }

    /**
     * @param account The account id of the player
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveOrUpdateAccount(Account account) {
        accountRepository.save(account);
    }

    /**
     * Getting transaction history
     *
     * @param accountId The account id of the player
     * @return transaction object which in turn will be response body
     */
    public List<Transaction> getTransactionHistory(Long accountId) throws AccountNotFoundException {
        if (accountRepository.existsById(accountId)) {
            return transactionRepository.findByAccountId(accountId);
        } else {
            throw new AccountNotFoundException("Account not found");
        }
    }

    /**
     * Check transaction id
     *
     * @param transactionId Unique identifier of the transaction
     * @return the status of the transaction with this specific transaction id
     */
    private boolean checkTransactionId(Long transactionId) {
        return transactionRepository.findById(transactionId).isPresent();
    }

    /**
     * Check available balance of the account
     *
     * @param accountId The account id of the player
     * @param newBalance The updated balance the new balance
     * @return the status of the available balance for this specific account id
     */
    private boolean checkAvailableBalance(Long accountId, Double newBalance) {
        return getAccountBalance(accountId) < newBalance;
    }

    /**
     * This method do both debit and credit transaction as per the request data. If
     * the requested data contains debit as transaction type the transaction will be
     * debit. And, if the requested data contains credit as transaction type it will
     * be credit transaction.
     *
     * @param transaction transaction object contains transaction id, account,
     *                    amount and type.
     * @return The transaction if the all validation are approved.
     * @throws BalanceNotAvailableException      This balance not available
     *                                           exception will be thrown, if the
     *                                           account balance is less that the
     *                                           request balance in debit
     *                                           transaction
     * @throws TransactionIdNotUniqueException   This transaction id not unique
     *                                           exception will be thrown, if the
     *                                           transaction id already exists in
     *                                           the transaction table.
     * @throws TransactionTypeInCorrectException This transaction type in correct
     *                                           exception will be thrown, if the
     *                                           transaction type is not debit or
     *                                           credit.
     */
    public Transaction makeTransaction(Transaction transaction)
            throws BalanceNotAvailableException, TransactionIdNotUniqueException, TransactionTypeInCorrectException {

        if (checkTransactionId(transaction.getTransactionId())) {
            throw new TransactionIdNotUniqueException("Transaction Id should be unique");
        }

        if (transaction.getTransactionType().equalsIgnoreCase("debit")) {
            if (checkAvailableBalance(transaction.getAccountId(), transaction.getAmount())) {
                throw new BalanceNotAvailableException("Balance Not Available");
            }
            updateAccountBalance(transaction.getAccountId(),
                    getAccountBalance(transaction.getAccountId()) - transaction.getAmount());
            return transactionRepository.save(transaction);
        } else if (transaction.getTransactionType().equalsIgnoreCase("credit")) {
            updateAccountBalance(transaction.getAccountId(),
                    getAccountBalance(transaction.getAccountId()) + transaction.getAmount());
            return transactionRepository.save(transaction);
        } else {
            throw new TransactionTypeInCorrectException("Transaction type should be debit or credit");
        }

    }

}
