package com.leaderyang.triptracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "type is required (FLIGHT, HOTEL or CAR)")
    @Enumerated(EnumType.STRING)
    private BookingType type;

    @NotBlank(message = "provider is required")
    private String provider;

    @NotNull(message = "price is required")
    @PositiveOrZero(message = "price cannot be negative")
    private BigDecimal price;

    // Many bookings belong to one trip. Hidden from JSON; tripId is exposed instead.
    @JsonIgnore
    @ManyToOne
    private Trip trip;

    public Booking() {
    }

    public Booking(BookingType type, String provider, BigDecimal price) {
        this.type = type;
        this.provider = provider;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookingType getType() {
        return type;
    }

    public void setType(BookingType type) {
        this.type = type;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Long getTripId() {
        return trip == null ? null : trip.getId();
    }
}
