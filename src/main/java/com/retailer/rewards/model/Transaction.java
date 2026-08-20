package com.retailer.rewards.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing a single purchase transaction made by a {@link Customer}.
 *
 * <p>Each transaction records the purchase amount and the date it occurred,
 * which are used to calculate the reward points earned by the customer.</p>
 */
@Entity
@Table(name = "transaction")
public class Transaction {

    /** Unique identifier for the transaction. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The customer who made this transaction. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /** The purchase amount for this transaction. Must be a positive value. */
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /** The date on which the transaction occurred. */
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    /** Default no-arg constructor required by JPA. */
    public Transaction() {
    }

    /**
     * Constructs a Transaction with all fields.
     *
     * @param id              unique identifier
     * @param customer        the customer who made the purchase
     * @param amount          the purchase amount
     * @param transactionDate the date of the transaction
     */
    public Transaction(Long id, Customer customer, BigDecimal amount, LocalDate transactionDate) {
        this.id = id;
        this.customer = customer;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    /**
     * Returns the transaction's unique identifier.
     *
     * @return transaction id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the transaction's unique identifier.
     *
     * @param id transaction id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the customer associated with this transaction.
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets the customer associated with this transaction.
     *
     * @param customer the customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Returns the purchase amount of this transaction.
     *
     * @return transaction amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the purchase amount of this transaction.
     *
     * @param amount transaction amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Returns the date the transaction occurred.
     *
     * @return transaction date
     */
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    /**
     * Sets the date the transaction occurred.
     *
     * @param transactionDate transaction date
     */
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
}
