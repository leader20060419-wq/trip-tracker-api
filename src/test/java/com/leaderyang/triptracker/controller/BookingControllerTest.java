package com.leaderyang.triptracker.controller;

import com.leaderyang.triptracker.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    void post_returns400_whenProviderIsMissing() throws Exception {
        // TODO 1: write a JSON body with "type":"FLIGHT" and "price":300 but NO provider
        // TODO 2: POST it to /api/trips/1/bookings (use mockMvc.perform, like the other test)
        // TODO 3: expect status 400 and a jsonPath("$.fieldErrors.provider") that exists
    }
}