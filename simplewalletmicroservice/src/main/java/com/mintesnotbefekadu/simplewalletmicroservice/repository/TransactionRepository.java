package com.mintesnotbefekadu.simplewalletmicroservice.repository;

import com.mintesnotbefekadu.simplewalletmicroservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A bean to access transaciton table
 *
 * @author mintesnotbefekadu
 */
@Component
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * A method to access transaction table with accountid instead of transaaction
     * id. Please note transaction id is the id of the table. That is why this
     * method needed.
     *
     * @param accountId The player account id
     * @return List of transaction done by the specified account id
     */
    @Query("select u from Transaction u where u.accountId = ?1")
    List<Transaction> findByAccountId(Long accountId);

}
