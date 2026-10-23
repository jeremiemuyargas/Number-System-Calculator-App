import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * "Convert" tab. Lets the user tap out a number on a colorful digit pad,
 * pick the From/To number systems, and see the converted result update
 * live as they type. A running history of past conversions is kept below.
 */
public class ConvertPanel extends JPanel {
    private final JLabel inputDisplay;
    private final JLabel resultLabel;
    // These are initialized while building the selector row.
    private JComboBox<NumberSystem> fromBox;
    private JComboBox<NumberSystem> toBox;
    private final Map<Character, RoundedButton> digitButtons = new LinkedHashMap<>();
    private final DefaultListModel<String> historyModel = new DefaultListModel<>();

    private final StringBuilder input = new StringBuilder();

    public ConvertPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(Theme.BG_WINDOW);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // ---- Digital display (input on top, live result underneath) ----
        JPanel displayCard = new JPanel();
        displayCard.setLayout(new BoxLayout(displayCard, BoxLayout.Y_AXIS));
        displayCard.setBackground(Theme.BG_DISPLAY);
        displayCard.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        inputDisplay = new JLabel(" ");
        inputDisplay.setFont(Theme.FONT_DISPLAY);
        inputDisplay.setForeground(Theme.TEXT_LIGHT);
        inputDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);

        resultLabel = new JLabel("Result will appear here as you type.");
        resultLabel.setFont(Theme.FONT_RESULT);
        resultLabel.setForeground(Theme.TEXT_MUTED);
        resultLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        displayCard.add(inputDisplay);
        displayCard.add(Box.createVerticalStrut(6));
        displayCard.add(resultLabel);
        add(displayCard, BorderLayout.NORTH);

        // ---- Center: system pickers + digit pad + controls ----
        JPanel centerPanel = new JPanel(new BorderLayout(12, 12));
        centerPanel.setBackground(Theme.BG_WINDOW);

        centerPanel.add(buildSystemSelectorRow(), BorderLayout.NORTH);
        centerPanel.add(buildDigitPad(), BorderLayout.CENTER);
        centerPanel.add(buildControlsRow(), BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // ---- History ----
        add(buildHistoryPanel(), BorderLayout.SOUTH);

        updateDigitAvailability();
    }

    private JPanel buildSystemSelectorRow() {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(Theme.BG_WINDOW);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 4, 0, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        fromBox = buildSystemComboBox();
        toBox = buildSystemComboBox();
        toBox.setSelectedItem(NumberSystem.BINARY);

        JLabel fromLabel = new JLabel("FROM");
        fromLabel.setFont(Theme.FONT_LABEL);
        fromLabel.setForeground(Theme.TEXT_MUTED);

        JLabel toLabel = new JLabel("TO");
        toLabel.setFont(Theme.FONT_LABEL);
        toLabel.setForeground(Theme.TEXT_MUTED);

        RoundedButton swapButton = new RoundedButton("\u21C4", Theme.ACCENT_PURPLE);
        swapButton.setPreferredSize(new Dimension(46, 34));
        swapButton.setToolTipText("Swap From and To");
        swapButton.addActionListener(e -> {
            NumberSystem from = (NumberSystem) fromBox.getSelectedItem();
            NumberSystem to = (NumberSystem) toBox.getSelectedItem();
            fromBox.setSelectedItem(to);
            toBox.setSelectedItem(from);
        });

        JPanel fromCol = new JPanel(new BorderLayout(2, 2));
        fromCol.setBackground(Theme.BG_WINDOW);
        fromCol.add(fromLabel, BorderLayout.NORTH);
        fromCol.add(fromBox, BorderLayout.CENTER);

        JPanel toCol = new JPanel(new BorderLayout(2, 2));
        toCol.setBackground(Theme.BG_WINDOW);
        toCol.add(toLabel, BorderLayout.NORTH);
        toCol.add(toBox, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.weightx = 1;
        row.add(fromCol, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.insets = new Insets(14, 4, 0, 4);
        row.add(swapButton, gbc);

        gbc.gridx = 2;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 4, 0, 4);
        row.add(toCol, gbc);

        fromBox.addActionListener(e -> {
            updateDigitAvailability();
            recalculateLive();
        });
        toBox.addActionListener(e -> recalculateLive());

        return row;
    }

    private JComboBox<NumberSystem> buildSystemComboBox() {
        JComboBox<NumberSystem> box = new JComboBox<>(NumberSystem.values());
        box.setFont(Theme.FONT_LABEL.deriveFont(Font.BOLD, 14f));
        box.setBackground(Theme.BG_CARD);
        box.setForeground(Theme.TEXT_LIGHT);
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                if (value instanceof NumberSystem) {
                    NumberSystem system = (NumberSystem) value;
                    label.setIcon(new ColorSwatchIcon(system.getColor(), 10));
                    label.setText(system.toString() + "  (" + system.getShortCode() + ")");
                }
                label.setBackground(isSelected ? Theme.ACCENT_PURPLE : Theme.BG_CARD);
                label.setForeground(Color.WHITE);
                label.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
                return label;
            }
        });
        return box;
    }

    private JPanel buildDigitPad() {
        JPanel digitPanel = new JPanel(new GridLayout(4, 4, 8, 8));
        digitPanel.setBackground(Theme.BG_WINDOW);

        // Numeric digits and hex letters share the pad but get distinct colors,
        // so it's visually obvious which keys belong to the extended hex range.
        String[] order = {"7", "8", "9", "A", "4", "5", "6", "B", "1", "2", "3", "C", "0", "D", "E", "F"};
        for (String d : order) {
            char digit = d.charAt(0);
            boolean isHexLetter = Character.isLetter(digit);
            Color color = isHexLetter ? new Color(0x475569) : new Color(0x374151);
            RoundedButton btn = new RoundedButton(d, color);
            btn.addActionListener(e -> {
                input.append(btn.getText());
                refreshDisplay();
                recalculateLive();
            });
            digitButtons.put(digit, btn);
            digitPanel.add(btn);
        }
        return digitPanel;
    }

    private JPanel buildControlsRow() {
        JPanel controlPanel = new JPanel(new GridLayout(1, 3, 8, 8));
        controlPanel.setBackground(Theme.BG_WINDOW);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        RoundedButton clearBtn = new RoundedButton("C", Theme.OP_SUB);
        RoundedButton delBtn = new RoundedButton("\u232B Del", new Color(0x4B5563));
        RoundedButton logBtn = new RoundedButton("\u2795 Log Result", Theme.ACCENT_PURPLE);

        clearBtn.addActionListener(e -> {
            input.setLength(0);
            refreshDisplay();
            resultLabel.setText("Result will appear here as you type.");
            resultLabel.setForeground(Theme.TEXT_MUTED);
        });
        delBtn.addActionListener(e -> {
            if (input.length() > 0) {
                input.deleteCharAt(input.length() - 1);
                refreshDisplay();
                recalculateLive();
            }
        });
        logBtn.addActionListener(e -> logCurrentConversion());

        controlPanel.add(clearBtn);
        controlPanel.add(delBtn);
        controlPanel.add(logBtn);
        return controlPanel;
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

    /** Greys out digit buttons that aren't valid for the currently selected "From" system. */
    private void updateDigitAvailability() {
        NumberSystem from = (NumberSystem) fromBox.getSelectedItem();
        int radix = from.getRadix();
        for (Map.Entry<Character, RoundedButton> entry : digitButtons.entrySet()) {
            int digitValue = Character.digit(entry.getKey(), 16);
            entry.getValue().setEnabled(digitValue < radix);
        }
    }

    private void refreshDisplay() {
        inputDisplay.setText(input.length() == 0 ? " " : input.toString());
    }

    /** Recomputes the live preview result every time the input or systems change. */
    private void recalculateLive() {
        if (input.length() == 0) {
            resultLabel.setText("Result will appear here as you type.");
            resultLabel.setForeground(Theme.TEXT_MUTED);
            return;
        }
        NumberSystem from = (NumberSystem) fromBox.getSelectedItem();
        NumberSystem to = (NumberSystem) toBox.getSelectedItem();
        try {
            String result = Converter.convert(input.toString(), from, to);
            resultLabel.setText("= " + result + "  (" + to.getShortCode() + ")");
            resultLabel.setForeground(to.getColor());
        } catch (InvalidDigitException ex) {
            resultLabel.setText("Waiting for a valid " + from + " number\u2026");
            resultLabel.setForeground(Theme.TEXT_MUTED);
        }
    }

    private void logCurrentConversion() {
        NumberSystem from = (NumberSystem) fromBox.getSelectedItem();
        NumberSystem to = (NumberSystem) toBox.getSelectedItem();
        try {
            String result = Converter.convert(input.toString(), from, to);
            String entry = input.toString().toUpperCase() + " (" + from.getShortCode() + ")  \u2192  "
                    + result + " (" + to.getShortCode() + ")";
            historyModel.add(0, entry);
            copyToClipboard(result);
            resultLabel.setText("= " + result + "  (" + to.getShortCode() + ")  \u2713 copied & logged");
            resultLabel.setForeground(to.getColor());
        } catch (InvalidDigitException ex) {
            resultLabel.setText("Error: " + ex.getMessage());
            resultLabel.setForeground(Theme.OP_SUB);
        }
    }

    private void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
    }
}
