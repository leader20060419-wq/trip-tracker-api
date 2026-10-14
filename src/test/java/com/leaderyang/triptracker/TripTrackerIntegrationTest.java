package com.leaderyang.triptracker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Runs the whole stack (controller, service, JPA, in-memory H2) with no mocks.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TripTrackerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTrip_addBooking_thenSummaryShowsRemainingBudget() throws Exception {
        String tripJson = """
                {"destination":"Seattle","startDate":"2027-08-01","endDate":"2027-08-05","budget":1000}
                """;
        String tripResponse = mockMvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON).content(tripJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long tripId = objectMapper.readTree(tripResponse).get("id").asLong();

        String bookingJson = """
                {"type":"FLIGHT","provider":"Alaska","price":300}
                """;
        mockMvc.perform(post("/api/trips/" + tripId + "/bookings")
                        .contentType(MediaType.APPLICATION_JSON).content(bookingJson))
                .andExpect(status().isCreated());

        String summaryResponse = mockMvc.perform(get("/api/trips/" + tripId + "/summary"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JsonNode summary = objectMapper.readTree(summaryResponse);

        assertEquals(0, new BigDecimal("300").compareTo(summary.get("totalBookingCost").decimalValue()));
        assertEquals(0, new BigDecimal("700").compareTo(summary.get("remainingBudget").decimalValue()));
    }

    @Test
    void deleteTrip_thenGetReturns404() throws Exception {
        String tripJson = """
                {"destination":"Austin","startDate":"2027-09-01","endDate":"2027-09-03","budget":500}
                """;
        String response = mockMvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON).content(tripJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long tripId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/trips/" + tripId)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/trips/" + tripId)).andExpect(status().isNotFound());
    }
}
