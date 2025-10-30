import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Chú thích: Lớp Paddle giờ cũng kế thừa từ GameObject.
public class Paddle extends GameObject {

    // Chú thích: Các hằng số (constants) định nghĩa thuộc tính của Paddle.
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 10;
    private final int SPEED = 12;

    private boolean hasLaser = false;
    private long laserStartTime;
    private static final long LASER_DURATION = 3000; // 3 giây
    private List<Laser> activeLasers = new ArrayList<>();

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

    public void activateLaser() {
        hasLaser = true;
        laserStartTime = System.currentTimeMillis();
    }

    public void updateLasers() {
        if (hasLaser) {
            if (System.currentTimeMillis() - laserStartTime > LASER_DURATION) {
                hasLaser = false;
                activeLasers.clear();
            }
        }

        // Update existing lasers
        Iterator<Laser> iterator = activeLasers.iterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.move();
            if (laser.isOffScreen()) {
                iterator.remove();
            }
        }
    }

    // Sửa phương thức shoot() để bắn laser từ 2 đầu paddle
    public void shoot() {
        if (hasLaser) {
            // Bắn từ hai đầu paddle
            activeLasers.add(new Laser(x, y, true));
            activeLasers.add(new Laser(x + width - 5, y, true));
        }
    }

    // Chú thích: Ghi đè (Override) phương thức 'draw' từ GameObject.
    @Override
    public void draw(Graphics g) {
        // Đổi màu paddle khi có laser
        g.setColor(hasLaser ? Color.BLUE : Color.DARK_GRAY);  // Đổi màu từ GREEN sang BLUE
        g.fillRect(x, y, width, height);

        // Vẽ các laser đang hoạt động
        for (Laser laser : activeLasers) {
            laser.draw(g);
        }
    }

    public void setPosition(int newX) {
        this.x = newX; //đây là di chuyển theo chuột sau này update di chuyển theo bàn phím
    }

    public List<Laser> getActiveLasers() {
        return activeLasers;
    }

    public boolean hasLaser() {
        return hasLaser;
    }
}