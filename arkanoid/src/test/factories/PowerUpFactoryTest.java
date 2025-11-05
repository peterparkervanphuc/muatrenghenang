package test.factories;

import entities.Powerup;
import factories.PowerUpFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PowerUpFactoryTest {
    private double x, y;

    @BeforeEach
    public void setUp() {
        x = 100.0;
        y = 150.0;
    }

    @Test
    public void createPowerUpTest() {
        Powerup p = PowerUpFactory.createPowerUp(Powerup.PowerupType.LASER, x, y);
        assertNotNull(p);
        assertEquals(Powerup.PowerupType.LASER, p.getType());
        assertEquals((int) x, p.getX());
        assertEquals((int) y, p.getY());
    }

    @Test
    public void createRandomPowerUpTest() {
        Powerup random = PowerUpFactory.createRandomPowerUp(x, y);
        assertNotNull(random);
        assertTrue(random.getType() instanceof Powerup.PowerupType);
        assertEquals((int) x, random.getX());
        assertEquals((int) y, random.getY());
    }

    @Test
    public void createPowerUpFromBrickTest() {
        Powerup p = PowerUpFactory.createPowerUpFromBrick(x, y, 1.0);
        assertNotNull(p);
        assertTrue(p.getType() instanceof Powerup.PowerupType);

        p = PowerUpFactory.createPowerUpFromBrick(x, y, 0.0);
        assertNull(p);
    }

    @Test
    public void createBonusPowerUpTest() {
        Powerup p = PowerUpFactory.createBonusPowerUp(x, y);
        assertNotNull(p);
        assertTrue(p.getType() instanceof Powerup.PowerupType);

        // Check that the bonus type is one of the beneficial ones
        Powerup.PowerupType type = p.getType();
        boolean valid = type == Powerup.PowerupType.ENLARGE ||
                type == Powerup.PowerupType.PLAYER ||
                type == Powerup.PowerupType.LASER ||
                type == Powerup.PowerupType.CATCH ||
                type == Powerup.PowerupType.DUPLICATE;
        assertTrue(valid, "Bonus power up phải là loại có lợi.");
    }
}
