package com.mintesnotbefekadu.simplewalletmicroservice.exception;

public class TransactionIdNotUniqueException extends RuntimeException {

    public TransactionIdNotUniqueException(String message) {
        super(message);
    }

}
