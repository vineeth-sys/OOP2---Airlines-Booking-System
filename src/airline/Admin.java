package airline;

public final class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }

    public Flight addFlight(String flightNumber, int seats, String destination, CabinType cabinType) {
        return new Flight(flightNumber, seats, destination, cabinType);
    }
}
