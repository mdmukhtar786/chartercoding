package com.retailer.rewards.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a standardised error response body.
 *
 * <p>Returned by the global exception handler whenever an API error occurs,
 * providing callers with the HTTP status code, a human-readable message,
 * and a timestamp for when the error was generated.</p>
 */
public class ErrorResponseDto {

    /** HTTP status code of the error. */
    private int status;

    /** Human-readable error message. */
    private String message;

    /** Timestamp at which the error occurred. */
    private LocalDateTime timestamp;

    /** Default no-arg constructor. */
    public ErrorResponseDto() {
    }

    /**
     * Constructs an ErrorResponseDto with all fields.
     *
     * @param status    HTTP status code
     * @param message   error description
     * @param timestamp time the error occurred
     */
    public ErrorResponseDto(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * Returns the HTTP status code.
     *
     * @return status code
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the HTTP status code.
     *
     * @param status status code
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Returns the error message.
     *
     * @return error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the error message.
     *
     * @param message error message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the timestamp when the error occurred.
     *
     * @return error timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp when the error occurred.
     *
     * @param timestamp error timestamp
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
