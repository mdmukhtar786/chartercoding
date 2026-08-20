package com.retailer.rewards.config;

import com.retailer.rewards.service.RewardsCalculatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring application configuration class.
 *
 * <p>Defines beans that are not directly managed by component scanning,
 * such as the stateless {@link RewardsCalculatorService}.</p>
 */
@Configuration
public class AppConfig {

    /**
     * Registers {@link RewardsCalculatorService} as a Spring-managed bean.
     *
     * <p>The calculator is stateless and safe to share as a singleton.</p>
     *
     * @return a new instance of {@link RewardsCalculatorService}
     */
    @Bean
    public RewardsCalculatorService rewardsCalculatorService() {
        return new RewardsCalculatorService();
    }
}
