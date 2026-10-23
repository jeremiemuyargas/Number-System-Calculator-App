import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Font;

/**
 * Central place for every color and font used across the UI, so the whole
 * app shares one consistent, modern dark theme.
 */
public final class Theme {
    private Theme() {}

    // Backgrounds
    public static final Color BG_WINDOW = new Color(0x1E1E2E);
    public static final Color BG_PANEL = new Color(0x262637);
    public static final Color BG_DISPLAY = new Color(0x11111B);
    public static final Color BG_CARD = new Color(0x2E2E42);

    // Text
    public static final Color TEXT_LIGHT = new Color(0xE4E4F0);
    public static final Color TEXT_MUTED = new Color(0x9A9AB0);

    // Brand accents
    public static final Color ACCENT_PURPLE = new Color(0x8B5CF6);
    public static final Color ACCENT_PINK = new Color(0xEC4899);

    // Arithmetic operation colors
    public static final Color OP_ADD = new Color(0x34D399);
    public static final Color OP_SUB = new Color(0xF87171);
    public static final Color OP_MUL = new Color(0x60A5FA);
    public static final Color OP_DIV = new Color(0xFBBF24);

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_DISPLAY = new Font("Consolas", Font.BOLD, 28);
    public static final Font FONT_RESULT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_TAB = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_HISTORY = new Font("Consolas", Font.PLAIN, 12);

    /** Applies dark, high-contrast defaults to the Nimbus look-and-feel. */
    public static void installNimbusOverrides() {
        UIManager.put("control", BG_WINDOW);
        UIManager.put("info", BG_PANEL);
        UIManager.put("nimbusBase", new Color(0x2B2B40));
        UIManager.put("nimbusBlueGrey", new Color(0x3A3A55));
        UIManager.put("nimbusLightBackground", BG_DISPLAY);
        UIManager.put("text", TEXT_LIGHT);
        UIManager.put("nimbusSelectionBackground", ACCENT_PURPLE);
        UIManager.put("nimbusSelectedText", Color.WHITE);
        UIManager.put("nimbusFocus", ACCENT_PINK);
        UIManager.put("List.background", BG_DISPLAY);
        UIManager.put("List.foreground", TEXT_LIGHT);
    }
}
