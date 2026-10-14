# Trip Tracker API

A REST API for planning trips and tracking bookings (flights, hotels, cars) against a budget.
Built with Java 17, Spring Boot, Spring Data JPA, and an in-memory H2 database.

## Features

- Create, read, update, and delete trips
- Add and remove bookings for a trip
- Budget summary per trip (total booking cost and remaining budget)
- Request validation with clear JSON error messages
- Unit tests (JUnit 5, Mockito), controller tests (MockMvc), and an end-to-end integration test

## Tech stack

Java 17 | Spring Boot 3 | Spring Web | Spring Data JPA | H2 | JUnit 5 | Mockito | Maven

## Run it

Requires Java 17+ and Maven.

```bash
mvn spring-boot:run
```

The API starts at `http://localhost:8080`.

## Run the tests

```bash
mvn test
```

## API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/trips` | Create a trip |
| GET | `/api/trips` | List all trips |
| GET | `/api/trips/{id}` | Get one trip |
| PUT | `/api/trips/{id}` | Update a trip |
| DELETE | `/api/trips/{id}` | Delete a trip and its bookings |
| GET | `/api/trips/{id}/summary` | Budget, total booking cost, remaining budget |
| POST | `/api/trips/{id}/bookings` | Add a booking (`FLIGHT`, `HOTEL`, `CAR`) |
| GET | `/api/trips/{id}/bookings` | List a trip's bookings |
| DELETE | `/api/trips/{id}/bookings/{bookingId}` | Remove a booking |

### Example

```bash
# Create a trip
curl -X POST http://localhost:8080/api/trips \
  -H "Content-Type: application/json" \
  -d '{"destination":"Tokyo","startDate":"2027-06-01","endDate":"2027-06-10","budget":1000}'

# Add a booking to trip 1
curl -X POST http://localhost:8080/api/trips/1/bookings \
  -H "Content-Type: application/json" \
  -d '{"type":"FLIGHT","provider":"Delta","price":300}'

# See how much budget is left
curl http://localhost:8080/api/trips/1/summary
```

Invalid requests return a 400 with details, and unknown ids return a 404:

```json
{
  "timestamp": "2027-01-01T12:00:00Z",
  "status": 400,
  "error": "Validation failed",
  "fieldErrors": { "destination": "destination is required" }
}
```

## Design notes

- **Layers:** controller (HTTP) -> service (business rules) -> repository (database).
- **Data model:** one `Trip` has many `Booking`s (many-to-one from `Booking` to `Trip`).
- **Validation:** Jakarta Bean Validation on the entities; a service-level rule rejects trips that end before they start.
- **Error handling:** a single `@RestControllerAdvice` converts exceptions into consistent JSON.
- **Money:** `BigDecimal` is used for budgets and prices to avoid floating-point rounding errors.

## Possible next steps

- Switch H2 to PostgreSQL
- Add pagination and filtering to `GET /api/trips`
- Add authentication
- Containerize with Docker
