import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for core game functionality
 * Tests collision detection, score management, and game state
 */
public class GameTest {

    private GameManager gameManager;
    private Ball ball;
    private Paddle paddle;
    private Brick brick;

    @BeforeEach
    public void setUp() {
        gameManager = new GameManager();
        gameManager.resetGame();
        ball = new Ball(400, 300);
        paddle = new Paddle(350, 550, false); // x, y, enlarged
        brick = BrickFactory.createBrick(Brick.BrickType.RED, 100, 50);
    }

    /**
     * Test: Ball-Brick collision detection
     */
    @Test
    public void testBallBrickCollision() {
        ball.setX(100);
        ball.setY(50);
        assertTrue(ball.intersects(brick.getBounds()),
            "Ball should intersect with brick at same position");
    }

    /**
     * Test: Brick destruction after hit
     */
    @Test
    public void testBrickDestruction() {
        assertFalse(brick.isDestroyed(), "Brick should not be destroyed initially");
        brick.hit();
        assertTrue(brick.isDestroyed(), "Normal brick should be destroyed after one hit");
    }

    /**
     * Test: Silver brick requires 3 hits
     */
    @Test
    public void testSilverBrickThreeHits() {
        Brick silverBrick = BrickFactory.createSilverBrick(100, 50);
        assertFalse(silverBrick.isDestroyed(), "Silver brick should not be destroyed initially");

        silverBrick.hit();
        assertFalse(silverBrick.isDestroyed(), "Silver brick should not be destroyed after 1 hit");

        silverBrick.hit();
        assertFalse(silverBrick.isDestroyed(), "Silver brick should not be destroyed after 2 hits");

        silverBrick.hit();
        assertTrue(silverBrick.isDestroyed(), "Silver brick should be destroyed after 3 hits");
    }

    /**
     * Test: Score increases when brick is destroyed
     */
    @Test
    public void testScoreIncrease() {
        int initialScore = gameManager.getScore();
        int brickPoints = brick.getPoints();

        gameManager.addScore(brickPoints);

        assertEquals(initialScore + brickPoints, gameManager.getScore(),
            "Score should increase by brick points value");
    }

    /**
     * Test: Lives decrease when ball is lost
     */
    @Test
    public void testLivesDecrease() {
        int initialLives = gameManager.getLives();
        gameManager.loseLife();

        assertEquals(initialLives - 1, gameManager.getLives(),
            "Lives should decrease by 1 when ball is lost");
    }

    /**
     * Test: Game over when no lives remaining
     */
    @Test
    public void testGameOver() {
        // Lose all lives (default is 3)
        gameManager.loseLife();
        gameManager.loseLife();
        assertFalse(gameManager.isGameOver(), "Game should not be over with 1 life");

        gameManager.loseLife(); // Lose last life
        assertTrue(gameManager.isGameOver(), "Game should be over with 0 lives");
    }

    /**
     * Test: Paddle stays within bounds
     */
    @Test
    public void testPaddleBounds() {
        paddle.setX(0);
        paddle.moveLeft();
        assertTrue(paddle.getX() >= GameBounds.PLAY_LEFT,
            "Paddle should not move beyond left boundary");

        paddle.setX(GameBounds.PLAY_RIGHT - paddle.getWidth());
        paddle.moveRight();
        assertTrue(paddle.getX() <= GameBounds.PLAY_RIGHT - paddle.getWidth(),
            "Paddle should not move beyond right boundary");
    }

    /**
     * Test: Ball velocity can be changed (simulating bounce)
     */
    @Test
    public void testBallPaddleBounce() {
        // Test that ball velocity can change direction (simulating bounce)
        ball.setVelocityY(5); // Moving down
        assertTrue(ball.getVelocityY() > 0, "Ball should be moving downward initially");

        // Simulate bounce by reversing Y velocity
        ball.setVelocityY(-Math.abs(ball.getVelocityY()));

        assertTrue(ball.getVelocityY() < 0, "Ball velocity should reverse to upward");
        assertEquals(-5, ball.getVelocityY(), 0.01, "Ball velocity should be -5 after bounce");
    }

    /**
     * Test: BrickFactory creates correct brick types
     */
    @Test
    public void testBrickFactory() {
        Brick redBrick = BrickFactory.createBrick(Brick.BrickType.RED, 0, 0);
        assertNotNull(redBrick, "Factory should create red brick");
        assertEquals(90, redBrick.getPoints(), "Red brick should have 90 points");

        Brick silverBrick = BrickFactory.createSilverBrick(0, 0);
        assertNotNull(silverBrick, "Factory should create silver brick");
        assertTrue(silverBrick.isSilver(), "Created brick should be silver type");
    }

    /**
     * Test: PowerUpFactory creates power-ups
     */
    @Test
    public void testPowerUpFactory() {
        Powerup powerup = PowerUpFactory.createPowerUp(Powerup.PowerupType.ENLARGE, 100, 100);
        assertNotNull(powerup, "Factory should create power-up");
        assertEquals(100, (int)powerup.getX(), "Power-up should be at correct X position");
        assertEquals(100, (int)powerup.getY(), "Power-up should be at correct Y position");
    }

    /**
     * Test: Level progression
     */
    @Test
    public void testLevelProgression() {
        int initialLevel = gameManager.getCurrentLevel();
        gameManager.nextLevel();

        assertEquals(initialLevel + 1, gameManager.getCurrentLevel(),
            "Level should increase by 1 after progression");
    }

    /**
     * Test: Random powerup creation with probability
     */
    @Test
    public void testPowerUpRandomCreation() {
        // Test that factory can create random powerups
        Powerup randomPowerup = PowerUpFactory.createRandomPowerUp(200, 200);
        assertNotNull(randomPowerup, "Factory should create random power-up");
    }
}

