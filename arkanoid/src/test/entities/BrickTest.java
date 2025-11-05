package test.entities;

import entities.Brick;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BrickTest {
    @Test
    public void testBrickInit() {
        Brick brick = new Brick(100, 200, Brick.BrickType.RED);
        assertEquals(100, brick.getX());
        assertEquals(200, brick.getY());
        assertEquals(1, brick.getHits());
        assertEquals(Brick.BrickType.RED, brick.getType());
        assertEquals(Brick.BrickType.RED.getPoints(), brick.getPoints());
    }

    @Test
    public void testBrickHit() {
        Brick brick = new Brick(100, 200, Brick.BrickType.SILVER);
        assertEquals(3, brick.getType().getMaxHits());
        brick.hit();
        assertEquals(2, brick.getHits());
        brick.hit();
        assertEquals(1, brick.getHits());
        brick.hit();
        assertEquals(0, brick.getHits());
    }
}
