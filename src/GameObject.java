import java.awt.Graphics;
import java.awt.Rectangle;

// Tạo lớp cơ sở (base class) trừu tượng cho TẤT CẢ các đối tượng trong game nhé nhé nhé???
// Áp dụng tính Trừu tượng (Abstraction) và Kế thừa (Inheritance). (Tuần 5)
public abstract class GameObject {

    //  'protected' để các lớp con (như Paddle, Ball)

    protected int x, y;
    protected int width, height;

    // Chú thích: Constructor để các lớp con gọi tới.
    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    //Phương thức chung để kiểm tra va chạm.

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    public abstract void draw(Graphics g);
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}