/**
 * Manages the game state, score, lives, and level progression
 */
public class GameManager {
    private int score;
    private int lives;
    private int currentLevel;
    private boolean gameOver;
    private boolean paddleEnlarged;
    
    private final ConfigManager config;

    public GameManager() {
        config = ConfigManager.getInstance();
        resetGame();
    }
    
    public void resetGame() {
        score = 0;
        lives = config.getInitialLives();
        currentLevel = 1;
        gameOver = false;
        paddleEnlarged = false;
        GameLogger.info("Game reset - Lives: " + lives);
    }
    
    public void addScore(int points) {
        score += points;
        GameLogger.debug("Score added: " + points + " | Total: " + score);
    }
    
    public void loseLife() {
        lives--;
        GameLogger.info("Life lost - Remaining lives: " + lives);
        if (lives <= 0) {
            gameOver = true;
            GameLogger.info("Game Over!");
        }
    }
    
    public void addLife() {
        lives++;
        GameLogger.info("Extra life gained - Lives: " + lives);
    }
    
    public void nextLevel() {
        currentLevel++;
        GameLogger.info("Advancing to level: " + currentLevel);
        if (currentLevel > config.getMaxLevel()) {
            gameOver = true;
            GameLogger.info("All levels completed!");
        }
    }
    
    // Getters and setters
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public int getCurrentLevel() { return currentLevel; }
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public boolean isPaddleEnlarged() { return paddleEnlarged; }
    public void setPaddleEnlarged(boolean enlarged) { this.paddleEnlarged = enlarged; }
}
