package airline;

public record Ticket(String flightNumber, String passengerName, CabinType cabinType, int fare) {
    public String bookingCode() {
        return flightNumber + "-" + passengerName.hashCode();
    }
}
