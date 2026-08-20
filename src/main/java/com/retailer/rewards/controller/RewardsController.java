package com.retailer.rewards.controller;

import com.retailer.rewards.dto.RewardsSummaryDto;
import com.retailer.rewards.service.RewardsService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing endpoints for the customer rewards program.
 *
 * <p>Base path: {@code /api/rewards}</p>
 *
 * <p>Available endpoints:
 * <ul>
 *   <li>{@code GET /api/rewards} — rewards summary for all customers</li>
 *   <li>{@code GET /api/rewards/{customerId}} — rewards summary for a specific customer</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

    private final RewardsService rewardsService;

    /**
     * Constructs the RewardsController with its required service dependency.
     *
     * @param rewardsService service that calculates reward points
     */
    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    /**
     * Retrieves the rewards summary for every customer in the system.
     *
     * <p>Each entry includes monthly point breakdowns and a cumulative total,
     * derived dynamically from the transaction data.</p>
     *
     * @return HTTP 200 with a list of {@link RewardsSummaryDto} for all customers
     */
    @GetMapping
    public ResponseEntity<List<RewardsSummaryDto>> getAllRewards() {
        List<RewardsSummaryDto> summaries = rewardsService.getAllCustomerRewards();
        return ResponseEntity.ok(summaries);
    }

    /**
     * Retrieves the rewards summary for a single customer.
     *
     * @param customerId the unique identifier of the customer
     * @return HTTP 200 with the customer's {@link RewardsSummaryDto},
     *         or HTTP 404 if no customer exists with that id
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<RewardsSummaryDto> getRewardsByCustomer(@PathVariable Long customerId) {
        RewardsSummaryDto summary = rewardsService.getCustomerRewards(customerId);
        return ResponseEntity.ok(summary);
    }
}
