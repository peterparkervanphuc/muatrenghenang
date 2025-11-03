package managers;

import entities.Ball;
import entities.Brick;
import entities.Powerup;
import entities.Paddle;
import core.GameManager;
import utils.GameLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages saving and loading game state
 * Provides auto-save and manual save functionality
 */
public class SaveGameManager {
    private static final String SAVE_DIR = "Saves";
    private static final String SAVE_FILE_PREFIX = "save_slot_";
    private static final String SAVE_FILE_EXTENSION = ".dat";
    private static final int MAX_SAVE_SLOTS = 3;

    private static SaveGameManager instance;

    private SaveGameManager() {
        // Create saves directory if it doesn't exist
        File saveDir = new File(SAVE_DIR);
        if (!saveDir.exists()) {
            saveDir.mkdir();
            GameLogger.info("Created saves directory");
        }
    }

    public static SaveGameManager getInstance() {
        if (instance == null) {
            instance = new SaveGameManager();
        }
        return instance;
    }

    /**
     * Save complete game state to a specific slot
     */
    public boolean saveGame(int slot, GameState gameState) {
        if (slot < 1 || slot > MAX_SAVE_SLOTS) {
            GameLogger.error("Invalid save slot: " + slot);
            return false;
        }

        String filename = SAVE_DIR + File.separator + SAVE_FILE_PREFIX + slot + SAVE_FILE_EXTENSION;

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(gameState);
            GameLogger.info("Game saved to slot " + slot);
            return true;
        } catch (IOException e) {
            GameLogger.error("Failed to save game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Load game state from a specific slot
     */
    public GameState loadGame(int slot) {
        if (slot < 1 || slot > MAX_SAVE_SLOTS) {
            GameLogger.error("Invalid save slot: " + slot);
            return null;
        }

        String filename = SAVE_DIR + File.separator + SAVE_FILE_PREFIX + slot + SAVE_FILE_EXTENSION;
        File saveFile = new File(filename);

        if (!saveFile.exists()) {
            GameLogger.info("No save file found in slot " + slot);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            GameState gameState = (GameState) ois.readObject();
            GameLogger.info("Game loaded from slot " + slot);
            return gameState;
        } catch (IOException | ClassNotFoundException e) {
            GameLogger.error("Failed to load game: " + e.getMessage());
            return null;
        }
    }

    /**
     * Check if a save slot has data
     */
    public boolean hasSaveData(int slot) {
        if (slot < 1 || slot > MAX_SAVE_SLOTS) {
            return false;
        }
        String filename = SAVE_DIR + File.separator + SAVE_FILE_PREFIX + slot + SAVE_FILE_EXTENSION;
        return new File(filename).exists();
    }

    /**
     * Delete save data from a slot
     */
    public boolean deleteSave(int slot) {
        if (slot < 1 || slot > MAX_SAVE_SLOTS) {
            return false;
        }
        String filename = SAVE_DIR + File.separator + SAVE_FILE_PREFIX + slot + SAVE_FILE_EXTENSION;
        File saveFile = new File(filename);
        if (saveFile.exists()) {
            boolean deleted = saveFile.delete();
            if (deleted) {
                GameLogger.info("Deleted save in slot " + slot);
            }
            return deleted;
        }
        return false;
    }

    /**
     * Get information about a save slot
     */
    public SaveInfo getSaveInfo(int slot) {
        if (!hasSaveData(slot)) {
            return null;
        }

        String filename = SAVE_DIR + File.separator + SAVE_FILE_PREFIX + slot + SAVE_FILE_EXTENSION;
        File saveFile = new File(filename);

        GameState state = loadGame(slot);
        if (state != null) {
            return new SaveInfo(
                slot,
                state.score,
                state.lives,
                state.currentLevel,
                saveFile.lastModified()
            );
        }
        return null;
    }

    /**
     * Inner class to hold save file information
     */
    public static class SaveInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        public final int slot;
        public final int score;
        public final int lives;
        public final int level;
        public final long timestamp;

        public SaveInfo(int slot, int score, int lives, int level, long timestamp) {
            this.slot = slot;
            this.score = score;
            this.lives = lives;
            this.level = level;
            this.timestamp = timestamp;
        }
    }

    /**
     * Inner class representing complete game state
     */
    public static class GameState implements Serializable {
        private static final long serialVersionUID = 1L;

        // GameManager state
        public int score;
        public int lives;
        public int currentLevel;
        public boolean gameOver;
        public boolean paddleEnlarged;

        // Paddle state
        public double paddleX;
        public double paddleY;
        public boolean paddleHasLaser;
        public boolean paddleHasCatch;

        // Ball states (support multiple balls)
        public List<BallState> balls;

        // Brick states
        public List<BrickState> bricks;

        // Powerup states (falling powerups)
        public List<PowerupState> powerups;

        // Powerup timers
        public long slowPowerupEndTime;
        public boolean slowPowerupActive;
        public long laserPowerupEndTime;
        public boolean laserPowerupActive;

        // Timestamp when saved
        public long saveTimestamp;

        public GameState() {
            balls = new ArrayList<>();
            bricks = new ArrayList<>();
            powerups = new ArrayList<>();
            saveTimestamp = System.currentTimeMillis();
        }
    }

    /**
     * Represents a ball's state
     */
    public static class BallState implements Serializable {
        private static final long serialVersionUID = 1L;

        public double x;
        public double y;
        public double velocityX;
        public double velocityY;
        public boolean attached;
        public double speedMultiplier;
        public double levelSpeedBonus;

        public BallState(Ball ball) {
            this.x = ball.getX();
            this.y = ball.getY();
            this.velocityX = ball.getVelocityX();
            this.velocityY = ball.getVelocityY();
            this.attached = ball.isAttached();
            this.speedMultiplier = ball.getSpeedMultiplier();
            this.levelSpeedBonus = ball.getLevelSpeedBonus();
        }
    }

    /**
     * Represents a brick's state
     */
    public static class BrickState implements Serializable {
        private static final long serialVersionUID = 1L;

        public double x;
        public double y;
        public String brickType; // Store as String for serialization
        public int hitsRemaining;

        public BrickState(Brick brick) {
            this.x = brick.getX();
            this.y = brick.getY();
            this.brickType = brick.getType().name();
            this.hitsRemaining = brick.getHitsRemaining();
        }
    }

    /**
     * Represents a powerup's state
     */
    public static class PowerupState implements Serializable {
        private static final long serialVersionUID = 1L;

        public double x;
        public double y;
        public String powerupType; // Store as String for serialization

        public PowerupState(Powerup powerup) {
            this.x = powerup.getX();
            this.y = powerup.getY();
            this.powerupType = powerup.getType().name();
        }
    }
}

