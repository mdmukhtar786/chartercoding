package com.retailer.rewards.repository;

import com.retailer.rewards.model.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link Transaction} entities.
 *
 * <p>Provides standard CRUD operations and a derived query for fetching
 * all transactions belonging to a specific customer.</p>
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Retrieves all transactions for a given customer, ordered by transaction date ascending.
     *
     * @param customerId the unique identifier of the customer
     * @return ordered list of transactions for that customer
     */
    List<Transaction> findByCustomerIdOrderByTransactionDateAsc(Long customerId);
}
