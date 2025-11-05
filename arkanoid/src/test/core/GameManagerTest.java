package test.core;

import core.GameManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameManager class
 */
public class GameManagerTest {

    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        gameManager = new GameManager();
    }

    @Test
    public void testResetGame() {
        gameManager.addLife();
        gameManager.addScore(500);
        gameManager.nextLevel();
        gameManager.setPaddleEnlarged(true);
        gameManager.resetGame();

        assertEquals(0, gameManager.getScore());
        assertEquals(5, gameManager.getLives());
        assertEquals(1, gameManager.getCurrentLevel());
        assertFalse(gameManager.isGameOver());
        assertFalse(gameManager.isPaddleEnlarged());
    }

    @Test
    public void testAddScore() {
        gameManager.addScore(100);
        assertEquals(100, gameManager.getScore());
        gameManager.addScore(250);
        assertEquals(350, gameManager.getScore());
    }

    @Test
    public void testLoseLife() {
        gameManager.loseLife();
        assertEquals(4, gameManager.getLives());
        assertFalse(gameManager.isGameOver());

        gameManager.loseLife();
        gameManager.loseLife();
        gameManager.loseLife();
        gameManager.loseLife();
        assertTrue(gameManager.isGameOver());
    }

    @Test
    public void testAddLife() {
        gameManager.addLife();
        assertEquals(6, gameManager.getLives());
    }

    @Test
    public void testNextLevel() {
        gameManager.nextLevel();
        assertEquals(2, gameManager.getCurrentLevel());

        // Giả lập vượt maxLevel
        for (int i = 0; i < 100; i++) {
            gameManager.nextLevel();
        }
        assertTrue(gameManager.isGameOver(), "Game should end after max level reached");
    }


    @Test
    void testGameOver() {
        assertFalse(gameManager.isPaddleEnlarged());
        gameManager.setPaddleEnlarged(true);
        assertTrue(gameManager.isPaddleEnlarged());
    }

    @Test
    void testPaddleEnlarged() {
        assertFalse(gameManager.isPaddleEnlarged());
        gameManager.setPaddleEnlarged(true);
        assertTrue(gameManager.isPaddleEnlarged());
    }
}