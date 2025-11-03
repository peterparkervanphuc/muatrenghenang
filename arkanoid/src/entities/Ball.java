package entities;

import core.GameBounds;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Represents the ball in the Arkanoid game
 * OOP Principles Applied:
 * - Inheritance: Extends entities.MovableObject for position and velocity
 * - Encapsulation: Private fields with controlled access
 * - Polymorphism: Overrides update() and render() methods
 * - Abstraction: Hides complex physics and collision logic
 */
public class Ball extends MovableObject {
    // Encapsulation: Private constants
    private static final int BALL_SIZE = 16; // diameter (radius * 2)
    private static final double INITIAL_SPEED = 5.0;
    
    // Encapsulation: Private fields
    private int radius = 8;
    private boolean attached = false;
    private Paddle attachedPaddle;
    private BufferedImage ballImage;
    
    private double speedMultiplier = 1.0;
    private double levelSpeedBonus = 0.0;
    
    /**
     * Constructor: Create ball at position
     */
    public Ball(double x, double y) {
        super(x, y, BALL_SIZE, BALL_SIZE, INITIAL_SPEED);
        setVelocity(0, 0); // Start stationary
        loadImage();
    }
    
    /**
     * Constructor with level-based speed bonus
     */
    public Ball(double x, double y, int level) {
        this(x, y);
        // Increase speed by 9% per level (level 1 = 100%, level 2 = 109%, level 3 = 118%, etc.)
        this.levelSpeedBonus = (level - 1) * 0.09;
    }
    
    private void loadImage() {
        try {
            var ballStream = getClass().getClassLoader().getResourceAsStream("Sprites/ball.png");
            if (ballStream != null) {
                ballImage = ImageIO.read(ballStream);
                ballStream.close();
            }
        } catch (Exception e) {
            System.err.println("Could not load ball image: " + e.getMessage());
        }
    }
    
    public void attachToPaddle(Paddle paddle) {
        this.attached = true;
        this.attachedPaddle = paddle;
        setVelocity(0, 0); // Stop movement when attached
    }
    
    public void launch() {
        if (attached) {
            attached = false;
            // Random angle between -45 and 45 degrees
            double angle = Math.toRadians(-90 + (Math.random() * 30 - 15));
            double totalSpeed = INITIAL_SPEED * (1.0 + levelSpeedBonus) * speedMultiplier;
            setVelocity(totalSpeed * Math.cos(angle), totalSpeed * Math.sin(angle));
            attachedPaddle = null;
        }
    }
    
    /**
     * Override update to handle attachment and collision with walls
     * Polymorphism: Custom update logic for entities.Ball
     */
    @Override
    public void update() {
        if (attached && attachedPaddle != null) {
            // Follow paddle when attached
            setX(attachedPaddle.getX() + attachedPaddle.getWidth() / 2);
            setY(attachedPaddle.getY() - radius - 2);
        } else {
            // Use inherited movement from entities.MovableObject
            super.update(); // Move ball with velocity
            
            // Bounce off left wall (with border consideration)
            if (getX() - radius < GameBounds.PLAY_LEFT) {
                setX(GameBounds.PLAY_LEFT + radius);
                setVelocityX(Math.abs(getVelocityX()));
            }
            // Bounce off right wall (with border consideration)
            if (getX() + radius > GameBounds.PLAY_RIGHT) {
                setX(GameBounds.PLAY_RIGHT - radius);
                setVelocityX(-Math.abs(getVelocityX()));
            }
            
            // Bounce off ceiling (with border consideration)
            if (getY() - radius < GameBounds.PLAY_TOP) {
                setY(GameBounds.PLAY_TOP + radius);
                setVelocityY(Math.abs(getVelocityY()));
            }
        }
    }
    
    /**
     * Legacy update method for compatibility with ui.GamePanel
     * Delegates to the new update() method
     */
    public void update(Paddle paddle) {
        update(); // Call the overridden update()
    }
    
    public void bounceOffPaddle(Paddle paddle) {
        if (getVelocityY() > 0) { // Only bounce if moving downward
            setVelocityY(-getVelocityY());
            
            // Adjust angle based on where it hit the paddle
            double hitPos = (getX() - paddle.getX()) / paddle.getWidth(); // 0 to 1
            double angle = (hitPos - 0.5) * 60; // -30 to 30 degrees
            
            double speed = Math.sqrt(getVelocityX() * getVelocityX() + getVelocityY() * getVelocityY());
            setVelocityX(speed * Math.sin(Math.toRadians(angle)));
            setVelocityY(-speed * Math.cos(Math.toRadians(angle)));
            
            // Ensure minimum vertical speed
            if (Math.abs(getVelocityY()) < 2) {
                setVelocityY(getVelocityY() < 0 ? -2 : 2);
            }
        }
    }
    
    public void bounceOffBrick(Brick brick) {
        Rectangle brickBounds = brick.getBounds();
        
        // Calculate overlap on each side
        double overlapLeft = (getX() + radius) - brickBounds.x;
        double overlapRight = (brickBounds.x + brickBounds.width) - (getX() - radius);
        double overlapTop = (getY() + radius) - brickBounds.y;
        double overlapBottom = (brickBounds.y + brickBounds.height) - (getY() - radius);
        
        // Find minimum overlap
        double minOverlap = Math.min(Math.min(overlapLeft, overlapRight), 
                                     Math.min(overlapTop, overlapBottom));
        
        // Bounce based on collision side
        if (minOverlap == overlapLeft || minOverlap == overlapRight) {
            setVelocityX(-getVelocityX());
            // Adjust position to prevent sticking
            if (minOverlap == overlapLeft) {
                setX(brickBounds.x - radius - 1);
            } else {
                setX(brickBounds.x + brickBounds.width + radius + 1);
            }
        } else {
            setVelocityY(-getVelocityY());
            // Adjust position to prevent sticking
            if (minOverlap == overlapTop) {
                setY(brickBounds.y - radius - 1);
            } else {
                setY(brickBounds.y + brickBounds.height + radius + 1);
            }
        }
    }
    
    public void slow() {
        if (speedMultiplier == 1.0) { // Only slow if not already slowed
            speedMultiplier = 0.6;
            setVelocityX(getVelocityX() * 0.6);
            setVelocityY(getVelocityY() * 0.6);
        }
    }
    
    public void restoreNormalSpeed() {
        if (speedMultiplier != 1.0 && !attached) {
            // Restore to normal speed
            setVelocityX(getVelocityX() / speedMultiplier);
            setVelocityY(getVelocityY() / speedMultiplier);
            speedMultiplier = 1.0;
        }
    }
    
    public void setVelocity(double dx, double dy) {
        setVelocityX(dx);
        setVelocityY(dy);
    }
    
    public boolean intersects(Rectangle rect) {
        // Check if circle intersects rectangle
        double closestX = Math.max(rect.x, Math.min(getX(), rect.x + rect.width));
        double closestY = Math.max(rect.y, Math.min(getY(), rect.y + rect.height));
        
        double distanceX = getX() - closestX;
        double distanceY = getY() - closestY;
        
        return (distanceX * distanceX + distanceY * distanceY) < (radius * radius);
    }
    
    /**
     * Polymorphism: Override abstract render() method
     */
    @Override
    public void render(Graphics2D g2d) {
        if (ballImage != null) {
            int size = radius * 2;
            g2d.drawImage(ballImage, (int)(getX() - radius), (int)(getY() - radius), size, size, null);
        } else {
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int)(getX() - radius), (int)(getY() - radius), radius * 2, radius * 2);
        }
    }
    
    /**
     * Legacy draw method for compatibility
     * Delegates to render()
     */
    public void draw(Graphics2D g2d) {
        render(g2d);
    }
    
    // Encapsulation: Public getters
    // getX() and getY() are inherited from entities.GameObject
    public double getDx() { return getVelocityX(); }
    public double getDy() { return getVelocityY(); }
    public boolean isAttached() { return attached; }
}
