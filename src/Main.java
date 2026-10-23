import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            applyLookAndFeel();
            CalculatorGUI gui = new CalculatorGUI();
            gui.setVisible(true);
        });
    }

    /** Switches to Nimbus (if available) and applies the app's dark color overrides. */
    private static void applyLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            // If Nimbus isn't available for some reason, fall back to the platform default.
        }
        Theme.installNimbusOverrides();
    }
}
