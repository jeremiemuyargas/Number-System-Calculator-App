import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A flat, rounded-corner button with a colored fill and a hover highlight.
 * Used for the digit pad, operation buttons, and system-picker buttons.
 */
public class RoundedButton extends JButton {
    private Color baseColor;
    private Color hoverColor;
    private boolean hovering = false;

    public RoundedButton(String text, Color baseColor) {
        super(text);
        setBaseColor(baseColor);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(Theme.FONT_BUTTON);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                repaint();
            }
        });
    }

    public final void setBaseColor(Color color) {
        this.baseColor = color;
        this.hoverColor = brighten(color, 0.18f);
        repaint();
    }

    private static Color brighten(Color c, float amount) {
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        hsb[2] = Math.min(1f, hsb[2] + amount);
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color fill = isEnabled() ? (hovering ? hoverColor : baseColor) : new Color(0x3A3A46);
        g2.setColor(fill);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public boolean isOpaque() {
        return false;
    }
}
