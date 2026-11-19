
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class Hotel {

    private ArrayList<Room> rooms;
    private ArrayList<Reservation> reservations;

    public Hotel() {
        rooms = new ArrayList<>();
        reservations = FileManager.loadBookings();

        // Standard Rooms
        for (int i = 101; i <= 105; i++) {
            rooms.add(new Room(i, "Standard", 1500));
        }

        // Deluxe Rooms
        for (int i = 201; i <= 205; i++) {
            rooms.add(new Room(i, "Deluxe", 2500));
        }

        // Suite Rooms
        for (int i = 301; i <= 305; i++) {
            rooms.add(new Room(i, "Suite", 4000));
        }
    }

    public ArrayList<Room> getAvailableRooms(
            String category, LocalDate checkIn, LocalDate checkOut) {

        ArrayList<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {

            if (!room.getCategory().equals(category)) {
                continue;
            }

            boolean available = true;

            for (Reservation reservation : reservations) {

                if (reservation.getRoom().getRoomNumber()
                        == room.getRoomNumber()
                        && reservation.getStatus().equals("Confirmed")) {

                    boolean overlaps =
                        checkIn.isBefore(reservation.getCheckOut())
                        && checkOut.isAfter(reservation.getCheckIn());

                    if (overlaps) {
                        available = false;
                        break;
                    }
                }
            }

            if (available) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    public Reservation bookRoom(
            Customer customer, Room room,
            LocalDate checkIn, LocalDate checkOut) {

        // Check availability again before booking
        boolean available = getAvailableRooms(
            room.getCategory(), checkIn, checkOut
        ).stream().anyMatch(
            r -> r.getRoomNumber() == room.getRoomNumber()
        );

        if (!available) {
            return null;
        }

        long nights = java.time.temporal.ChronoUnit.DAYS.between(
            checkIn, checkOut
        );

        double total = nights * room.getPrice();

        String bookingId = "HTL-" +
            UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Reservation reservation = new Reservation(
            bookingId, customer, room,
            checkIn, checkOut, total, "Confirmed"
        );

        reservations.add(reservation);
        FileManager.saveBookings(reservations);

        return reservation;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public boolean cancelBooking(String bookingId) {

        for (Reservation reservation : reservations) {

            if (reservation.getBookingId().equalsIgnoreCase(bookingId)
                    && reservation.getStatus().equals("Confirmed")) {

                reservation.cancel();
                FileManager.saveBookings(reservations);
                return true;
            }
        }

        return false;
    }
}
