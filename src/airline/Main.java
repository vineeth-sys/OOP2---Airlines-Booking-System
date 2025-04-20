package airline;

import java.util.*;
import java.util.concurrent.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.*;
import java.text.NumberFormat;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Admin Login
        System.out.print("Enter admin username: ");
        String adminUsername = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String adminPassword = scanner.nextLine();
        Admin admin = new Admin(adminUsername, adminPassword);

        BookingService bookingService = new BookingService();

        // Admin adds flights
        System.out.print("Enter number of flights to add: ");
        int numFlights = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < numFlights; i++) {
            System.out.println("\nEnter details for flight #" + (i + 1));
            System.out.print("Flight number: ");
            String flightNumber = scanner.nextLine();
            System.out.print("Total seats per cabin: ");
            int seats = Integer.parseInt(scanner.nextLine());
            System.out.print("Destination: ");
            String destination = scanner.nextLine();

            for (CabinType cabinType : CabinType.values()) {
                Flight flight = admin.addFlight(flightNumber + "-" + cabinType, seats, destination, cabinType);
                bookingService.addFlight(flight);
            }
        }

        // Passenger registration
        System.out.println("\n--- Passenger Registration ---");
        System.out.print("Enter username: ");
        String passengerUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String passengerPassword = scanner.nextLine();
        System.out.print("Enter full name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email address: ");
        String email = scanner.nextLine();
        System.out.print("Enter contact number: ");
        String contactNumber = scanner.nextLine();

        Passenger passenger = new Passenger(passengerUsername, passengerPassword, name, email, contactNumber);

        // Display available flights
        List<Flight> flights = bookingService.getAllFlights();
        System.out.println("\n--- Available Flights ---");
        for (int i = 0; i < flights.size(); i++) {
            System.out.println((i + 1) + ". " + flights.get(i));
        }

        System.out.print("Select a flight (enter number or flight code): ");
        String input = scanner.nextLine();
        String selectedFlightNumber;
        try {
            int choice = Integer.parseInt(input);
            selectedFlightNumber = flights.get(choice - 1).flightNumber();
        } catch (NumberFormatException e) {
            selectedFlightNumber = input.trim();
        }

        try {
            final String finalFlightNumber = selectedFlightNumber;
            ExecutorService exec = Executors.newSingleThreadExecutor();
            Future<Ticket> ticketFuture = exec.submit(() -> bookingService.bookTicket(finalFlightNumber, passenger));
            Ticket ticket = ticketFuture.get();
            exec.shutdown();

            String message = switch (ticket.cabinType()) {
                case ECONOMY -> "Standard service.";
                case BUSINESS -> "Business lounge access.";
                case FIRST_CLASS -> "Luxury experience awaits!";
            };

            System.out.println("\nCabin Perk: " + message);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            NumberFormat euro = NumberFormat.getCurrencyInstance(Locale.GERMANY);

            // HTML receipt
            String htmlReceipt = "<html><body>" +
                "<h2>Booking Receipt</h2>" +
                "<p><strong>Booking Code:</strong> " + ticket.bookingCode() + "</p>" +
                "<p><strong>Passenger:</strong> " + ticket.passengerName() + "</p>" +
                "<p><strong>Flight:</strong> " + ticket.flightNumber() + "</p>" +
                "<p><strong>Cabin:</strong> " + ticket.cabinType() + "</p>" +
                "<p><strong>Fare:</strong> " + euro.format(ticket.fare()) + "</p>" +
                "<p><strong>Email:</strong> " + passenger.getEmail() + "</p>" +
                "<p><strong>Contact:</strong> " + passenger.getContactNumber() + "</p>" +
                "<p><strong>Time:</strong> " + timestamp + "</p>" +
                "</body></html>";

            Path htmlPath = Paths.get("receipt_" + ticket.bookingCode() + ".html");
            Files.writeString(htmlPath, htmlReceipt);
            System.out.println("HTML receipt saved to: " + htmlPath.toAbsolutePath());

        } catch (ExecutionException e) {
            System.err.println("Booking failed: " + e.getCause().getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        scanner.close();
    }
}
