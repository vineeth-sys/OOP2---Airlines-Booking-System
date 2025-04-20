package airline;

import java.util.*;

public class BookingService {
    private final Map<String, Flight> flightMap = new HashMap<>();

    public void addFlight(Flight flight) {
        flightMap.put(flight.flightNumber(), flight);
    }

    public Ticket bookTicket(String flightNumber, Passenger passenger) {
        Flight flight = flightMap.get(flightNumber);
        if (flight == null || !flight.hasSeatsAvailable()) {
            throw new RuntimeException("Cannot book ticket. Flight full or not found.");
        }
        Flight updatedFlight = flight.bookSeat();
        flightMap.put(flightNumber, updatedFlight);

        int fare = FareCalculator.calculateFare(flight.cabinType());
        Ticket ticket = new Ticket(flightNumber, passenger.username, flight.cabinType(), fare);
        passenger.addTicket(ticket);
        return ticket;
    }

    public List<Flight> getAllFlights() {
        return new ArrayList<>(flightMap.values());
    }
}
