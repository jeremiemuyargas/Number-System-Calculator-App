import java.awt.Color;

/**
 * Represents the number systems supported by the calculator.
 * Each system knows its radix (base), display label, a short badge code,
 * and a signature color used to visually distinguish it throughout the UI.
 */
public enum NumberSystem {
    BINARY(2, "Binary", "BIN", new Color(0x3DDC97)),
    OCTAL(8, "Octal", "OCT", new Color(0x38BDF8)),
    DECIMAL(10, "Decimal", "DEC", new Color(0xA78BFA)),
    HEXADECIMAL(16, "Hexadecimal", "HEX", new Color(0xFB923C));

    private final int radix;
    private final String label;
    private final String shortCode;
    private final Color color;

    NumberSystem(int radix, String label, String shortCode, Color color) {
        this.radix = radix;
        this.label = label;
        this.shortCode = shortCode;
        this.color = color;
    }

    public int getRadix() {
        return radix;
    }

    public String getShortCode() {
        return shortCode;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return label;
    }
}
