package com.leaderyang.triptracker.controller;

import com.leaderyang.triptracker.model.Trip;
import com.leaderyang.triptracker.model.TripSummary;
import com.leaderyang.triptracker.service.TripService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Trip create(@Valid @RequestBody Trip trip) {
        return tripService.create(trip);
    }

    @GetMapping
    public List<Trip> list() {
        return tripService.findAll();
    }

    @GetMapping("/{id}")
    public Trip get(@PathVariable Long id) {
        return tripService.findById(id);
    }

    @PutMapping("/{id}")
    public Trip update(@PathVariable Long id, @Valid @RequestBody Trip trip) {
        return tripService.update(id, trip);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tripService.delete(id);
    }

    @GetMapping("/{id}/summary")
    public TripSummary summary(@PathVariable Long id) {
        return tripService.summarize(id);
    }
}
