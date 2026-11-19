
import java.awt.Component;
import javax.swing.JOptionPane;

public class Payment {

    public static boolean processPayment(Component parent, double amount) {

        int choice = JOptionPane.showConfirmDialog(
            parent,
            "Total Amount: Rs. " + String.format("%.2f", amount)
            + "\n\nProceed with simulated payment?",
            "Payment Gateway",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(
                parent,
                "Payment Successful!\nYour booking is confirmed.",
                "Payment Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            return true;
        }

        return false;
    }
}
