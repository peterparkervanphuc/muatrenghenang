package entities;

import core.GameBounds;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the player's paddle (Vaus spacecraft)
 * OOP Principles Applied:
 * - Inheritance: Extends entities.MovableObject to reuse movement logic
 * - Encapsulation: Private fields with controlled access
 * - Polymorphism: Overrides update() and render() methods
 * - Abstraction: Hides complex powerup management behind simple interface
 */
public class Paddle extends MovableObject {
    // Encapsulation: Private constants
    private static final int SPEED = 8;
    private static final int NORMAL_WIDTH = 80;
    private static final int NORMAL_HEIGHT = 20;
    private static final int ENLARGED_WIDTH = 120;
    private static final long LASER_COOLDOWN = 300; // milliseconds
    
    // Encapsulation: Private fields for powerup states
    private boolean enlarged;
    private boolean hasLaser;
    private boolean hasCatch;
    
    // Encapsulation: Private image resources
    private BufferedImage paddleImage;
    private BufferedImage laserPaddleImage;
    private BufferedImage enlargedImage;
    
    // Encapsulation: Private laser beam management
    private ArrayList<LaserBeam> laserBeams;
    private long lastLaserTime;
    
    /**
     * Constructor: Creates paddle at position with optional enlarged state
     * Encapsulation: Properly initializes all fields including inherited ones
     */
    public Paddle(int x, int y, boolean enlarged) {
        super(x, y, 
              enlarged ? ENLARGED_WIDTH : NORMAL_WIDTH, 
              NORMAL_HEIGHT, 
              SPEED);
        
        this.enlarged = enlarged;
        this.hasLaser = false;
        this.hasCatch = false;
        this.lastLaserTime = 0;
        this.laserBeams = new ArrayList<>();
        
        loadImages();
    }
    
    private void loadImages() {
        try {
            paddleImage = loadImage("Sprites/Spacecraft/VausSpacecraft.png");
            laserPaddleImage = loadImage("Sprites/Spacecraft/LaserVausSpacecraft.png");
            enlargedImage = loadImage("Sprites/Spacecraft/VausSpacecraftLarge.png");
        } catch (Exception e) {
            System.err.println("Could not load paddle images: " + e.getMessage());
        }
    }
    
    private BufferedImage loadImage(String path) throws Exception {
        var imgStream = getClass().getClassLoader().getResourceAsStream(path);
        if (imgStream == null) {
            throw new Exception("Image not found: " + path);
        }
        BufferedImage img = ImageIO.read(imgStream);
        imgStream.close();
        return img;
    }
    
    /**
     * Move paddle left within game bounds
     * Encapsulation: Uses inherited getters/setters instead of direct field access
     */
    public void moveLeft() {
        setX(getX() - SPEED);
        if (getX() < GameBounds.PLAY_LEFT) {
            setX(GameBounds.PLAY_LEFT);
        }
    }
    
    /**
     * Move paddle right within game bounds
     * Encapsulation: Uses inherited getters/setters instead of direct field access
     */
    public void moveRight() {
        setX(getX() + SPEED);
        if (getX() + getWidth() > GameBounds.PLAY_RIGHT) {
            setX(GameBounds.PLAY_RIGHT - getWidth());
        }
    }
    
    /**
     * Enlarge the paddle (powerup effect)
     * Encapsulation: Modifies size through proper methods
     */
    public void enlarge() {
        if (!enlarged) {
            int oldWidth = getWidth();
            setWidth(ENLARGED_WIDTH);
            setX(getX() - (getWidth() - oldWidth) / 2); // Center the enlargement
            
            // Keep within bounds
            if (getX() < GameBounds.PLAY_LEFT) setX(GameBounds.PLAY_LEFT);
            if (getX() + getWidth() > GameBounds.PLAY_RIGHT) setX(GameBounds.PLAY_RIGHT - getWidth());
            
            enlarged = true;
        }
    }
    
    /**
     * Shrink paddle back to normal size
     * Encapsulation: Modifies size through proper methods
     */
    public void shrink() {
        if (enlarged) {
            int oldWidth = getWidth();
            setWidth(NORMAL_WIDTH);
            setX(getX() + (oldWidth - getWidth()) / 2); // Center the shrink
            enlarged = false;
        }
    }
    
    public void enableLaser() {
        hasLaser = true;
    }
    
    public void disableLaser() {
        hasLaser = false;
    }
    
    public void enableCatch() {
        hasCatch = true;
    }
    
    public void disableCatch() {
        hasCatch = false;
    }
    
    /**
     * Fire laser beams from paddle
     * Encapsulation: Uses getters to access position
     */
    public void fireLaser() {
        if (!hasLaser) return;
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastLaserTime >= LASER_COOLDOWN) {
            // Fire two laser beams from left and right sides
            laserBeams.add(new LaserBeam((int)(getX() + 15), (int)getY()));
            laserBeams.add(new LaserBeam((int)(getX() + getWidth() - 15), (int)getY()));
            lastLaserTime = currentTime;
        }
    }
    
    /**
     * Update paddle and laser beams
     * Polymorphism: Overrides abstract update() method
     */
    @Override
    public void update() {
        // Update laser beams
        Iterator<LaserBeam> iterator = laserBeams.iterator();
        while (iterator.hasNext()) {
            LaserBeam laser = iterator.next();
            laser.update();
            
            if (!laser.isActive() || laser.getY() < 0) {
                iterator.remove();
            }
        }
    }
    
    /**
     * Render paddle and lasers
     * Polymorphism: Overrides abstract render() method
     */
    @Override
    public void render(Graphics2D g2d) {
        BufferedImage imageToDraw = paddleImage;
        
        if (hasLaser && laserPaddleImage != null) {
            imageToDraw = laserPaddleImage;
        }
        if (enlarged && enlargedImage != null) {
            imageToDraw = enlargedImage;
        }
        
        if (imageToDraw != null) {
            g2d.drawImage(imageToDraw, (int)getX(), (int)getY(), getWidth(), getHeight(), null);
        } else {
            // Fallback drawing
            g2d.setColor(Color.CYAN);
            g2d.fillRoundRect((int)getX(), (int)getY(), getWidth(), getHeight(), 10, 10);
            
            if (hasLaser) {
                g2d.setColor(Color.RED);
                g2d.fillRect((int)getX() + 10, (int)getY(), 5, 5);
                g2d.fillRect((int)getX() + getWidth() - 15, (int)getY(), 5, 5);
            }
        }
        
        // Draw laser beams (Polymorphism)
        for (LaserBeam laser : laserBeams) {
            laser.render(g2d);
        }
    }
    
    // Encapsulation: Public getters for powerup states
    public boolean hasLaser() { return hasLaser; }
    public boolean hasCatch() { return hasCatch; }
    public boolean isEnlarged() { return enlarged; }
    public ArrayList<LaserBeam> getLasers() { return laserBeams; }
}
