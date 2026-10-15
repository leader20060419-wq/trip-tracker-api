package com.leaderyang.triptracker.service;

import com.leaderyang.triptracker.exception.InvalidTripException;
import com.leaderyang.triptracker.exception.ResourceNotFoundException;
import com.leaderyang.triptracker.model.Booking;
import com.leaderyang.triptracker.model.BookingType;
import com.leaderyang.triptracker.model.Trip;
import com.leaderyang.triptracker.model.TripSummary;
import com.leaderyang.triptracker.repository.BookingRepository;
import com.leaderyang.triptracker.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private TripService tripService;

    private Trip sampleTrip() {
        return new Trip("Tokyo", LocalDate.of(2027, 6, 1), LocalDate.of(2027, 6, 10), new BigDecimal("1000"));
    }

    @Test
    void create_savesTrip_whenDatesAreValid() {
        Trip trip = sampleTrip();
        when(tripRepository.save(trip)).thenReturn(trip);

        Trip saved = tripService.create(trip);

        assertEquals("Tokyo", saved.getDestination());
        verify(tripRepository).save(trip);
    }

    @Test
    void create_rejectsTrip_whenEndDateIsBeforeStartDate() {
        Trip trip = new Trip("Paris", LocalDate.of(2027, 6, 10), LocalDate.of(2027, 6, 1), new BigDecimal("500"));

        assertThrows(InvalidTripException.class, () -> tripService.create(trip));
        verify(tripRepository, never()).save(any());
    }

    @Test
    void findById_throwsNotFound_whenTripDoesNotExist() {
        when(tripRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tripService.findById(99L));
    }

    @Test
    void summarize_subtractsBookingCostsFromBudget() {
        Trip trip = sampleTrip();
        trip.setId(1L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(bookingRepository.findByTripId(1L)).thenReturn(List.of(
                new Booking(BookingType.FLIGHT, "Delta", new BigDecimal("300")),
                new Booking(BookingType.HOTEL, "Hilton", new BigDecimal("250.50"))));

        TripSummary summary = tripService.summarize(1L);

        assertEquals(0, new BigDecimal("550.50").compareTo(summary.totalBookingCost()));
        assertEquals(0, new BigDecimal("449.50").compareTo(summary.remainingBudget()));
    }

    @Test
    void summarize_returnsFullBudget_whenThereAreNoBookings() {
        Trip trip = sampleTrip();
        trip.setId(2L);
        when(tripRepository.findById(2L)).thenReturn(Optional.of(trip));
        when(bookingRepository.findByTripId(2L)).thenReturn(List.of());

        TripSummary summary = tripService.summarize(2L);

        assertEquals(0, BigDecimal.ZERO.compareTo(summary.totalBookingCost()));
        assertEquals(0, new BigDecimal("1000").compareTo(summary.remainingBudget()));
    }
        @Test
    void delete_removesBookingsAndTheTrip() {
        Trip trip = sampleTrip();
        trip.setId(1L);
        List<Booking> bookings = List.of(
                new Booking(BookingType.FLIGHT, "Delta", new BigDecimal("300")));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(bookingRepository.findByTripId(1L)).thenReturn(bookings);

        tripService.delete(1L);

        // TODO 1: verify that bookingRepository.deleteAll was called with bookings
        // TODO 2: verify that tripRepository.delete was called with trip
    }
}
