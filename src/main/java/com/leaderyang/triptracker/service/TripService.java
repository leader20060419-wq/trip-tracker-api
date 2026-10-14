package com.leaderyang.triptracker.service;

import com.leaderyang.triptracker.exception.InvalidTripException;
import com.leaderyang.triptracker.exception.ResourceNotFoundException;
import com.leaderyang.triptracker.model.Booking;
import com.leaderyang.triptracker.model.Trip;
import com.leaderyang.triptracker.model.TripSummary;
import com.leaderyang.triptracker.repository.BookingRepository;
import com.leaderyang.triptracker.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final BookingRepository bookingRepository;

    public TripService(TripRepository tripRepository, BookingRepository bookingRepository) {
        this.tripRepository = tripRepository;
        this.bookingRepository = bookingRepository;
    }

    public Trip create(Trip trip) {
        validateDates(trip);
        trip.setId(null); // never let the client choose the id
        return tripRepository.save(trip);
    }

    public List<Trip> findAll() {
        return tripRepository.findAll();
    }

    public Trip findById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip " + id + " not found"));
    }

    public Trip update(Long id, Trip updated) {
        Trip existing = findById(id);
        validateDates(updated);
        existing.setDestination(updated.getDestination());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setBudget(updated.getBudget());
        return tripRepository.save(existing);
    }

    public void delete(Long id) {
        Trip trip = findById(id);
        bookingRepository.deleteAll(bookingRepository.findByTripId(id));
        tripRepository.delete(trip);
    }

    public TripSummary summarize(Long id) {
        Trip trip = findById(id);
        BigDecimal total = bookingRepository.findByTripId(id).stream()
                .map(Booking::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal remaining = trip.getBudget().subtract(total);
        return new TripSummary(id, trip.getBudget(), total, remaining);
    }

    private void validateDates(Trip trip) {
        if (trip.getEndDate().isBefore(trip.getStartDate())) {
            throw new InvalidTripException("endDate cannot be before startDate");
        }
    }
}
