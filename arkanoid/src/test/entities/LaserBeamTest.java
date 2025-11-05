package test.entities;

import entities.LaserBeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LaserBeamTest {
    private LaserBeam laser;

    @BeforeEach
    void setUp() {
        laser = new LaserBeam(100, 200);
    }

    @Test
    void testInitialProperties() {
        assertEquals(100, laser.getX());
        assertEquals(200, laser.getY());
        assertEquals(4, laser.getWidth());
        assertEquals(15, laser.getHeight());
        assertTrue(laser.isActive());
    }

    @Test
    void testUpwardMovement() {
        double initialY = laser.getY();
        laser.update();
        assertTrue(laser.getY() < initialY);
    }

    @Test
    void testActiveFlag() {
        assertTrue(laser.isActive());
        laser.setActive(false);
        assertFalse(laser.isActive());
    }

    @Test
    void testCollisionIntersectsTrue() {
        Rectangle rect = new Rectangle(100, 190, 10, 20);
        assertTrue(laser.intersects(rect));
    }

    @Test
    void testCollisionIntersectsFalse() {
        Rectangle rect = new Rectangle(300, 300, 20, 20);
        assertFalse(laser.intersects(rect));
    }

    @Test
    void testRenderFallbackWithoutImage() {
        try {
            var field = LaserBeam.class.getDeclaredField("laserImage");
            field.setAccessible(true);
            field.set(laser, null);
        } catch (Exception e) {
            fail("Không thể truy cập laserImage");
        }

        BufferedImage canvas = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = canvas.createGraphics();

        assertDoesNotThrow(() -> laser.render(g2d));

        g2d.dispose();
    }
}
