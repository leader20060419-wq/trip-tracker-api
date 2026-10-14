package com.leaderyang.triptracker.repository;

import com.leaderyang.triptracker.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Explicit query: Booking has a getTripId() helper that is not a database column,
    // so Spring Data cannot derive this query from the method name alone.
    @Query("select b from Booking b where b.trip.id = :tripId")
    List<Booking> findByTripId(@Param("tripId") Long tripId);
}