package com.leaderyang.triptracker.controller;

import com.leaderyang.triptracker.exception.ResourceNotFoundException;
import com.leaderyang.triptracker.model.Trip;
import com.leaderyang.triptracker.service.TripService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TripController.class)
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TripService tripService;

    @Test
    void post_returns201_forValidTrip() throws Exception {
        Trip saved = new Trip("Tokyo", LocalDate.of(2027, 6, 1), LocalDate.of(2027, 6, 10), new BigDecimal("1000"));
        saved.setId(1L);
        when(tripService.create(any(Trip.class))).thenReturn(saved);

        String body = """
                {"destination":"Tokyo","startDate":"2027-06-01","endDate":"2027-06-10","budget":1000}
                """;

        mockMvc.perform(post("/api/trips").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.destination").value("Tokyo"));
    }

        @Test
    void post_returns400_whenDestinationIsBlank() throws Exception {
        String body = """
                {"destination":"","startDate":"2027-06-01","endDate":"2027-06-10","budget":1000}
                """;

        mockMvc.perform(post("/api/trips").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.destination").exists());
    }
        @Test
    void post_returns400_whenBudgetIsNegative() throws Exception {
        String body = """
                {"destination":"Tokyo","startDate":"2027-06-01","endDate":"2027-06-10","budget":-50}
                """;

        mockMvc.perform(post("/api/trips").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.budget").exists());
    }

    @Test
    void get_returns404_whenTripDoesNotExist() throws Exception {
        when(tripService.findById(99L)).thenThrow(new ResourceNotFoundException("Trip 99 not found"));

        mockMvc.perform(get("/api/trips/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Trip 99 not found"));
    }
}
