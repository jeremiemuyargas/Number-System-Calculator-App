import javax.swing.Icon;
import java.awt.*;

/** A small filled circle, used as a color badge next to number-system names. */
public class ColorSwatchIcon implements Icon {
    private final Color color;
    private final int size;

    public ColorSwatchIcon(Color color, int size) {
        this.color = color;
        this.size = size;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fillOval(x, y + 2, size, size);
        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return size + 4;
    }

    @Override
    public int getIconHeight() {
        return size + 4;
    }
}
