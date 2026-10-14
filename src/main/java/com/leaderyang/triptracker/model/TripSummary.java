package com.leaderyang.triptracker.model;

import java.math.BigDecimal;

/**
 * Read-only view of how much of a trip's budget has been spent on bookings.
 */
public record TripSummary(Long tripId,
                          BigDecimal budget,
                          BigDecimal totalBookingCost,
                          BigDecimal remainingBudget) {
}
