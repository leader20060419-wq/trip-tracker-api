package com.leaderyang.triptracker.controller;

import com.leaderyang.triptracker.model.Booking;
import com.leaderyang.triptracker.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trips/{tripId}/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking add(@PathVariable Long tripId, @Valid @RequestBody Booking booking) {
        return bookingService.add(tripId, booking);
    }

    @GetMapping
    public List<Booking> list(@PathVariable Long tripId) {
        return bookingService.list(tripId);
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long tripId, @PathVariable Long bookingId) {
        bookingService.delete(tripId, bookingId);
    }
}
