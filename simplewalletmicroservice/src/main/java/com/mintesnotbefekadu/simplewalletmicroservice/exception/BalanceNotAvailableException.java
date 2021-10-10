package com.mintesnotbefekadu.simplewalletmicroservice.exception;

public class BalanceNotAvailableException extends RuntimeException {

    public BalanceNotAvailableException(String message) {
        super(message);
    }

}
