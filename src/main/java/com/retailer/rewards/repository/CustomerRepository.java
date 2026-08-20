package com.retailer.rewards.repository;

import com.retailer.rewards.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link Customer} entities.
 *
 * <p>Provides standard CRUD operations and query method derivation for
 * customer records. Additional custom queries can be added here as needed.</p>
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
