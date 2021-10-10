package com.mintesnotbefekadu.simplewalletmicroservice.repository;

import com.mintesnotbefekadu.simplewalletmicroservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * A bean to access account table
 *
 * @author mintesnotbefekadu
 */
@Component
public interface AccountRepository extends JpaRepository<Account, Long> {

}
