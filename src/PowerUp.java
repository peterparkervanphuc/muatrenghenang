import java.awt.Color;
import java.awt.Graphics;

public class PowerUp extends GameObject {
    private static final int FALLING_SPEED = 3;
    private static final int POWERUP_WIDTH = 30;
    private static final int POWERUP_HEIGHT = 15;
    private BrickType type;

    public PowerUp(int x, int y, BrickType type) {
        super(x, y, POWERUP_WIDTH, POWERUP_HEIGHT);
        if (!type.hasPowerUp()) {
            throw new IllegalArgumentException("BrickType không phải là PowerUp!");
        }
        this.type = type;
    }

    public void move() {
        y += FALLING_SPEED;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(type.getColor());
        g.fillRect(x, y, width, height);
    }

    public BrickType getType() {
        return type;
    }

    public boolean isOffScreen(int screenHeight) {
        return y > screenHeight;
    }
}

