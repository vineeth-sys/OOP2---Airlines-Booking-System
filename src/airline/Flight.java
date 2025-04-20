package airline;

public record Flight(String flightNumber, int seats, String destination, CabinType cabinType) {
    public boolean hasSeatsAvailable() {
        return seats > 0;
    }

    public Flight bookSeat() {
        if (seats <= 0) throw new RuntimeException("No seats available");
        return new Flight(flightNumber, seats - 1, destination, cabinType);
    }
}
