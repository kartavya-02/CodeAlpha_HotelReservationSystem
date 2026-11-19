
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Main extends JFrame {

    private Hotel hotel;
    private ArrayList<Room> availableRooms;

    private JComboBox<String> categoryBox;
    private JTextField checkInField, checkOutField;

    private JTable roomTable;
    private DefaultTableModel roomModel;

    private JTable bookingTable;
    private DefaultTableModel bookingModel;

    private Color darkBlue = new Color(25, 45, 75);
    private Color lightBlue = new Color(225, 238, 250);

    public Main() {

        hotel = new Hotel();
        availableRooms = new ArrayList<>();

        setTitle("Hotel Reservation System");
        setSize(1050, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        createHeader();

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 14));

        tabs.addTab("Search & Book Rooms", createSearchPanel());
        tabs.addTab("My Bookings", createBookingsPanel());

        add(tabs, BorderLayout.CENTER);

        refreshBookings();
    }

    private void createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(darkBlue);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("HOTEL RESERVATION SYSTEM");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel subtitle = new JLabel("Find your perfect stay");
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(title);
        textPanel.add(subtitle);

        header.add(textPanel, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);
    }

    private JPanel createSearchPanel() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel heading = new JLabel("Search Available Rooms");
        heading.setFont(new Font("Arial", Font.BOLD, 21));
        heading.setForeground(darkBlue);

        JPanel form = new JPanel(new GridLayout(2, 4, 12, 10));
        form.setBackground(Color.WHITE);

        categoryBox = new JComboBox<>(
            new String[]{"Standard", "Deluxe", "Suite"}
        );

        checkInField = new JTextField("2026-12-01");
        checkOutField = new JTextField("2026-12-03");

        form.add(new JLabel("Room Category"));
        form.add(new JLabel("Check-in (YYYY-MM-DD)"));
        form.add(new JLabel("Check-out (YYYY-MM-DD)"));
        form.add(new JLabel(""));

        form.add(categoryBox);
        form.add(checkInField);
        form.add(checkOutField);

        JButton searchButton = new JButton("Search Rooms");
        styleButton(searchButton);
        searchButton.addActionListener(e -> searchRooms());

        form.add(searchButton);

        roomModel = new DefaultTableModel(
            new String[]{"Room Number", "Category", "Price Per Night (Rs.)"},
            0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        roomTable = new JTable(roomModel);
        styleTable(roomTable);

        JScrollPane scrollPane = new JScrollPane(roomTable);

        JButton bookButton = new JButton("Book Selected Room");
        styleButton(bookButton);
        bookButton.addActionListener(e -> bookSelectedRoom());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(Color.WHITE);
        bottom.add(bookButton);

        JPanel top = new JPanel(new BorderLayout(10, 15));
        top.setBackground(Color.WHITE);
        top.add(heading, BorderLayout.NORTH);
        top.add(form, BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBookingsPanel() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel heading = new JLabel("Reservation Management");
        heading.setFont(new Font("Arial", Font.BOLD, 21));
        heading.setForeground(darkBlue);

        bookingModel = new DefaultTableModel(
            new String[]{
                "Booking ID", "Customer", "Room",
                "Category", "Check-in", "Check-out",
                "Amount (Rs.)", "Status"
            }, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookingTable = new JTable(bookingModel);
        styleTable(bookingTable);

        JButton refreshButton = new JButton("Refresh Bookings");
        JButton detailsButton = new JButton("View Details");
        JButton cancelButton = new JButton("Cancel Booking");

        styleButton(refreshButton);
        styleButton(detailsButton);
        styleButton(cancelButton);

        refreshButton.addActionListener(e -> refreshBookings());
        detailsButton.addActionListener(e -> viewBookingDetails());
        cancelButton.addActionListener(e -> cancelSelectedBooking());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setBackground(Color.WHITE);

        buttons.add(refreshButton);
        buttons.add(detailsButton);
        buttons.add(cancelButton);

        panel.add(heading, BorderLayout.NORTH);
        panel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void searchRooms() {

        try {

            LocalDate checkIn = LocalDate.parse(
                checkInField.getText().trim()
            );

            LocalDate checkOut = LocalDate.parse(
                checkOutField.getText().trim()
            );

            if (!checkIn.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(
                    this, "Check-in must be a future date."
                );
                return;
            }

            if (!checkOut.isAfter(checkIn)) {
                JOptionPane.showMessageDialog(
                    this, "Check-out must be after check-in."
                );
                return;
            }

            String category = (String) categoryBox.getSelectedItem();

            availableRooms = hotel.getAvailableRooms(
                category, checkIn, checkOut
            );

            roomModel.setRowCount(0);

            for (Room room : availableRooms) {
                roomModel.addRow(new Object[]{
                    room.getRoomNumber(),
                    room.getCategory(),
                    String.format("%.2f", room.getPrice())
                });
            }

            if (availableRooms.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "No rooms available for these dates."
                );
            }

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                this, "Please enter valid dates in YYYY-MM-DD format."
            );
        }
    }

    private void bookSelectedRoom() {

        int selectedRow = roomTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select a room first."
            );
            return;
        }

        try {

            LocalDate checkIn = LocalDate.parse(
                checkInField.getText().trim()
            );

            LocalDate checkOut = LocalDate.parse(
                checkOutField.getText().trim()
            );

            Room room = availableRooms.get(
                roomTable.convertRowIndexToModel(selectedRow)
            );

            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField phoneField = new JTextField();

            JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));

            form.add(new JLabel("Customer Name:"));
            form.add(nameField);

            form.add(new JLabel("Email Address:"));
            form.add(emailField);

            form.add(new JLabel("Phone Number:"));
            form.add(phoneField);

            int result = JOptionPane.showConfirmDialog(
                this, form, "Customer Details",
                JOptionPane.OK_CANCEL_OPTION
            );

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()
                    || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
                    || !phone.matches("[0-9]{10,15}")) {

                JOptionPane.showMessageDialog(
                    this, "Enter a valid name, email and phone number."
                );
                return;
            }

            long nights = java.time.temporal.ChronoUnit.DAYS.between(
                checkIn, checkOut
            );

            double total = nights * room.getPrice();

            if (!Payment.processPayment(this, total)) {
                JOptionPane.showMessageDialog(
                    this, "Payment cancelled. No booking was created."
                );
                return;
            }

            Customer customer = new Customer(name, email, phone);

            Reservation reservation = hotel.bookRoom(
                customer, room, checkIn, checkOut
            );

            if (reservation == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Sorry! This room is no longer available. Search again."
                );
                searchRooms();
                return;
            }

            JOptionPane.showMessageDialog(
                this,
                "BOOKING CONFIRMED!\n\n" +
                "Booking ID: " + reservation.getBookingId() +
                "\nCustomer: " + name +
                "\nRoom: " + room.getRoomNumber() +
                "\nCategory: " + room.getCategory() +
                "\nCheck-in: " + checkIn +
                "\nCheck-out: " + checkOut +
                "\nNights: " + nights +
                "\nTotal Amount: Rs. " +
                String.format("%.2f", total),
                "Booking Successful",
                JOptionPane.INFORMATION_MESSAGE
            );

            refreshBookings();
            searchRooms();

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                this, "Please enter valid booking dates."
            );
        }
    }

    private void refreshBookings() {

        bookingModel.setRowCount(0);

        for (Reservation r : hotel.getReservations()) {

            bookingModel.addRow(new Object[]{
                r.getBookingId(),
                r.getCustomer().getName(),
                r.getRoom().getRoomNumber(),
                r.getRoom().getCategory(),
                r.getCheckIn(),
                r.getCheckOut(),
                String.format("%.2f", r.getTotalAmount()),
                r.getStatus()
            });
        }
    }

    private void viewBookingDetails() {

        int row = bookingTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select a booking."
            );
            return;
        }

        String bookingId = bookingModel.getValueAt(
            bookingTable.convertRowIndexToModel(row), 0
        ).toString();

        for (Reservation r : hotel.getReservations()) {

            if (r.getBookingId().equals(bookingId)) {

                JOptionPane.showMessageDialog(
                    this,
                    "BOOKING DETAILS\n\n" +
                    "Booking ID: " + r.getBookingId() +
                    "\nCustomer: " + r.getCustomer().getName() +
                    "\nEmail: " + r.getCustomer().getEmail() +
                    "\nPhone: " + r.getCustomer().getPhone() +
                    "\nRoom Number: " + r.getRoom().getRoomNumber() +
                    "\nRoom Category: " + r.getRoom().getCategory() +
                    "\nPrice Per Night: Rs. " + r.getRoom().getPrice() +
                    "\nCheck-in: " + r.getCheckIn() +
                    "\nCheck-out: " + r.getCheckOut() +
                    "\nTotal Amount: Rs. " + r.getTotalAmount() +
                    "\nStatus: " + r.getStatus(),
                    "Reservation Details",
                    JOptionPane.INFORMATION_MESSAGE
                );

                return;
            }
        }
    }

    private void cancelSelectedBooking() {

        int row = bookingTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select a booking to cancel."
            );
            return;
        }

        String bookingId = bookingModel.getValueAt(
            bookingTable.convertRowIndexToModel(row), 0
        ).toString();

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to cancel booking " + bookingId + "?",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            boolean cancelled = hotel.cancelBooking(bookingId);

            if (cancelled) {
                JOptionPane.showMessageDialog(
                    this, "Booking cancelled successfully!"
                );
            } else {
                JOptionPane.showMessageDialog(
                    this, "Booking could not be cancelled."
                );
            }

            refreshBookings();
        }
    }

    private void styleButton(JButton button) {

        button.setBackground(darkBlue);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
    }

    private void styleTable(JTable table) {

        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );
        table.getTableHeader().setBackground(lightBlue);
        table.setSelectionBackground(new Color(190, 215, 240));
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
