import java.awt.Color;
import java.awt.Graphics;
//  Lớp Ball kế thừa từ GameObject (Inheritance).
// Lớp này giờ đây đóng gói (Encapsulation) tất cả logic và dữ liệu liên quan đến quả bóng.
public class Ball extends GameObject {
    private int dx;
    private int dy;
    private static final int BALL_SIZE = 20;

    public Ball(int x, int y, int dx, int dy) {
        super(x, y, BALL_SIZE, BALL_SIZE);
        this.dx = dx;
        this.dy = dy;
    }

    // Chú thích: Phương thức đóng gói logic di chuyển của Ball.
    // Lớp Board (GameManager) chỉ cần gọi ball.move(), không cần biết bên trong làm gì.
    public void move() {
        x += dx;
        y += dy;
    }

    // Đây là cách Ball tự vẽ chính nó.
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillOval(x, y, width, height);
    }

    public void reverseDX() {
        dx = -dx;
    }

    public void reverseDY() {
        dy = -dy;
    }

    public int getDX() {
        return dx;
    }

    public void setDX(int dx) {
        this.dx = dx;
    }

    public int getDY() {
        return dy;
    }

    // Chú thích: Dùng để đặt lại vị trí bóng (ví dụ: khi mất mạng).
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}