package com.mintesnotbefekadu.simplewalletmicroservice.controller;

import com.mintesnotbefekadu.simplewalletmicroservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 * This class will control exception responses and can be uses as the whole
 * microservice exception response handler The class Exception controller
 * extends response entity exception handler
 *
 * @author mintesnotbefekadu
 */

@ControllerAdvice
@RestController
public class ExceptionController extends ResponseEntityExceptionHandler {

    /**
     * Handle all exception
     *
     * @param ex      the ex
     * @param request the request
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        WalletExceptionResponse exceptionResponse = new WalletExceptionResponse(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<Object>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle account not found exception
     *
     * @param ex      the ex
     * @param request the request
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public final ResponseEntity<Object> handleAccountNotFoundException(Exception ex, WebRequest request) {
        WalletExceptionResponse exceptionResponse = new WalletExceptionResponse(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<Object>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle transaction identifier not unique exception
     *
     * @param ex      the ex
     * @param request the request
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(TransactionIdNotUniqueException.class)
    public final ResponseEntity<Object> handleTransactionIdNotUniqueException(Exception ex, WebRequest request) {
        WalletExceptionResponse exceptionResponse = new WalletExceptionResponse(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<Object>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Handle transaction type in correct exception
     *
     * @param ex      the ex
     * @param request the request
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(TransactionTypeInCorrectException.class)
    public final ResponseEntity<Object> handleTransactionTypeInCorrectException(Exception ex, WebRequest request) {
        WalletExceptionResponse exceptionResponse = new WalletExceptionResponse(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<Object>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Handle balance not available exception
     *
     * @param ex      the ex
     * @param request the request
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(BalanceNotAvailableException.class)
    public final ResponseEntity<Object> handleBalanceNotAvailableException(Exception ex, WebRequest request) {
        WalletExceptionResponse exceptionResponse = new WalletExceptionResponse(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
