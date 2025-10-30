import java.awt.Color;
import java.awt.Graphics;

public class Laser extends GameObject {
    private static final int LASER_SPEED = 5;
    private static final int LASER_WIDTH = 3;
    private static final int LASER_HEIGHT = 10;

    public Laser(int x, int y, boolean goingUp) {
        super(x, y, LASER_WIDTH, LASER_HEIGHT);
        this.dy = goingUp ? -LASER_SPEED : LASER_SPEED;
    }

    private int dy;

    public void move() {
        y += dy;
    }

    public boolean isOffScreen() {
        return y < 0;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);  // Đổi màu laser từ RED sang BLUE
        g.fillRect(x, y, width, height);
    }
}
