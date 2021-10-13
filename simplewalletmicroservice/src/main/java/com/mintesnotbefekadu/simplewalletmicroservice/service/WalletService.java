package com.mintesnotbefekadu.simplewalletmicroservice.service;

import com.mintesnotbefekadu.simplewalletmicroservice.exception.AccountNotFoundException;
import com.mintesnotbefekadu.simplewalletmicroservice.exception.BalanceNotAvailableException;
import com.mintesnotbefekadu.simplewalletmicroservice.exception.TransactionIdNotUniqueException;
import com.mintesnotbefekadu.simplewalletmicroservice.exception.TransactionTypeInCorrectException;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Account;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Transaction;
import com.mintesnotbefekadu.simplewalletmicroservice.model.Transactions;
import com.mintesnotbefekadu.simplewalletmicroservice.repository.AccountRepository;
import com.mintesnotbefekadu.simplewalletmicroservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * All this simple wallet microservice business logic is coded here
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public double getAccountBalance(Long accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found")).getBalance();
    }

    /**
     * Updating the account record for the specific account id with the new balance with Sanity check
     *
     * @param accountId  The account id of the player
     * @param newBalance The updated balance the new balance
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateAccountBalance(Long accountId, Double newBalance) {
        accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"))
                .setBalance(newBalance);
    }

    /**
     * @param account The account details of the player
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
    @Transactional(readOnly = true)
    public Transactions getTransactionHistory(Long accountId) throws AccountNotFoundException {
        if (accountRepository.existsById(accountId)) {
            return new Transactions(transactionRepository.findByAccountId(accountId));
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
    @Transactional(readOnly = true)
    public boolean checkTransactionId(Long transactionId) {
        return transactionRepository.findById(transactionId).isPresent();
    }

    /**
     * Check available balance of the account
     *
     * @param accountId  The account id of the player
     * @param amount The debit transaction amount
     * @return the status of the available balance for this specific account id
     */
    @Transactional(readOnly = true)
    public boolean checkAvailableBalance(Long accountId, Double amount) {
        return getAccountBalance(accountId) < amount;
    }

    /**
     * This method do both debit and credit transaction as per the request data. If
     * the requested data contains debit as transaction type the transaction will
     * debit. And, if the requested data contains credit as transaction type it will
     * be credit transaction.
     *
     * @param transaction transaction object contains transaction id, account,
     *                    amount and type.
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
    public void makeTransaction(Transaction transaction)
            throws BalanceNotAvailableException, TransactionIdNotUniqueException, TransactionTypeInCorrectException {

        if (checkTransactionId(transaction.getTransactionId())) {
            throw new TransactionIdNotUniqueException("Transaction Id should be unique");
        } else if (transaction.getTransactionType().equalsIgnoreCase("debit")) {
            if (checkAvailableBalance(transaction.getAccountId(), transaction.getAmount())) {
                throw new BalanceNotAvailableException("Balance Not Available");
            }
            updateAccountBalance(transaction.getAccountId(),
                    getAccountBalance(transaction.getAccountId()) - transaction.getAmount());
            transactionRepository.save(transaction);
        } else if (transaction.getTransactionType().equalsIgnoreCase("credit")) {
            updateAccountBalance(transaction.getAccountId(),
                    getAccountBalance(transaction.getAccountId()) + transaction.getAmount());
            transactionRepository.save(transaction);
        } else {
            throw new TransactionTypeInCorrectException("Transaction type should be debit or credit");
        }
    }
}