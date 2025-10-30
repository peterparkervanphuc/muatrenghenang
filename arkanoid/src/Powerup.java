import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * Represents a powerup that falls from destroyed bricks
 * OOP Principles Applied:
 * - Inheritance: Extends MovableObject for automatic falling movement
 * - Encapsulation: Private fields with controlled access
 * - Polymorphism: Overrides update() and render(), enum for type safety
 * - Abstraction: Hides image loading and rendering complexity
 */
public class Powerup extends MovableObject {
    // Encapsulation: Private constants
    private static final int POWERUP_WIDTH = 40;
    private static final int POWERUP_HEIGHT = 20;
    private static final int FALL_SPEED = 3;
    
    // Encapsulation: Private fields
    private PowerupType type;
    private BufferedImage powerupImage;
    
    public enum PowerupType {
        ENLARGE("Sprites/Powerups/Enlarge.png"),
        LASER("Sprites/Powerups/Laser.png"),
        CATCH("Sprites/Powerups/Catch.png"),
        SLOW("Sprites/Powerups/Slow.png"),
        DUPLICATE("Sprites/Powerups/Duplicate.png"),
        BREAK("Sprites/Powerups/Break.png"),
        PLAYER("Sprites/Powerups/Player.png"); // Extra life

        private final String imagePath;
        
        PowerupType(String imagePath) {
            this.imagePath = imagePath;
        }
        
        public String getImagePath() { return imagePath; }
    }
    
    /**
     * Constructor: Create random powerup at position
     * Encapsulation: Initializes with random type
     */
    public Powerup(int x, int y) {
        super(x, y, POWERUP_WIDTH, POWERUP_HEIGHT, FALL_SPEED);
        
        // Random powerup type - Polymorphism through enum
        PowerupType[] types = PowerupType.values();
        this.type = types[(int)(Math.random() * types.length)];
        
        // Set downward velocity (falling effect)
        setVelocity(0, FALL_SPEED);
        
        loadImage();
    }
    
    /**
     * Constructor: Create specific powerup type at position
     * Polymorphism: Constructor overloading
     */
    public Powerup(int x, int y, PowerupType type) {
        super(x, y, POWERUP_WIDTH, POWERUP_HEIGHT, FALL_SPEED);
        this.type = type;
        
        // Set downward velocity (falling effect)
        setVelocity(0, FALL_SPEED);
        
        loadImage();
    }
    
    private void loadImage() {
        try {
            var powerupStream = getClass().getClassLoader().getResourceAsStream(type.getImagePath());
            if (powerupStream != null) {
                powerupImage = ImageIO.read(powerupStream);
                powerupStream.close();
            }
        } catch (Exception e) {
            System.err.println("Could not load powerup image: " + e.getMessage());
        }
    }
    
    /**
     * Polymorphism: Uses inherited update() method from MovableObject
     * Movement (falling) is automatically handled by velocity set in constructor
     */
    
    /**
     * Polymorphism: Override abstract render() method
     * Abstraction: Hides complex drawing logic
     */
    @Override
    public void render(Graphics2D g2d) {
        if (powerupImage != null) {
            g2d.drawImage(powerupImage, (int)getX(), (int)getY(), getWidth(), getHeight(), null);
        } else {
            // Fallback drawing
            g2d.setColor(getColorForType());
            g2d.fillRoundRect((int)getX(), (int)getY(), getWidth(), getHeight(), 5, 5);
            
            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect((int)getX(), (int)getY(), getWidth(), getHeight(), 5, 5);
            
            // Draw letter indicator
            String letter = type.name().substring(0, 1);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (int)getX() + (getWidth() - fm.stringWidth(letter)) / 2;
            int textY = (int)getY() + (getHeight() + fm.getAscent()) / 2 - 2;
            g2d.drawString(letter, textX, textY);
        }
    }
    
    private Color getColorForType() {
        switch (type) {
            case ENLARGE: return new Color(0, 150, 255); // Blue
            case LASER: return new Color(255, 50, 50); // Red
            case CATCH: return new Color(50, 255, 50); // Green
            case SLOW: return new Color(255, 200, 0); // Yellow
            case DUPLICATE: return new Color(255, 100, 255); // Pink
            case BREAK: return new Color(150, 75, 0); // Brown
            case PLAYER: return new Color(0, 255, 255); // Cyan
            default: return Color.GRAY;
        }
    }
    
    /**
     * Check collision with rectangle
     * Abstraction: Uses inherited getBounds()
     */
    public boolean intersects(Rectangle rect) {
        return getBounds().intersects(rect);
    }
    
    // Encapsulation: Public getter for powerup type
    public PowerupType getType() { return type; }
}
