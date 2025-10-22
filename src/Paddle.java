import java.awt.*;

// Chú thích: Lớp Paddle giờ cũng kế thừa từ GameObject.
public class Paddle extends GameObject {

    // Chú thích: Các hằng số (constants) định nghĩa thuộc tính của Paddle.
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 10;
    private final int SPEED = 12;

    public Paddle(int x, int y) {
        // Chú thích: Gọi constructor của lớp cha (GameObject) để gán vị trí và kích thước.
        super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
    }

    public void moveLeft() {
        x -= SPEED;
        if (x < 0) x = 0;
    }
    public void moveRight(int boardWidth) {
        x += SPEED;
        if (x > boardWidth - width) x = boardWidth - width; // 'width' giờ là thuộc tính kế thừa
    }

    // Chú thích: Ghi đè (Override) phương thức 'draw' từ GameObject.
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.pink);
        g.fillRect(x, y, width, height);
    }

    public void setPosition(int newX) {
        this.x = newX; //đây là di chuyển theo chuột sau này update di chuyển theo bàn phím
    }
}