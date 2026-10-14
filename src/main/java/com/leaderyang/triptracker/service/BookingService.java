package com.leaderyang.triptracker.service;

import com.leaderyang.triptracker.exception.ResourceNotFoundException;
import com.leaderyang.triptracker.model.Booking;
import com.leaderyang.triptracker.model.Trip;
import com.leaderyang.triptracker.repository.BookingRepository;
import com.leaderyang.triptracker.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final TripRepository tripRepository;
    private final BookingRepository bookingRepository;

    public BookingService(TripRepository tripRepository, BookingRepository bookingRepository) {
        this.tripRepository = tripRepository;
        this.bookingRepository = bookingRepository;
    }

    public Booking add(Long tripId, Booking booking) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip " + tripId + " not found"));
        booking.setId(null);
        booking.setTrip(trip);
        return bookingRepository.save(booking);
    }

    public List<Booking> list(Long tripId) {
        if (!tripRepository.existsById(tripId)) {
            throw new ResourceNotFoundException("Trip " + tripId + " not found");
        }
        return bookingRepository.findByTripId(tripId);
    }

    public void delete(Long tripId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .filter(b -> b.getTrip() != null && tripId.equals(b.getTrip().getId()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking " + bookingId + " not found for trip " + tripId));
        bookingRepository.delete(booking);
    }
}
