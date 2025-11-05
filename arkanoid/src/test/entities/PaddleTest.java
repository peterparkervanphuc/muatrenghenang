package test.entities;

import core.GameBounds;
import entities.Paddle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;


public class PaddleTest {
    private Paddle paddle;

    @BeforeEach
    public void setup() {
        paddle = new Paddle(100, 100, false);
    }

    @Test
    public void moveLeftTest() {
        paddle.setX(10);
        paddle.moveLeft();
        assertTrue(paddle.getX() >= GameBounds.PLAY_LEFT, "Paddle không di chuyển quá giới hạn bên trái");

        paddle.setX(100);
        double oldX = paddle.getX();
        paddle.moveLeft();
        assertTrue(paddle.getX() < oldX, "Paddle nên di chuyển sang trái");
    }

    @Test
    public void moveRightTest() {
        paddle.setX(GameBounds.PLAY_RIGHT - paddle.getWidth() - 5);
        paddle.moveRight();
        assertTrue(paddle.getX() + paddle.getWidth() <= GameBounds.PLAY_RIGHT, "Paddle không di chuyển quá giới hạn bên phải");

        paddle.setX(50);
        double oldX = paddle.getX();
        paddle.moveRight();
        assertTrue(paddle.getX() > oldX, "Paddle nên di chuyển sang phải");
    }

    @Test
    public void enlargeTest() {
        assertFalse(paddle.isEnlarged());
        int oldWidth = paddle.getWidth();

        paddle.enlarge();

        assertTrue(paddle.isEnlarged());
        assertTrue(paddle.getWidth() > oldWidth);
        assertTrue(paddle.getX() >= GameBounds.PLAY_LEFT);
        assertTrue(paddle.getX() + paddle.getWidth() <= GameBounds.PLAY_RIGHT);
    }

    @Test
    public void shrinkTest() {
        paddle.enlarge();
        assertTrue(paddle.isEnlarged());

        paddle.shrink();

        assertFalse(paddle.isEnlarged());
        assertEquals(80, paddle.getWidth());
    }

    @Test
    public void laserPowerupTest() {
        assertFalse(paddle.hasLaser());

        paddle.enableLaser();
        assertTrue(paddle.hasLaser());

        paddle.disableLaser();
        assertFalse(paddle.hasLaser());
    }

    @Test
    public void catchPowerupTest() {
        assertFalse(paddle.hasCatch());

        paddle.enableCatch();
        assertTrue(paddle.hasCatch());

        paddle.disableCatch();
        assertFalse(paddle.hasCatch());
    }

    @Test
    public void updateTest() {
        paddle.enableLaser();
        paddle.fireLaser();

        assertFalse(paddle.getLasers().isEmpty());
        paddle.getLasers().get(0).setY(-5);
        paddle.update();

        assertTrue(paddle.getLasers().size() <= 2, "các tia laser bên ngoaif phải được loại");
    }

    @Test
    public void renderTest() {
        Graphics2D g2d = new BufferedImage(300, 200, BufferedImage.TYPE_INT_ARGB).createGraphics();
        assertDoesNotThrow(() -> paddle.render(g2d));
    }
}
