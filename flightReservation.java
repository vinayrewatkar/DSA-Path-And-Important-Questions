import java.util.*;

import java.time.LocalDateTime;

/**
 * Represents a flight reservation system that allows users to search for
 * flights and make reservations.
 * 
 */

public class FlightReservationSystem {

    /**
     * Searches for available flights based on the specified criteria.
     *
     * @param origin        The departure airport code.
     * @param destination   The arrival airport code.
     * @param departureDate The date of departure (YYYY-MM-DD).
     * @param returnDate    The date of return (YYYY-MM-DD, optional).
     * @param passengers    The number of passengers (adults, children, infants).
     * @param travelClass   The class of service (e.g., Economy, Business).
     * @return A list of available flight options.
     * 
     */

    private List<Flight> availableFlights;
    private List<Reservation> reservations;

    public FlightReservationSystem() {
        this.availableFlights = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    public void addFlight(Flight flight) {
        availableFlights.add(flight);
    }

    public List<Flight> searchFlights(String origin, String destination, String departureDate,
            String returnDate, int passengers, String travelClass) {
        // Implement flight search logic here
        // Return a list of Flight objects matching the criteria
        List<Flight> matchingFlights = new ArrayList<>();

        for (Flight flight : availableFlights) {
            if (flight.matchesCriteria(origin, destination, departureDate, returnDate, passengers, travelClass)) {
                matchingFlights.add(flight);
            }
        }

        return matchingFlights;

    }

    /**
     * Makes a flight reservation for the specified flight and passengers.
     *
     * @param flightId    The unique identifier for the selected flight.
     * @param passengers  The list of passenger details.
     * @param paymentInfo The payment information (credit card details).
     * @return A reservation confirmation.
     */
    public Reservation makeReservation(String flightId, List<Passenger> passengers, PaymentInfo paymentInfo) {
        // Implement reservation logic here
        // Return a reservation confirmation
        Flight selectedFlight = findFlightById(flightId);
        if (selectedFlight == null) {
            throw new IllegalArgumentException("Flight not found with ID: " + flightId);
        }

        boolean seatsAvailable = selectedFlight.reserveSeats(passengers.size(), paymentInfo.getTravelClass());
        if (!seatsAvailable) {
            throw new RuntimeException("Seats not available for the selected flight.");
        }

        Reservation reservation = new Reservation(selectedFlight, passengers, paymentInfo);
        reservations.add(reservation);
        return reservation;
    }

    private Flight findFlightById(String flightId) {
        for (Flight flight : availableFlights) {
            if (flight.getFlightId().equals(flightId)) {
                return flight;
            }
        }
        return null;
    }
}

/**
 * Represents a flight available for booking.
 */
public class Flight {

    private String flightId;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private String airline;
    private Map<String, Integer> availableSeatsByClass;

    public Flight(String flightId, String departureAirportCode, String arrivalAirportCode,
            LocalDateTime departureDateTime, LocalDateTime arrivalDateTime, String airline) {
        this.flightId = flightId;
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.airline = airline;
        this.availableSeatsByClass = new HashMap<>();
    }

    public String getFlightId() {
        return flightId;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public String getAirline() {
        return airline;
    }

    public Map<String, Integer> getAvailableSeatsByClass() {
        return availableSeatsByClass;
    }

    // Other flight-related properties and methods

}

/**
 * Represents a passenger with personal information.
 */
public class Passenger {

    private String name;

    public Passenger(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Other passenger-related properties and methods
}

/**
 * Represents payment information for a reservation.
 */
public class PaymentInfo {

    private String creditCardNumber;
    private String travelClass;

    public PaymentInfo(String creditCardNumber, String travelClass) {
        this.creditCardNumber = creditCardNumber;
        this.travelClass = travelClass;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getTravelClass() {
        return travelClass;
    }
}

// Other payment-related properties and methods

/**
 * Represents a flight reservation confirmation.
 */
public class Reservation {

    private String reservationId;
    private Flight flight;
    private List<Passenger> passengers;
    private PaymentInfo paymentInfo;

    public Reservation(Flight flight, List<Passenger> passengers, PaymentInfo paymentInfo) {
        this.reservationId = generateReservationId();
        this.flight = flight;
        this.passengers = passengers;
        this.paymentInfo = paymentInfo;
    }

    private String generateReservationId() {
        // Implement a logic to generate a unique reservation ID
        return UUID.randomUUID().toString();
    }

    public String getReservationId() {
        return reservationId;
    }

    // Other reservation-related properties and methods
}

public class Main {
    public static void main(String[] args) {
        FlightReservationSystem reservationSystem = new FlightReservationSystem();

        // Create and add flights to the system
        Flight flight1 = new Flight("F001", "JFK", "LAX",
                LocalDateTime.of(2023, 10, 1, 10, 0),
                LocalDateTime.of(2023, 10, 1, 14, 0), "Airline1");
        Flight flight2 = new Flight("F002", "LAX", "JFK",
                LocalDateTime.of(2023, 10, 2, 12, 0),
                LocalDateTime.of(2023, 10, 2, 16, 0), "Airline2");
        reservationSystem.addFlight(flight1);
        reservationSystem.addFlight(flight2);

        // Search for available flights
        List<Flight> availableFlights = reservationSystem.searchFlights("JFK", "LAX", "2023-10-01",
                null, 2, "Economy");

        // Make a reservation
        if (!availableFlights.isEmpty()) {
            Flight selectedFlight = availableFlights.get(0); // Assuming user selects the first available flight

            // Create passengers
            List<Passenger> passengers = new ArrayList<>();
            passengers.add(new Passenger("Passenger1"));
            passengers.add(new Passenger("Passenger2"));

            // Create payment information
            PaymentInfo paymentInfo = new PaymentInfo("1234-5678-9012-3456", "Economy");

            Reservation reservation = reservationSystem.makeReservation(selectedFlight.getFlightId(), passengers,
                    paymentInfo);

            // Display reservation confirmation
            System.out.println("Reservation ID: " + reservation.getReservationId());
        } else {
            System.out.println("No available flights matching the criteria.");
        }
    }
}
