package test.factories;

import entities.Brick;
import factories.BrickFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BrickFactoryTest {
    private int x, y;

    @BeforeEach
    public void setUp() {
        x = 100;
        y = 200;
    }

    @Test
    public void createBrickTest() {
        Brick brick = BrickFactory.createBrick(1, x, y);
        assertNotNull(brick);
        assertEquals(Brick.BrickType.WHITE, brick.getType());
        assertEquals(x, brick.getX());
        assertEquals(y, brick.getY());
    }

    @Test
    public void createRandomBrickTest() {
        Brick randomBrick = BrickFactory.createRandomBrick(x, y);
        assertNotNull(randomBrick);
        assertNotEquals(Brick.BrickType.SILVER, randomBrick.getType());
        assertEquals(x, randomBrick.getX());
        assertEquals(y, randomBrick.getY());
    }
}
