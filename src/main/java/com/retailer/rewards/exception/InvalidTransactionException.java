package com.retailer.rewards.exception;

/**
 * Exception thrown when a transaction contains invalid data,
 * such as a null, zero, or negative purchase amount.
 *
 * <p>This results in an HTTP 400 Bad Request response when handled
 * by the global exception handler.</p>
 */
public class InvalidTransactionException extends RuntimeException {

    /**
     * Constructs an InvalidTransactionException with a descriptive message.
     *
     * @param message explanation of what is invalid about the transaction
     */
    public InvalidTransactionException(String message) {
        super(message);
    }
}
