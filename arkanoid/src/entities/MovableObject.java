package entities;

/**
 * Abstract class for movable game objects
 * Demonstrates: Inheritance (extends entities.GameObject) and Abstraction
 */
public abstract class MovableObject extends GameObject {
    // Encapsulation: private fields for velocity
    private double velocityX;
    private double velocityY;
    private double speed;
    
    /**
     * Constructor for movable objects
     */
    public MovableObject(double x, double y, int width, int height, double speed) {
        super(x, y, width, height);
        this.speed = speed;
        this.velocityX = 0;
        this.velocityY = 0;
    }
    
    /**
     * Update position based on velocity
     * Can be overridden by subclasses for custom movement
     */
    @Override
    public void update() {
        setX(getX() + velocityX);
        setY(getY() + velocityY);
    }
    
    /**
     * Move the object by delta amounts
     */
    protected void move(double dx, double dy) {
        setX(getX() + dx);
        setY(getY() + dy);
    }
    
    /**
     * Set velocity in both directions
     */
    protected void setVelocity(double vx, double vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }
    
    /**
     * Stop all movement
     */
    protected void stop() {
        this.velocityX = 0;
        this.velocityY = 0;
    }
    
    // Encapsulation: Getters and Setters for velocity
    public double getVelocityX() {
        return velocityX;
    }
    
    protected void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }
    
    public double getVelocityY() {
        return velocityY;
    }
    
    protected void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
    
    public double getSpeed() {
        return speed;
    }
    
    protected void setSpeed(double speed) {
        this.speed = speed;
    }
    
    /**
     * Check if object is moving
     */
    public boolean isMoving() {
        return velocityX != 0 || velocityY != 0;
    }
}
