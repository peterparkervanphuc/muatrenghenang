package test.managers;

import managers.ConfigManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConfigManagerTest {
    private ConfigManager configManager;

    @BeforeEach
    void setUp() {
        configManager = ConfigManager.getInstance();
    }

    //Test khởi tạo giá trị ban đầu
    @Test
    void defaultConfigValuesTest() {
        // Window Init
        assertEquals(800, configManager.getWindowWidth());
        assertEquals(600, configManager.getWindowHeight());
        assertEquals("Arkanoid Game", configManager.getWindowTitle());

        // Setting Init
        assertEquals(5, configManager.getInitialLives());
        assertEquals(18, configManager.getMaxLevel());

        // Sound Init
        assertTrue(configManager.isSoundEnabled());
    }

    //Kiểm tra từng kiểu giá trị cho vào, nếu không hợp lệ thì dùng giá trị mặc định.
    @Test
    void getDataTypeTest() {
        int width = configManager.getInt("window.width", 999);
        int invalid = configManager.getInt("nonexistent.key", 999);
        assertEquals(800, width);
        assertEquals(999, invalid);

        double fps = configManager.getDouble("game.fps", 0.0);
        double missing = configManager.getDouble("invalid.key", 42.5);
        assertEquals(60.0, fps);
        assertEquals(42.5, missing);

        boolean soundEnabled = configManager.getBoolean("sound.enabled", false);
        boolean missingb = configManager.getBoolean("missing.key", true);
        assertTrue(soundEnabled);
        assertTrue(missingb);

        String title = configManager.getString("window.title", "Unknown");
        String missings = configManager.getString("unknown.key", "Fallback");
        assertEquals("Arkanoid Game", title);
        assertEquals("Fallback", missings);
    }

    @Test
    void getHighScoreFileTest() {
        String path = configManager.getHighScoreFile();
        assertEquals("High Scores/High Scores.txt", path);
    }
}
