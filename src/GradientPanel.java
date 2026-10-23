import javax.swing.*;
import java.awt.*;

/** A panel that fills itself with a diagonal gradient instead of a flat color. */
public class GradientPanel extends JPanel {
    private final Color colorStart;
    private final Color colorEnd;

    public GradientPanel(Color colorStart, Color colorEnd) {
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, colorStart, getWidth(), getHeight(), colorEnd);
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}
