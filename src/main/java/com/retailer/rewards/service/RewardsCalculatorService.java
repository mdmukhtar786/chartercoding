package com.retailer.rewards.service;

import com.retailer.rewards.exception.InvalidTransactionException;
import java.math.BigDecimal;

/**
 * Stateless service responsible for the core reward points calculation logic.
 *
 * <p>Reward rules:
 * <ul>
 *   <li>2 points for every whole dollar spent <strong>over $100</strong></li>
 *   <li>1 point for every whole dollar spent <strong>between $50 and $100</strong> (inclusive of $50, exclusive of lower tier)</li>
 *   <li>No points for amounts at or below $50</li>
 * </ul>
 * Example: a $120 purchase = (2 × $20) + (1 × $50) = 90 points.
 * </p>
 */
public class RewardsCalculatorService {

    /** The lower threshold below which no points are awarded. */
    private static final BigDecimal LOWER_THRESHOLD = new BigDecimal("50");

    /** The upper threshold above which the double-point tier activates. */
    private static final BigDecimal UPPER_THRESHOLD = new BigDecimal("100");

    /**
     * Calculates the reward points earned for a single transaction amount.
     *
     * <p>The calculation uses only the whole-dollar portion of the amount.</p>
     *
     * @param amount the purchase amount; must be non-null and non-negative
     * @return the number of reward points earned for this amount
     * @throws InvalidTransactionException if the amount is null or negative
     */
    public long calculatePoints(BigDecimal amount) {
        if (amount == null) {
            throw new InvalidTransactionException("Transaction amount must not be null.");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionException(
                    "Transaction amount must not be negative, but was: " + amount);
        }

        long points = 0;

        if (amount.compareTo(UPPER_THRESHOLD) > 0) {
            // Points for the portion above $100
            long aboveHundred = amount.longValue() - UPPER_THRESHOLD.longValue();
            points += aboveHundred * 2;
            // Points for the $50-$100 band (full 50 dollars)
            points += UPPER_THRESHOLD.longValue() - LOWER_THRESHOLD.longValue();
        } else if (amount.compareTo(LOWER_THRESHOLD) > 0) {
            // Points only for the $50-$100 band
            long aboveFifty = amount.longValue() - LOWER_THRESHOLD.longValue();
            points += aboveFifty;
        }

        return points;
    }
}
