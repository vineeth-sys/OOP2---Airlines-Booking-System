package airline;

import java.util.*;

public final class Passenger extends User {
    private final String name;
    private final String email;
    private final String contactNumber;
    private final List<Ticket> tickets = new ArrayList<>();

    public Passenger(String username, String password, String name, String email, String contactNumber) {
        super(username, password);
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public String getEmail() { return email; }
    public String getContactNumber() { return contactNumber; }
}
