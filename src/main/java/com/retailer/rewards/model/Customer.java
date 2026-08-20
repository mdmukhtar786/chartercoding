package com.retailer.rewards.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a retail customer enrolled in the rewards program.
 *
 * <p>A customer can have many associated {@link Transaction} records
 * from which reward points are calculated.</p>
 */
@Entity
@Table(name = "customer")
public class Customer {

    /** Unique identifier for the customer. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Full name of the customer. */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /** Unique email address of the customer. */
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    /** List of all transactions made by this customer. */
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    /** Default no-arg constructor required by JPA. */
    public Customer() {
    }

    /**
     * Constructs a Customer with all fields.
     *
     * @param id           unique identifier
     * @param name         customer full name
     * @param email        customer email address
     * @param transactions list of transactions
     */
    public Customer(Long id, String name, String email, List<Transaction> transactions) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.transactions = transactions;
    }

    /**
     * Returns the customer's unique identifier.
     *
     * @return customer id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the customer's unique identifier.
     *
     * @param id customer id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the customer's full name.
     *
     * @return customer name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the customer's full name.
     *
     * @param name customer name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the customer's email address.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the customer's email address.
     *
     * @param email email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the list of transactions associated with this customer.
     *
     * @return list of transactions
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Sets the list of transactions for this customer.
     *
     * @param transactions list of transactions
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
