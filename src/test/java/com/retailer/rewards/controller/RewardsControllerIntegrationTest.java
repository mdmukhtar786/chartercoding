package com.retailer.rewards.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link RewardsController}.
 *
 * <p>Boots the full Spring application context with the H2 in-memory database
 * seeded from {@code schema.sql} and {@code data.sql}. Tests exercise the
 * complete request-response cycle including JSON serialisation.</p>
 *
 * <p>Expected data (from data.sql):
 * <ul>
 *   <li>Customer 1 (Alice): Jan=125, Feb=250, Mar=80, Total=455</li>
 *   <li>Customer 2 (Bob):   Jan=25,  Feb=150, Mar=210, Total=385</li>
 *   <li>Customer 3 (Carol): Jan=400, Feb=25,  Mar=480, Total=905</li>
 *   <li>Customer 4 (David): Jan=45,  Feb=240, Mar=315, Total=600</li>
 * </ul>
 * </p>
 */
@SpringBootTest
@AutoConfigureMockMvc
class RewardsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // -------------------------------------------------------------------------
    // GET /api/rewards
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("GET /api/rewards — returns HTTP 200 with all 4 customers")
    void getAllRewards_returnsAllCustomers() throws Exception {
        mockMvc.perform(get("/api/rewards").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    @DisplayName("GET /api/rewards — each entry has required fields")
    void getAllRewards_eachEntryHasRequiredFields() throws Exception {
        mockMvc.perform(get("/api/rewards").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId",    notNullValue()))
                .andExpect(jsonPath("$[0].customerName",  notNullValue()))
                .andExpect(jsonPath("$[0].customerEmail", notNullValue()))
                .andExpect(jsonPath("$[0].monthlyPoints", notNullValue()))
                .andExpect(jsonPath("$[0].totalPoints",   notNullValue()));
    }

    // -------------------------------------------------------------------------
    // GET /api/rewards/{customerId} — valid customers
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("GET /api/rewards/1 — Alice total points = 455")
    void getRewards_alice_correctTotalPoints() throws Exception {
        mockMvc.perform(get("/api/rewards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId",   is(1)))
                .andExpect(jsonPath("$.customerName", is("Alice Johnson")))
                .andExpect(jsonPath("$.totalPoints",  is(455)));
    }

    @Test
    @DisplayName("GET /api/rewards/1 — Alice monthly breakdown is correct")
    void getRewards_alice_correctMonthlyBreakdown() throws Exception {
        mockMvc.perform(get("/api/rewards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyPoints['2024-01']", is(125)))
                .andExpect(jsonPath("$.monthlyPoints['2024-02']", is(250)))
                .andExpect(jsonPath("$.monthlyPoints['2024-03']", is(80)));
    }

    @Test
    @DisplayName("GET /api/rewards/2 — Bob total points = 385")
    void getRewards_bob_correctTotalPoints() throws Exception {
        mockMvc.perform(get("/api/rewards/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId",  is(2)))
                .andExpect(jsonPath("$.totalPoints", is(385)));
    }

    @Test
    @DisplayName("GET /api/rewards/2 — Bob monthly breakdown is correct")
    void getRewards_bob_correctMonthlyBreakdown() throws Exception {
        mockMvc.perform(get("/api/rewards/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyPoints['2024-01']", is(25)))
                .andExpect(jsonPath("$.monthlyPoints['2024-02']", is(150)))
                .andExpect(jsonPath("$.monthlyPoints['2024-03']", is(210)));
    }

    @Test
    @DisplayName("GET /api/rewards/3 — Carol total points = 905")
    void getRewards_carol_correctTotalPoints() throws Exception {
        mockMvc.perform(get("/api/rewards/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId",  is(3)))
                .andExpect(jsonPath("$.totalPoints", is(905)));
    }

    @Test
    @DisplayName("GET /api/rewards/4 — David total points = 600")
    void getRewards_david_correctTotalPoints() throws Exception {
        mockMvc.perform(get("/api/rewards/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId",  is(4)))
                .andExpect(jsonPath("$.totalPoints", is(600)));
    }

    @Test
    @DisplayName("GET /api/rewards/4 — David monthly breakdown is correct")
    void getRewards_david_correctMonthlyBreakdown() throws Exception {
        mockMvc.perform(get("/api/rewards/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyPoints['2024-01']", is(45)))
                .andExpect(jsonPath("$.monthlyPoints['2024-02']", is(240)))
                .andExpect(jsonPath("$.monthlyPoints['2024-03']", is(315)));
    }

    // -------------------------------------------------------------------------
    // Negative / error scenarios
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("GET /api/rewards/999 — returns HTTP 404 for non-existent customer")
    void getRewards_nonExistentCustomer_returns404() throws Exception {
        mockMvc.perform(get("/api/rewards/999").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status",    is(404)))
                .andExpect(jsonPath("$.message",   notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/rewards/abc — returns HTTP 400 for invalid path variable type")
    void getRewards_invalidIdType_returns400() throws Exception {
        mockMvc.perform(get("/api/rewards/abc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    @DisplayName("GET /api/rewards/0 — returns HTTP 404 for id=0 (no such customer)")
    void getRewards_zeroId_returns404() throws Exception {
        mockMvc.perform(get("/api/rewards/0").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
