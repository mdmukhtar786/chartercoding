package com.retailer.rewards.service;

import com.retailer.rewards.dto.RewardsSummaryDto;
import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer responsible for orchestrating the retrieval of customer and
 * transaction data and computing the rewards summary for one or all customers.
 *
 * <p>Months are derived dynamically from the transaction dates stored in the
 * database — no months are hardcoded anywhere in this class.</p>
 */
@Service
public class RewardsService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final RewardsCalculatorService calculatorService;

    /**
     * Constructs the RewardsService with its required dependencies.
     *
     * @param customerRepository    repository for {@link Customer} data access
     * @param transactionRepository repository for {@link Transaction} data access
     * @param calculatorService     service containing the points calculation logic
     */
    public RewardsService(CustomerRepository customerRepository,
                          TransactionRepository transactionRepository,
                          RewardsCalculatorService calculatorService) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
        this.calculatorService = calculatorService;
    }

    /**
     * Calculates the rewards summary for every customer in the system.
     *
     * <p>Each summary contains a per-month breakdown and a cumulative total,
     * derived from all transactions present in the database.</p>
     *
     * @return list of {@link RewardsSummaryDto} for all customers
     */
    @Transactional(readOnly = true)
    public List<RewardsSummaryDto> getAllCustomerRewards() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> buildSummary(customer,
                        transactionRepository.findByCustomerIdOrderByTransactionDateAsc(customer.getId())))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the rewards summary for a single customer identified by their id.
     *
     * @param customerId the unique identifier of the customer
     * @return {@link RewardsSummaryDto} containing monthly and total points for that customer
     * @throws CustomerNotFoundException if no customer exists with the given id
     */
    @Transactional(readOnly = true)
    public RewardsSummaryDto getCustomerRewards(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        List<Transaction> transactions =
                transactionRepository.findByCustomerIdOrderByTransactionDateAsc(customerId);

        return buildSummary(customer, transactions);
    }

    /**
     * Builds a {@link RewardsSummaryDto} for a customer from their transaction list.
     *
     * <p>Months are grouped from the transaction dates themselves using a
     * "YYYY-MM" string key, ensuring no months are hardcoded.</p>
     *
     * @param customer     the customer entity
     * @param transactions list of that customer's transactions
     * @return a fully populated {@link RewardsSummaryDto}
     */
    private RewardsSummaryDto buildSummary(Customer customer, List<Transaction> transactions) {
        // Group and sum points by "YYYY-MM" — months derived entirely from data
        Map<String, Long> monthlyPoints = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getTransactionDate().getYear() + "-"
                                + String.format("%02d", t.getTransactionDate().getMonthValue()),
                        TreeMap::new,
                        Collectors.summingLong(t -> calculatorService.calculatePoints(t.getAmount()))
                ));

        long totalPoints = monthlyPoints.values().stream().mapToLong(Long::longValue).sum();

        // Use LinkedHashMap to preserve the sorted order in the response
        return new RewardsSummaryDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                new LinkedHashMap<>(monthlyPoints),
                totalPoints
        );
    }
}
