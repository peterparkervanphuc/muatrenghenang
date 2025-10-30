import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameManager
 */
class GameManagerTest {
    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        gameManager = new GameManager();
    }

    @Test
    @DisplayName("Should initialize with correct lives")
    void testInitialLives() {
        int lives = gameManager.getLives();
        assertTrue(lives > 0, "Should start with positive lives");
    }

    @Test
    @DisplayName("Should start at level 1")
    void testInitialLevel() {
        assertEquals(1, gameManager.getCurrentLevel(), "Should start at level 1");
    }

    @Test
    @DisplayName("Should not be game over initially")
    void testInitialGameOverState() {
        assertFalse(gameManager.isGameOver(), "Game should not be over initially");
    }

    @Test
    @DisplayName("Should add score correctly")
    void testAddScore() {
        int initialScore = gameManager.getScore();
        gameManager.addScore(100);
        assertEquals(initialScore + 100, gameManager.getScore(), "Score should increase by 100");
    }

    @Test
    @DisplayName("Should lose life correctly")
    void testLoseLife() {
        int initialLives = gameManager.getLives();
        gameManager.loseLife();
        assertEquals(initialLives - 1, gameManager.getLives(), "Lives should decrease by 1");
    }

    @Test
    @DisplayName("Should set game over when lives reach 0")
    void testGameOverWhenNoLives() {
        // Lose all lives
        while (gameManager.getLives() > 0) {
            gameManager.loseLife();
        }
        assertTrue(gameManager.isGameOver(), "Game should be over when no lives left");
    }

    @Test
    @DisplayName("Should add life correctly")
    void testAddLife() {
        int initialLives = gameManager.getLives();
        gameManager.addLife();
        assertEquals(initialLives + 1, gameManager.getLives(), "Lives should increase by 1");
    }

    @Test
    @DisplayName("Should advance to next level")
    void testNextLevel() {
        int initialLevel = gameManager.getCurrentLevel();
        gameManager.nextLevel();
        assertEquals(initialLevel + 1, gameManager.getCurrentLevel(), "Should advance one level");
    }

    @Test
    @DisplayName("Should reset game correctly")
    void testResetGame() {
        // Make some changes
        gameManager.addScore(500);
        gameManager.loseLife();
        gameManager.nextLevel();

        // Reset
        gameManager.resetGame();

        assertEquals(0, gameManager.getScore(), "Score should reset to 0");
        assertEquals(1, gameManager.getCurrentLevel(), "Level should reset to 1");
        assertFalse(gameManager.isGameOver(), "Game over should be false");
    }
}

