package com.retailer.rewards.exception;

/**
 * Exception thrown when a requested customer does not exist in the system.
 *
 * <p>This is a runtime exception that results in an HTTP 404 Not Found
 * response when handled by the global exception handler.</p>
 */
public class CustomerNotFoundException extends RuntimeException {

    /**
     * Constructs a CustomerNotFoundException with the given customer id.
     *
     * @param customerId the id of the customer that was not found
     */
    public CustomerNotFoundException(Long customerId) {
        super("Customer not found with id: " + customerId);
    }

    /**
     * Constructs a CustomerNotFoundException with a custom message.
     *
     * @param message descriptive error message
     */
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
