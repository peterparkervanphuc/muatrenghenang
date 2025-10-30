import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Manages game configuration from config.properties file
 * Singleton pattern for global access
 */
public class ConfigManager {
    private static ConfigManager instance;
    private Properties properties;

    private ConfigManager() {
        properties = new Properties();
        loadConfig();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadConfig() {
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
            if (input == null) {
                // Try loading from file system
                input = ConfigManager.class.getResourceAsStream("/config.properties");
            }
            if (input != null) {
                properties.load(input);
                input.close();
            } else {
                System.err.println("Config file not found, using defaults");
                loadDefaults();
            }
        } catch (IOException e) {
            System.err.println("Error loading config: " + e.getMessage());
            loadDefaults();
        }
    }

    private void loadDefaults() {
        // Window defaults
        properties.setProperty("window.width", "800");
        properties.setProperty("window.height", "600");
        properties.setProperty("window.title", "Arkanoid Game");

        // Game defaults
        properties.setProperty("game.initial.lives", "3");
        properties.setProperty("game.max.level", "5");
        properties.setProperty("game.fps", "60");

        // Sound defaults
        properties.setProperty("sound.enabled", "true");
    }

    // Getters for various config values
    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public double getDouble(String key, double defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    // Convenience methods for common settings
    public int getWindowWidth() {
        return getInt("window.width", 800);
    }

    public int getWindowHeight() {
        return getInt("window.height", 600);
    }

    public String getWindowTitle() {
        return getString("window.title", "Arkanoid Game");
    }

    public int getInitialLives() {
        return getInt("game.initial.lives", 3);
    }

    public int getMaxLevel() {
        return getInt("game.max.level", 5);
    }

    public boolean isSoundEnabled() {
        return getBoolean("sound.enabled", true);
    }

    public String getHighScoreFile() {
        return getString("highscore.file", "High Scores/High Scores.txt");
    }
}

