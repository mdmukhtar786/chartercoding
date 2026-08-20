package com.retailer.rewards.dto;

import java.util.Map;

/**
 * Data Transfer Object representing the rewards summary for a single customer.
 *
 * <p>Contains the customer's identity information, a breakdown of points earned
 * per month (keyed by "YYYY-MM"), and the cumulative total points across all months.</p>
 */
public class RewardsSummaryDto {

    /** Unique identifier of the customer. */
    private Long customerId;

    /** Full name of the customer. */
    private String customerName;

    /** Email address of the customer. */
    private String customerEmail;

    /**
     * Map of reward points earned per calendar month.
     * Key format: "YYYY-MM" (e.g., "2024-01").
     * Value: total points earned in that month.
     */
    private Map<String, Long> monthlyPoints;

    /** Cumulative total reward points across all months. */
    private long totalPoints;

    /** Default no-arg constructor. */
    public RewardsSummaryDto() {
    }

    /**
     * Constructs a RewardsSummaryDto with all fields.
     *
     * @param customerId    unique customer identifier
     * @param customerName  full name of the customer
     * @param customerEmail email address of the customer
     * @param monthlyPoints map of month-to-points breakdown
     * @param totalPoints   total accumulated reward points
     */
    public RewardsSummaryDto(Long customerId, String customerName, String customerEmail,
                             Map<String, Long> monthlyPoints, long totalPoints) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.monthlyPoints = monthlyPoints;
        this.totalPoints = totalPoints;
    }

    /**
     * Returns the customer's unique identifier.
     *
     * @return customer id
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer's unique identifier.
     *
     * @param customerId customer id
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * Returns the customer's full name.
     *
     * @return customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the customer's full name.
     *
     * @param customerName customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Returns the customer's email address.
     *
     * @return customer email
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * Sets the customer's email address.
     *
     * @param customerEmail customer email
     */
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    /**
     * Returns the map of monthly reward points.
     *
     * @return map with "YYYY-MM" keys and point values
     */
    public Map<String, Long> getMonthlyPoints() {
        return monthlyPoints;
    }

    /**
     * Sets the map of monthly reward points.
     *
     * @param monthlyPoints map with "YYYY-MM" keys and point values
     */
    public void setMonthlyPoints(Map<String, Long> monthlyPoints) {
        this.monthlyPoints = monthlyPoints;
    }

    /**
     * Returns the total reward points accumulated across all months.
     *
     * @return total points
     */
    public long getTotalPoints() {
        return totalPoints;
    }

    /**
     * Sets the total reward points accumulated across all months.
     *
     * @param totalPoints total points
     */
    public void setTotalPoints(long totalPoints) {
        this.totalPoints = totalPoints;
    }
}
