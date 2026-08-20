package com.retailer.rewards.service;

import com.retailer.rewards.dto.RewardsSummaryDto;
import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link RewardsService}.
 *
 * <p>Uses Mockito to isolate the service from the database, verifying
 * that monthly grouping, point totalling, and exception propagation
 * all behave correctly across multiple customer and transaction scenarios.</p>
 */
@ExtendWith(MockitoExtension.class)
class RewardsServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Spy
    private RewardsCalculatorService calculatorService;

    @InjectMocks
    private RewardsService rewardsService;

    private Customer alice;
    private Customer bob;

    /**
     * Sets up test customer entities reused across test methods.
     */
    @BeforeEach
    void setUp() {
        alice = new Customer(1L, "Alice Johnson", "alice@example.com", Collections.emptyList());
        bob = new Customer(2L, "Bob Martinez", "bob@example.com", Collections.emptyList());
    }

    // -------------------------------------------------------------------------
    // getAllCustomerRewards
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("All customers — returns a summary per customer")
    void getAllCustomerRewards_returnsSummaryForEachCustomer() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(alice, bob));
        when(transactionRepository.findByCustomerIdOrderByTransactionDateAsc(1L))
                .thenReturn(Collections.emptyList());
        when(transactionRepository.findByCustomerIdOrderByTransactionDateAsc(2L))
                .thenReturn(Collections.emptyList());

        List<RewardsSummaryDto> result = rewardsService.getAllCustomerRewards();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Customer with no transactions earns 0 total points")
    void customerWithNoTransactions_earnsZeroPoints() {
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(alice));
        when(transactionRepository.findByCustomerIdOrderByTransactionDateAsc(1L))
                .thenReturn(Collections.emptyList());

        List<RewardsSummaryDto> result = rewardsService.getAllCustomerRewards();

        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getTotalPoints());
        assertTrue(result.get(0).getMonthlyPoints().isEmpty());
    }

    @Test
    @DisplayName("Single customer — monthly points are grouped correctly")
    void singleCustomer_monthlyPointsGroupedCorrectly() {
        Transaction t1 = transaction(alice, "120.00", "2024-01-05"); // 90 pts
        Transaction t2 = transaction(alice, "85.00",  "2024-01-20"); // 35 pts
        Transaction t3 = transaction(alice, "200.00", "2024-02-10"); // 250 pts

        when(customerRepository.findAll()).thenReturn(Collections.singletonList(alice));
        when(transactionRepository.findByCustomerIdOrderByTransactionDateAsc(1L))
                .thenReturn(Arrays.asList(t1, t2, t3));

        List<RewardsSummaryDto> result = rewardsService.getAllCustomerRewards();
        RewardsSummaryDto summary = result.get(0);

        assertEquals(125L, summary.getMonthlyPoints().get("2024-01")); // 90+35
        assertEquals(250L, summary.getMonthlyPoints().get("2024-02"));
        assertEquals(375L, summary.getTotalPoints());
    }

    @Test
    @DisplayName("Two customers — points are isolated per customer")
    void twoCustomers_pointsIsolatedPerCustomer() {
        Transaction aliceTx = transaction(alice, "120.00", "2024-01-05"); // 90 pts
        Transaction bobTx   = transaction(bob,   "75.00",  "2024-01-10"); // 25 pts

        when(customerRepository.findAll()).thenReturn(Arrays.asList(alice, bob));
        when(transactionRepository.findByCustomerIdOrderByTransactionDateAsc(1L))
                .thenReturn(Collections.singletonList(aliceTx));
        when(transactionRepository.findByCustomerIdOrderByTransactionDateAsc(2L))
                .thenReturn(Collections.singletonList(bobTx));

        List<RewardsSummaryDto> result = rewardsService.getAllCustomerRewards();

        RewardsSummaryDto aliceSummary = result.stream()
                .filter(s -> s.getCustomerId().equals(1L)).findFirst().orElseThrow();
        RewardsSummaryDto bobSummary = result.stream()
                .filter(s -> s.getCustomerId().equals(2L)).findFirst().orElseThrow();

        assertEquals(90L, aliceSummary.getTotalPoints());
        assertEquals(25L, bobSummary.getTotalPoints());
    }

    @Test
    @DisplayName("Months are derived from data — three distinct months appear")
    void monthsAreDerivedFromData_threeMonthsPresent() {
        Transaction t1 = transaction(alice, "120.00", "2024-01-05");
        Transaction t2 = transaction(alice, "200.00", "2024-02-10");
        Transaction t3 = transaction(alice, "110.00", "2024-03-08");

        when(customerRepository.findAll()).thenReturn(Collections.singletonList(alice));
        when(transactionRepository.findByCustomerIdOrderByTransactionDateAsc(1L))
                .thenReturn(Arrays.asList(t1, t2, t3));

        RewardsSummaryDto summary = rewardsService.getAllCustomerRewards().get(0);

        assertEquals(3, summary.getMonthlyPoints().size());
        assertTrue(summary.getMonthlyPoints().containsKey("2024-01"));
        assertTrue(summary.getMonthlyPoints().containsKey("2024-02"));
        assertTrue(summary.getMonthlyPoints().containsKey("2024-03"));
    }

    // -------------------------------------------------------------------------
    // getCustomerRewards
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getCustomerRewards — returns correct summary for valid customer id")
    void getCustomerRewards_validId_returnsCorrectSummary() {
        Transaction t1 = transaction(alice, "120.00", "2024-01-05"); // 90 pts
        Transaction t2 = transaction(alice, "60.00",  "2024-01-20"); // 10 pts

        when(customerRepository.findById(1L)).thenReturn(Optional.of(alice));
        when(transactionRepository.findByCustomerIdOrderByTransactionDateAsc(1L))
                .thenReturn(Arrays.asList(t1, t2));

        RewardsSummaryDto summary = rewardsService.getCustomerRewards(1L);

        assertNotNull(summary);
        assertEquals(1L, summary.getCustomerId());
        assertEquals("Alice Johnson", summary.getCustomerName());
        assertEquals(100L, summary.getMonthlyPoints().get("2024-01"));
        assertEquals(100L, summary.getTotalPoints());
    }

    @Test
    @DisplayName("getCustomerRewards — throws CustomerNotFoundException for unknown id")
    void getCustomerRewards_unknownId_throwsCustomerNotFoundException() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> rewardsService.getCustomerRewards(999L));
    }

    @Test
    @DisplayName("getCustomerRewards — exception message contains the missing customer id")
    void getCustomerRewards_unknownId_exceptionMessageContainsId() {
        when(customerRepository.findById(42L)).thenReturn(Optional.empty());

        CustomerNotFoundException ex = assertThrows(CustomerNotFoundException.class,
                () -> rewardsService.getCustomerRewards(42L));

        assertTrue(ex.getMessage().contains("42"));
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    /**
     * Creates a Transaction entity for use in tests.
     *
     * @param customer        the owning customer
     * @param amountStr       string representation of the amount
     * @param dateStr         ISO date string (YYYY-MM-DD)
     * @return populated Transaction instance
     */
    private Transaction transaction(Customer customer, String amountStr, String dateStr) {
        Transaction t = new Transaction();
        t.setCustomer(customer);
        t.setAmount(new BigDecimal(amountStr));
        t.setTransactionDate(LocalDate.parse(dateStr));
        return t;
    }
}
