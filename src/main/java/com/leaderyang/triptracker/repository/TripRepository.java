package com.leaderyang.triptracker.repository;

import com.leaderyang.triptracker.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {
}
