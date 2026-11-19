
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class FileManager {

    private static final String FILE_PATH = "data/bookings.txt";

    private static String encode(String value) {
        return Base64.getEncoder().encodeToString(
            value.getBytes(StandardCharsets.UTF_8)
        );
    }

    private static String decode(String value) {
        return new String(
            Base64.getDecoder().decode(value),
            StandardCharsets.UTF_8
        );
    }

    public static void saveBookings(ArrayList<Reservation> bookings) {

        File folder = new File("data");
        folder.mkdirs();

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(FILE_PATH))) {

            for (Reservation r : bookings) {
                writer.println(
                    r.getBookingId() + "|" +
                    encode(r.getCustomer().getName()) + "|" +
                    encode(r.getCustomer().getEmail()) + "|" +
                    encode(r.getCustomer().getPhone()) + "|" +
                    r.getRoom().getRoomNumber() + "|" +
                    r.getRoom().getCategory() + "|" +
                    r.getRoom().getPrice() + "|" +
                    r.getCheckIn() + "|" +
                    r.getCheckOut() + "|" +
                    r.getTotalAmount() + "|" +
                    r.getStatus()
                );
            }

        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    public static ArrayList<Reservation> loadBookings() {

        ArrayList<Reservation> bookings = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return bookings;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    String[] data = line.split("\\|", -1);

                    if (data.length != 11) {
                        continue;
                    }

                    Customer customer = new Customer(
                        decode(data[1]),
                        decode(data[2]),
                        decode(data[3])
                    );

                    Room room = new Room(
                        Integer.parseInt(data[4]),
                        data[5],
                        Double.parseDouble(data[6])
                    );

                    Reservation reservation = new Reservation(
                        data[0],
                        customer,
                        room,
                        java.time.LocalDate.parse(data[7]),
                        java.time.LocalDate.parse(data[8]),
                        Double.parseDouble(data[9]),
                        data[10]
                    );

                    bookings.add(reservation);

                } catch (Exception e) {
                    System.out.println("Skipping invalid booking record.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }

        return bookings;
    }
}
