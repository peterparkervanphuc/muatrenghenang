package entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Represents a laser beam fired by the paddle
 * OOP Principles Applied:
 * - Inheritance: Extends entities.MovableObject for automatic upward movement
 * - Encapsulation: Private constants and controlled access
 * - Polymorphism: Overrides render() method
 * - Abstraction: Hides movement logic in velocity
 */
public class LaserBeam extends MovableObject {
    // Encapsulation: Private constants
    private static final int LASER_WIDTH = 4;
    private static final int LASER_HEIGHT = 15;
    private static final int LASER_SPEED = 10;
    
    // Encapsulation: Private fields
    private boolean active = true;
    private BufferedImage laserImage;
    
    /**
     * Constructor: Create laser beam at position
     * Encapsulation: Initializes with upward velocity
     */
    public LaserBeam(int x, int y) {
        super(x, y, LASER_WIDTH, LASER_HEIGHT, LASER_SPEED);
        
        // Set upward velocity (negative Y direction)
        setVelocity(0, -LASER_SPEED);
        
        loadImage();
    }
    
    private void loadImage() {
        try {
            var laserStream = getClass().getClassLoader().getResourceAsStream("Sprites/Spacecraft/Laser Beam.png");
            if (laserStream != null) {
                laserImage = ImageIO.read(laserStream);
                laserStream.close();
            }
        } catch (Exception e) {
            System.err.println("Could not load laser beam image: " + e.getMessage());
        }
    }
    
    /**
     * Polymorphism: Uses inherited update() method from entities.MovableObject
     * Movement (upward) is automatically handled by velocity
     */
    
    /**
     * Polymorphism: Override abstract render() method
     * Abstraction: Hides rendering complexity
     */
    @Override
    public void render(Graphics2D g2d) {
        if (!active) return;
        
        if (laserImage != null) {
            g2d.drawImage(laserImage, (int)getX(), (int)getY(), getWidth(), getHeight(), null);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillRect((int)getX(), (int)getY(), getWidth(), getHeight());
        }
    }
    
    /**
     * Check collision with rectangle
     * Abstraction: Uses inherited getBounds() from entities.GameObject
     */
    public boolean intersects(Rectangle rect) {
        return getBounds().intersects(rect);
    }
    
    /**
     * Abstraction: Uses inherited getBounds() method
     */
    // Removed: getBounds() is inherited from entities.GameObject
    
    // Encapsulation: Public getters
    // Removed: getY() is inherited from entities.GameObject (returns double)
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
