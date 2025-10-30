import java.awt.*;

/**
 * Abstract base class for all game objects
 * Demonstrates: Abstraction and Encapsulation
 */
public abstract class GameObject {
    // Encapsulation: private fields
    private double x;
    private double y;
    private int width;
    private int height;
    
    /**
     * Constructor with position and dimensions
     */
    public GameObject(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    /**
     * Abstract method to update object state
     * Polymorphism: Each subclass implements differently
     */
    public abstract void update();
    
    /**
     * Abstract method to render the object
     * Polymorphism: Each subclass implements differently
     */
    public abstract void render(Graphics2D g2d);
    
    /**
     * Check collision with another GameObject
     */
    public boolean intersects(GameObject other) {
        return this.getBounds().intersects(other.getBounds());
    }
    
    /**
     * Check collision with a Rectangle
     */
    public boolean intersects(Rectangle rect) {
        return this.getBounds().intersects(rect);
    }
    
    /**
     * Get bounding rectangle for collision detection
     */
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
    
    // Encapsulation: Getters and Setters
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public int getWidth() {
        return width;
    }
    
    protected void setWidth(int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return height;
    }
    
    protected void setHeight(int height) {
        this.height = height;
    }
    
    /**
     * Move object to new position
     */
    protected void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
