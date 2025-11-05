package test.entities;

import entities.Ball;
import entities.Brick;
import entities.Paddle;
import org.junit.jupiter.api.Test;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

public class BallTest {
    @Test
    public void testBallInit() {
        Ball ball = new Ball(100, 200);
        assertEquals(100, ball.getX());
        assertEquals(200, ball.getY());
        assertEquals(8, ball.getRadius());
        assertEquals(0, ball.getVelocityX());
        assertEquals(0, ball.getVelocityY());
        assertFalse(ball.isAttached());
    }

    @Test
    public void tesBallInit2() {
        Ball ball = new Ball(100, 200, 12);
        assertEquals(1.1, ball.getLevelSpeedBonus());
    }

    @Test
    public void testAttachToPaddle() {
        Paddle paddle = new Paddle(100, 200, false);
        Ball ball = new Ball(100, 200, 12);
        ball.attachToPaddle(paddle);
        assertTrue(ball.isAttached());
        assertEquals(0, ball.getVelocityX());
        assertEquals(0, ball.getVelocityY());
    }

    @Test
    public void testLaunch() {
        Paddle paddle = new Paddle(100, 200, false);
        Ball ball = new Ball(100, 200, 12);
        ball.attachToPaddle(paddle);
        ball.launch();
        assertFalse(ball.isAttached());
        assertNotEquals(0, ball.getVelocityX());
        assertNotEquals(0, ball.getVelocityY());
    }

    @Test
    public void testBounceOffPaddle() {
        Paddle paddle = new Paddle(100, 300, false);
        Ball ball = new Ball(140, 290, 12);
        ball.setVelocity(3, 4);
        ball.bounceOffPaddle(paddle);
        assertEquals(0, ball.getVelocityX());
        assertEquals(-5, ball.getVelocityY());

        ball.setVelocity(0, 1.25); // Moving downwards
        ball.bounceOffPaddle(paddle);
        assertEquals(-2, ball.getVelocityY());
    }

    //này mất thời gian quá :(
    @Test
    public void testBounceOffBrick() {
        Brick brick = new Brick(100, 100, Brick.BrickType.RED);
        Ball ball = new Ball(94, 110, 5);
        ball.setVelocity(2, 0);
        ball.bounceOffBrick(brick);
        assertEquals(-2, ball.getVelocityX(), 0.001);
        assertEquals(0, ball.getVelocityY(), 0.001);

        brick = new Brick(100, 100, Brick.BrickType.RED);
        ball = new Ball(166, 110, 5);
        ball.setVelocity(-2, 0);
        ball.bounceOffBrick(brick);
        assertEquals(2, ball.getVelocityX(), 0.001);
        assertEquals(0, ball.getVelocityY(), 0.001);

        brick = new Brick(100, 100, Brick.BrickType.RED);
        ball = new Ball(120, 94, 5);
        ball.setVelocity(0, 2);
        ball.bounceOffBrick(brick);

        assertEquals(-2, ball.getVelocityY(), 0.001);
        assertEquals(0, ball.getVelocityX(), 0.001);

        brick = new Brick(100, 100, Brick.BrickType.RED);
        ball = new Ball(120, 126, 5);
        ball.setVelocity(0, -2);
        ball.bounceOffBrick(brick);
        assertEquals(2, ball.getVelocityY(), 0.001);
        assertEquals(0, ball.getVelocityX(), 0.001);
    }

    @Test
    public void testSlow() {
        Ball ball = new Ball(100, 200, 11);
        ball.setVelocity(10, 10);
        ball.slow();
        assertEquals(5, ball.getVelocityX());
        assertEquals(5, ball.getVelocityY());

        ball.slow();
        assertEquals(3.75, ball.getVelocityX());
        assertEquals(3.75, ball.getVelocityY());
    }

    @Test
    public void testRestoreNormalSpeed() {
        Ball ball = new Ball(100, 200, 11);
        ball.setVelocity(10, 10);
        ball.slow();
        ball.restoreNormalSpeed();
        assertEquals(10, ball.getVelocityX());
        assertEquals(10, ball.getVelocityY());
        assertEquals(1, ball.getSpeedMultiplier());

        ball.slow();
        ball.slow();
        ball.restoreNormalSpeed();
        assertEquals(10, ball.getVelocityX());
        assertEquals(10, ball.getVelocityY());
        assertEquals(1, ball.getSpeedMultiplier());
    }

    @Test
    public void testIntersects() {
        Ball ball = new Ball(100, 200, 11);
        Rectangle rect = new Rectangle(40, 40, 30, 30);

        // ball nằm bên trong
        ball.setX(50);
        ball.setY(50);
        assertTrue(ball.intersects(rect));

        // ball chạm cạnh trái
        ball.setX(62);
        ball.setY(55);
        assertTrue(ball.intersects(rect));

        // ball nằm xa rectangle
        ball.setX(0);
        ball.setY(0);
        assertFalse(ball.intersects(rect));

        // ball cách rectangle 1 pixel
        ball.setX(79);
        ball.setY(55);
        assertFalse(ball.intersects(rect));

        // ball chạm góc trên trái
        ball.setX(34);
        ball.setY(34);
        assertFalse(ball.intersects(rect));
    }
}

