import javax.swing.*;
import java.awt.*;

/**
 * "Arithmetic" tab. Lets the user pick a number system with colored pill
 * buttons, enter two numbers in that system, and run +, -, x, / with big,
 * distinctly colored operation buttons. Keeps a scrolling history below.
 */
public class ArithmeticPanel extends JPanel {
    private final JTextField field1;
    private final JTextField field2;
    private final JLabel resultLabel;
    private final JLabel systemBadge;
    private final DefaultListModel<String> historyModel = new DefaultListModel<>();
    private NumberSystem currentSystem = NumberSystem.DECIMAL;

    public ArithmeticPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(Theme.BG_WINDOW);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Theme.BG_WINDOW);

        systemBadge = new JLabel("Number system: DECIMAL");
        systemBadge.setFont(Theme.FONT_LABEL.deriveFont(Font.BOLD, 13f));
        systemBadge.setForeground(currentSystem.getColor());
        systemBadge.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(systemBadge);
        topPanel.add(Box.createVerticalStrut(8));
        topPanel.add(buildSystemPicker());
        topPanel.add(Box.createVerticalStrut(14));

        field1 = buildInputField();
        field2 = buildInputField();
        topPanel.add(buildLabeledField("FIRST NUMBER", field1));
        topPanel.add(Box.createVerticalStrut(8));
        topPanel.add(buildLabeledField("SECOND NUMBER", field2));

        add(topPanel, BorderLayout.NORTH);
        add(buildOperationGrid(), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setBackground(Theme.BG_WINDOW);

        JPanel resultCard = new JPanel(new BorderLayout());
        resultCard.setBackground(Theme.BG_DISPLAY);
        resultCard.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        resultLabel = new JLabel("Result will appear here.");
        resultLabel.setFont(Theme.FONT_RESULT);
        resultLabel.setForeground(Theme.TEXT_MUTED);
        resultCard.add(resultLabel, BorderLayout.CENTER);
        bottom.add(resultCard, BorderLayout.NORTH);
        bottom.add(buildHistoryPanel(), BorderLayout.CENTER);

        add(bottom, BorderLayout.SOUTH);
    }

    private JTextField buildInputField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Consolas", Font.BOLD, 18));
        field.setBackground(Theme.BG_DISPLAY);
        field.setForeground(Theme.TEXT_LIGHT);
        field.setCaretColor(Theme.TEXT_LIGHT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BG_CARD, 2),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return field;
    }

    private JPanel buildLabeledField(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setBackground(Theme.BG_WINDOW);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel label = new JLabel(labelText);
        label.setFont(Theme.FONT_LABEL);
        label.setForeground(Theme.TEXT_MUTED);
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildSystemPicker() {
        JPanel picker = new JPanel(new GridLayout(1, 4, 8, 8));
        picker.setBackground(Theme.BG_WINDOW);
        picker.setAlignmentX(Component.LEFT_ALIGNMENT);
        picker.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        for (NumberSystem system : NumberSystem.values()) {
            RoundedButton btn = new RoundedButton(system.getShortCode(), dim(system.getColor()));
            btn.putClientProperty("system", system);
            btn.addActionListener(e -> selectSystem(system, picker));
            picker.add(btn);
        }
        highlightSelected(picker);
        return picker;
    }

    private void selectSystem(NumberSystem system, JPanel picker) {
        currentSystem = system;
        systemBadge.setText("Number system: " + system.toString().toUpperCase());
        systemBadge.setForeground(system.getColor());
        highlightSelected(picker);
    }

    private void highlightSelected(JPanel picker) {
        for (Component c : picker.getComponents()) {
            if (c instanceof RoundedButton) {
                RoundedButton btn = (RoundedButton) c;
                NumberSystem system = (NumberSystem) btn.getClientProperty("system");
                boolean selected = system == currentSystem;
                btn.setBaseColor(selected ? system.getColor() : dim(system.getColor()));
            }
        }
    }

    private static Color dim(Color c) {
        return new Color(c.getRed() / 3, c.getGreen() / 3, c.getBlue() / 3);
    }

    private JPanel buildOperationGrid() {
        JPanel opPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        opPanel.setBackground(Theme.BG_WINDOW);
        opPanel.setBorder(BorderFactory.createEmptyBorder(14, 0, 14, 0));

        RoundedButton addBtn = new RoundedButton("+", Theme.OP_ADD);
        RoundedButton subBtn = new RoundedButton("\u2212", Theme.OP_SUB);
        RoundedButton mulBtn = new RoundedButton("\u00D7", Theme.OP_MUL);
        RoundedButton divBtn = new RoundedButton("\u00F7", Theme.OP_DIV);

        for (RoundedButton b : new RoundedButton[]{addBtn, subBtn, mulBtn, divBtn}) {
            b.setFont(new Font("Segoe UI", Font.BOLD, 24));
        }

        addBtn.addActionListener(e -> compute('+', Theme.OP_ADD));
        subBtn.addActionListener(e -> compute('-', Theme.OP_SUB));
        mulBtn.addActionListener(e -> compute('*', Theme.OP_MUL));
        divBtn.addActionListener(e -> compute('/', Theme.OP_DIV));

        opPanel.add(addBtn);
        opPanel.add(subBtn);
        opPanel.add(mulBtn);
        opPanel.add(divBtn);
        return opPanel;
    }

    private JPanel buildHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setBackground(Theme.BG_WINDOW);

        JLabel title = new JLabel("HISTORY");
        title.setFont(Theme.FONT_LABEL);
        title.setForeground(Theme.TEXT_MUTED);
        panel.add(title, BorderLayout.NORTH);

        JList<String> historyList = new JList<>(historyModel);
        historyList.setFont(Theme.FONT_HISTORY);
        historyList.setBackground(Theme.BG_DISPLAY);
        historyList.setForeground(Theme.TEXT_LIGHT);
        historyList.setFixedCellHeight(20);
        historyList.setSelectionBackground(Theme.ACCENT_PURPLE);

        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setPreferredSize(new Dimension(10, 90));
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BG_CARD));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void compute(char op, Color opColor) {
        String a = field1.getText().trim();
        String b = field2.getText().trim();
        try {
            String result;
            String opSymbol;
            switch (op) {
                case '+':
                    result = Converter.add(a, b, currentSystem);
                    opSymbol = "+";
                    break;
                case '-':
                    result = Converter.subtract(a, b, currentSystem);
                    opSymbol = "\u2212";
                    break;
                case '*':
                    result = Converter.multiply(a, b, currentSystem);
                    opSymbol = "\u00D7";
                    break;
                case '/':
                    result = Converter.divide(a, b, currentSystem);
                    opSymbol = "\u00F7";
                    break;
                default:
                    throw new IllegalStateException("Unknown operation");
            }
            String line = a.toUpperCase() + " " + opSymbol + " " + b.toUpperCase() + " = " + result;
            resultLabel.setText(line + "  (" + currentSystem.getShortCode() + ")");
            resultLabel.setForeground(opColor);
            historyModel.add(0, line + " [" + currentSystem.getShortCode() + "]");
        } catch (InvalidDigitException | ArithmeticException ex) {
            resultLabel.setText("Error: " + ex.getMessage());
            resultLabel.setForeground(Theme.OP_SUB);
        }
    }
}
