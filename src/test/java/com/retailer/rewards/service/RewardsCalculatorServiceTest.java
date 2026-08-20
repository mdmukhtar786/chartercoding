package com.retailer.rewards.service;

import com.retailer.rewards.exception.InvalidTransactionException;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link RewardsCalculatorService}.
 *
 * <p>Covers normal boundary cases, edge cases, and negative/invalid scenarios
 * to ensure the points calculation formula is always correct.</p>
 */
class RewardsCalculatorServiceTest {

    private RewardsCalculatorService calculator;

    /**
     * Initialises a fresh instance of the calculator before each test.
     */
    @BeforeEach
    void setUp() {
        calculator = new RewardsCalculatorService();
    }

    // -------------------------------------------------------------------------
    // Happy path — boundary values
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("$0 purchase earns 0 points")
    void zeroDollars_earnsZeroPoints() {
        assertEquals(0, calculator.calculatePoints(new BigDecimal("0")));
    }

    @Test
    @DisplayName("$30 purchase (below $50) earns 0 points")
    void belowLowerThreshold_earnsZeroPoints() {
        assertEquals(0, calculator.calculatePoints(new BigDecimal("30")));
    }

    @Test
    @DisplayName("Exactly $50 earns 0 points (boundary: threshold is exclusive)")
    void exactlyFiftyDollars_earnsZeroPoints() {
        assertEquals(0, calculator.calculatePoints(new BigDecimal("50")));
    }

    @Test
    @DisplayName("$51 earns 1 point (just above lower threshold)")
    void justAboveLowerThreshold_earnsOnePoint() {
        assertEquals(1, calculator.calculatePoints(new BigDecimal("51")));
    }

    @Test
    @DisplayName("$75 purchase earns 25 points")
    void seventyFiveDollars_earns25Points() {
        // $75 - $50 = $25 in the 1-pt tier => 25 points
        assertEquals(25, calculator.calculatePoints(new BigDecimal("75")));
    }

    @Test
    @DisplayName("Exactly $100 earns 50 points")
    void exactlyHundredDollars_earns50Points() {
        // $100 - $50 = $50 in the 1-pt tier => 50 points
        assertEquals(50, calculator.calculatePoints(new BigDecimal("100")));
    }

    @Test
    @DisplayName("$101 earns 52 points (1 dollar into the 2-pt tier)")
    void justAboveUpperThreshold_earns52Points() {
        // 1 * 2 (above $100) + 50 * 1 (between $50-$100) = 52
        assertEquals(52, calculator.calculatePoints(new BigDecimal("101")));
    }

    @Test
    @DisplayName("$120 purchase earns 90 points (example from requirements)")
    void oneHundredTwentyDollars_earns90Points() {
        // 2*20 + 1*50 = 90
        assertEquals(90, calculator.calculatePoints(new BigDecimal("120")));
    }

    @Test
    @DisplayName("$200 purchase earns 250 points")
    void twoHundredDollars_earns250Points() {
        // 2*100 + 1*50 = 250
        assertEquals(250, calculator.calculatePoints(new BigDecimal("200")));
    }

    @Test
    @DisplayName("$300 purchase earns 450 points")
    void threeHundredDollars_earns450Points() {
        // 2*200 + 1*50 = 450
        assertEquals(450, calculator.calculatePoints(new BigDecimal("300")));
    }

    @Test
    @DisplayName("Decimal amount $120.99 — only whole dollars count")
    void decimalAmount_usesOnlyWholeDollars() {
        // longValue() of 120.99 = 120 => same as $120 => 90 points
        assertEquals(90, calculator.calculatePoints(new BigDecimal("120.99")));
    }

    // -------------------------------------------------------------------------
    // Negative / exception scenarios
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Null amount throws InvalidTransactionException")
    void nullAmount_throwsInvalidTransactionException() {
        assertThrows(InvalidTransactionException.class,
                () -> calculator.calculatePoints(null));
    }

    @Test
    @DisplayName("Negative amount throws InvalidTransactionException")
    void negativeAmount_throwsInvalidTransactionException() {
        assertThrows(InvalidTransactionException.class,
                () -> calculator.calculatePoints(new BigDecimal("-10")));
    }

    @Test
    @DisplayName("Negative amount exception message contains the offending value")
    void negativeAmount_exceptionMessageContainsAmount() {
        InvalidTransactionException ex = assertThrows(InvalidTransactionException.class,
                () -> calculator.calculatePoints(new BigDecimal("-50")));
        assertEquals(true, ex.getMessage().contains("-50"));
    }
}
