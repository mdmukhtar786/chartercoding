package com.retailer.rewards.exception;

import com.retailer.rewards.dto.ErrorResponseDto;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Global exception handler that intercepts exceptions thrown by any controller
 * and converts them into a consistent {@link ErrorResponseDto} JSON response.
 *
 * <p>Handles application-level exceptions as well as common Spring MVC binding errors.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link CustomerNotFoundException} by returning HTTP 404.
     *
     * @param ex the exception carrying the not-found message
     * @return a 404 response with error details
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomerNotFound(CustomerNotFoundException ex) {
        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles {@link InvalidTransactionException} by returning HTTP 400.
     *
     * @param ex the exception carrying the validation message
     * @return a 400 response with error details
     */
    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidTransaction(InvalidTransactionException ex) {
        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles {@link MethodArgumentTypeMismatchException} caused by invalid
     * path variable types (e.g., passing a string where a Long is expected).
     *
     * @param ex the type mismatch exception
     * @return a 400 response with a descriptive error message
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(), ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                message,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Fallback handler for any unhandled exception, returning HTTP 500.
     *
     * @param ex the unexpected exception
     * @return a 500 response with a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred: " + ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
