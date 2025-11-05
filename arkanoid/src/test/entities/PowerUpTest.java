package test.entities;

import entities.Powerup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;

public class PowerUpTest {
    private Powerup powerup;
    private Powerup specificPowerup;

    @BeforeEach
    public void setUp() {
        powerup = new Powerup(100, 200);
        specificPowerup = new Powerup(50, 80, Powerup.PowerupType.ENLARGE);
    }

    @Test
    public void PowerInitTest() {
        // Random type powerup
        assertNotNull(powerup.getType());
        assertEquals(100, powerup.getX());
        assertEquals(200, powerup.getY());
        assertEquals(40, powerup.getWidth());
        assertEquals(20, powerup.getHeight());

        assertEquals(Powerup.PowerupType.ENLARGE, specificPowerup.getType());
        assertEquals(50, specificPowerup.getX());
        assertEquals(80, specificPowerup.getY());
        assertEquals(40, specificPowerup.getWidth());
        assertEquals(20, specificPowerup.getHeight());
    }

    @Test
    public void PowerupTest() {
        assertEquals(3, specificPowerup.getSpeed());
        assertEquals(3, specificPowerup.getVelocityY());
        assertEquals(0, specificPowerup.getVelocityX());

        Rectangle bounds = specificPowerup.getBounds();
        assertEquals(50, bounds.x);
        assertEquals(80, bounds.y);
        assertEquals(40, bounds.width);
        assertEquals(20, bounds.height);

        Rectangle rectHit = new Rectangle(55, 85, 10, 10);
        Rectangle rectMiss = new Rectangle(200, 300, 20, 20);
        assertTrue(specificPowerup.intersects(rectHit));
        assertFalse(specificPowerup.intersects(rectMiss));
    }

    @Test
    public void loadImageTest() {
        assertDoesNotThrow(() -> new Powerup(10, 10, Powerup.PowerupType.BREAK));
    }

    @Test
    public void renderTest() {
        Powerup fallbackPowerup = new Powerup(20, 30, Powerup.PowerupType.CATCH);

        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        assertDoesNotThrow(() -> fallbackPowerup.render(g2d));

        g2d.dispose();
    }

    @Test
    public void PowerupTypeTest() {
        for (Powerup.PowerupType type : Powerup.PowerupType.values()) {
            assertNotNull(type.getImagePath());
            assertTrue(type.getImagePath().endsWith(".png"));
        }

        assertEquals("Sprites/Powerups/Enlarge.png", Powerup.PowerupType.ENLARGE.getImagePath());
        assertEquals("PLAYER", Powerup.PowerupType.PLAYER.name());
    }
}
