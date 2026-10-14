package com.leaderyang.triptracker.service;

import com.leaderyang.triptracker.exception.ResourceNotFoundException;
import com.leaderyang.triptracker.model.Booking;
import com.leaderyang.triptracker.model.BookingType;
import com.leaderyang.triptracker.model.Trip;
import com.leaderyang.triptracker.repository.BookingRepository;
import com.leaderyang.triptracker.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    private Trip tripWithId(Long id) {
        Trip trip = new Trip("Rome", LocalDate.of(2027, 7, 1), LocalDate.of(2027, 7, 8), new BigDecimal("2000"));
        trip.setId(id);
        return trip;
    }

    @Test
    void add_attachesBookingToTrip() {
        Trip trip = tripWithId(1L);
        Booking booking = new Booking(BookingType.HOTEL, "Marriott", new BigDecimal("400"));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking saved = bookingService.add(1L, booking);

        assertEquals(trip, saved.getTrip());
        assertEquals(1L, saved.getTripId());
    }

    @Test
    void add_throwsNotFound_whenTripDoesNotExist() {
        Booking booking = new Booking(BookingType.CAR, "Hertz", new BigDecimal("120"));
        when(tripRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.add(42L, booking));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void delete_throwsNotFound_whenBookingBelongsToDifferentTrip() {
        Booking booking = new Booking(BookingType.FLIGHT, "United", new BigDecimal("350"));
        booking.setTrip(tripWithId(2L));
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));

        assertThrows(ResourceNotFoundException.class, () -> bookingService.delete(1L, 10L));
        verify(bookingRepository, never()).delete(any());
    }

    @Test
    void delete_removesBooking_whenItBelongsToTheTrip() {
        Booking booking = new Booking(BookingType.FLIGHT, "United", new BigDecimal("350"));
        booking.setTrip(tripWithId(1L));
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));

        bookingService.delete(1L, 10L);

        verify(bookingRepository).delete(booking);
    }
}
