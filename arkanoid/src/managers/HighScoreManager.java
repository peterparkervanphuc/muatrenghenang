package managers;

import utils.GameLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Manages high scores - reading, writing, and sorting
 */
public class HighScoreManager {
    private static String highScoreFile;
    private static int maxScores;

    static {
        ConfigManager config = ConfigManager.getInstance();
        highScoreFile = config.getHighScoreFile();
        maxScores = config.getInt("highscore.max.entries", 10);
    }

    public static class ScoreEntry {
        public String name;
        public int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
        
        @Override
        public String toString() {
            return name + ":" + score;
        }
    }
    
    /**
     * Load high scores from file
     */
    public static ArrayList<ScoreEntry> loadHighScores() {
        ArrayList<ScoreEntry> scores = new ArrayList<>();
        
        try {
            File file = new File(highScoreFile);
            if (!file.exists()) {
                GameLogger.info("High score file not found, creating default scores");
                return createDefaultScores();
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    try {
                        String name = parts[0].trim();
                        int score = Integer.parseInt(parts[1].trim());
                        scores.add(new ScoreEntry(name, score));
                    } catch (NumberFormatException e) {
                        GameLogger.warning("Invalid score entry: " + line);
                    }
                }
            }
            
            reader.close();
            
            // Sort by score descending
            Collections.sort(scores, new Comparator<ScoreEntry>() {
                @Override
                public int compare(ScoreEntry a, ScoreEntry b) {
                    return Integer.compare(b.score, a.score);
                }
            });
            
            GameLogger.info("Loaded " + scores.size() + " high scores");

        } catch (IOException e) {
            GameLogger.error("Error loading high scores: " + e.getMessage());
            return createDefaultScores();
        }
        
        return scores;
    }
    
    /**
     * Save high scores to file
     */
    public static void saveHighScores(ArrayList<ScoreEntry> scores) {
        try {
            // Ensure directory exists
            File file = new File(highScoreFile);
            file.getParentFile().mkdirs();
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            
            for (ScoreEntry entry : scores) {
                writer.write(entry.toString());
                writer.newLine();
            }
            
            writer.close();
            GameLogger.info("Saved " + scores.size() + " high scores");

        } catch (IOException e) {
            GameLogger.error("Error saving high scores: " + e.getMessage());
        }
    }
    
    /**
     * Add a new score and update the high score list
     */
    public static boolean addScore(String name, int score) {
        ArrayList<ScoreEntry> scores = loadHighScores();
        
        // Add new score
        scores.add(new ScoreEntry(name, score));
        GameLogger.info("Adding score: " + name + " - " + score);

        // Sort by score descending
        Collections.sort(scores, new Comparator<ScoreEntry>() {
            @Override
            public int compare(ScoreEntry a, ScoreEntry b) {
                return Integer.compare(b.score, a.score);
            }
        });
        
        // Keep only top scores
        while (scores.size() > maxScores) {
            scores.remove(scores.size() - 1);
        }
        
        // Save updated scores
        saveHighScores(scores);
        
        // Return true if the score made it to top list
        for (ScoreEntry entry : scores) {
            if (entry.name.equals(name) && entry.score == score) {
                GameLogger.info("Score made it to high score list!");
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Create default high scores if file doesn't exist
     */
    private static ArrayList<ScoreEntry> createDefaultScores() {
        ArrayList<ScoreEntry> scores = new ArrayList<>();
        scores.add(new ScoreEntry("TAITO", 10000));
        scores.add(new ScoreEntry("ARKANOID", 8000));
        scores.add(new ScoreEntry("VAUS", 6000));
        scores.add(new ScoreEntry("DOH", 5000));
        scores.add(new ScoreEntry("PLAYER1", 4000));
        scores.add(new ScoreEntry("PLAYER2", 3000));
        scores.add(new ScoreEntry("ARCADE", 2000));
        scores.add(new ScoreEntry("RETRO", 1500));
        scores.add(new ScoreEntry("CLASSIC", 1000));
        scores.add(new ScoreEntry("GAMER", 500));
        
        // Save default scores
        saveHighScores(scores);
        
        return scores;
    }
}
