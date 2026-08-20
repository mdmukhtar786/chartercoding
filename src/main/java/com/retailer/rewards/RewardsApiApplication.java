package com.retailer.rewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Rewards API Spring Boot application.
 *
 * <p>This application provides a RESTful API for calculating customer
 * reward points based on purchase transactions over a rolling period.</p>
 */
@SpringBootApplication
public class RewardsApiApplication {

    /**
     * Main method that bootstraps the Spring Boot application.
     *
     * @param args command-line arguments passed at startup
     */
    public static void main(String[] args) {
        SpringApplication.run(RewardsApiApplication.class, args);
    }
}
