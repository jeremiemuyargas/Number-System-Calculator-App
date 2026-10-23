import javax.swing.*;
import java.awt.*;

/**
 * Main application window. Shows a gradient header banner up top,
 * then the Convert and Arithmetic tabs below it.
 */
public class CalculatorGUI extends JFrame {
    public CalculatorGUI() {
        setTitle("Number System Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(540, 720);
        setMinimumSize(new Dimension(480, 620));
        setLocationRelativeTo(null); // center the window on screen

        getContentPane().setBackground(Theme.BG_WINDOW);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabs(), BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        GradientPanel header = new GradientPanel(Theme.ACCENT_PURPLE, Theme.ACCENT_PINK);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("\uD83D\uDD22 Number System Calculator");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Convert and calculate across Binary \u00B7 Octal \u00B7 Decimal \u00B7 Hex");
        subtitle.setFont(Theme.FONT_SUBTITLE);
        subtitle.setForeground(new Color(255, 255, 255, 220));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        return header;
    }

    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(Theme.FONT_TAB);
        tabs.setBackground(Theme.BG_PANEL);
        tabs.setForeground(Theme.TEXT_LIGHT);
        tabs.addTab("\uD83D\uDD01  Convert", new ConvertPanel());
        tabs.addTab("\u2795  Arithmetic", new ArithmeticPanel());
        return tabs;
    }
}
