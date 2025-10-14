import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

public class Brick {
    private int x, y;
    private static final int WIDTH = 70;
    private static final int HEIGHT = 20;
    private boolean isVisible;

    public Brick(int x, int y) {
        this.x = x;
        this.y = y;
        this.isVisible = true;
    }

    public void draw(Graphics g) {
        if (isVisible) {
            g.setColor(Color.ORANGE);
            g.fillRect(x, y, WIDTH, HEIGHT);

            // Adding a border to the brick for better visibility
            g.setColor(Color.BLACK);
            g.drawRect(x, y, WIDTH, HEIGHT);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
}
