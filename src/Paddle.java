import java.awt.*;
public class Paddle {
    private int x,y;
    private final int WIDTH = 100;
    private final int HEIGHT = 10;
    private final int SPEED = 12; // Tăng tốc độ từ 6 lên 12
    public Paddle(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void moveLeft() {
        x -= SPEED;
        if(x<0)x=0;
    }
    public void moveRight(int boardWidth) {
        x+=SPEED;
        if(x>boardWidth-WIDTH)x=boardWidth-WIDTH;
    }
    public  void draw(Graphics g) {
        g.setColor(Color.pink);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return WIDTH;}
    public int getHeight() {return HEIGHT;}
    public void setPosition(int newX) {
        this.x = newX;
    }
}
