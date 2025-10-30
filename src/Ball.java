import java.awt.Color;
import java.awt.Graphics;
//  Lớp Ball kế thừa từ GameObject (Inheritance).
// Lớp này giờ đây đóng gói (Encapsulation) tất cả logic và dữ liệu liên quan đến quả bóng.
public class Ball extends GameObject {
    private int dx;
    private int dy;
    private static final int BALL_SIZE = 20;
    private boolean isFireball = false;
    private long fireballStartTime; // Thêm biến để theo dõi thời gian
    private static final long FIREBALL_DURATION = 4000; // 4 seconds in milliseconds

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
        if (isFireball) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }
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

    public void setFireball(boolean active) {
        this.isFireball = active;
        if (active) {
            fireballStartTime = System.currentTimeMillis();
        }
    }

    public void updateFireballStatus() {
        if (isFireball && System.currentTimeMillis() - fireballStartTime > FIREBALL_DURATION) {
            isFireball = false;
        }
    }

    public boolean isFireball() {
        return this.isFireball;
    }

    // =======================================================
    // === HÀM ĐƯỢC THÊM VÀO ĐỂ SỬA LỖI ===
    /**
     * Trả về kích cỡ của bóng để Board.java có thể tính toán
     */
    public static int getBallSize() {
        return BALL_SIZE;
    }
    // =======================================================
}