import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
// Chú thích: Lớp Brick kế thừa từ GameObject, đại diện cho viên gạch trong trò chơi.
public class Brick extends GameObject {
    private static final int BRICK_WIDTH = 70;
    private static final int BRICK_HEIGHT = 20;
    private boolean isVisible;

    public Brick(int x, int y) {
        super(x, y, BRICK_WIDTH, BRICK_HEIGHT);
        this.isVisible = true;
    }
// GHI đè (Override) phương thức 'draw' từ GameObject để vẽ viên gạch.
    @Override
    public void draw(Graphics g) {
        if (isVisible) {
            g.setColor(Color.ORANGE);
            g.fillRect(x, y, width, height);

            // Adding a border to the brick for better visibility
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
}

