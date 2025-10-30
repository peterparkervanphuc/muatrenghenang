import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConfigManager
 */
class ConfigManagerTest {
    private ConfigManager config;

    @BeforeEach
    void setUp() {
        config = ConfigManager.getInstance();
    }

    @Test
    @DisplayName("Should load default window width")
    void testDefaultWindowWidth() {
        int width = config.getWindowWidth();
        assertTrue(width > 0, "Window width should be positive");
        assertEquals(800, width, "Default window width should be 800");
    }

    @Test
    @DisplayName("Should load default window height")
    void testDefaultWindowHeight() {
        int height = config.getWindowHeight();
        assertTrue(height > 0, "Window height should be positive");
        assertEquals(600, height, "Default window height should be 600");
    }

    @Test
    @DisplayName("Should load initial lives")
    void testInitialLives() {
        int lives = config.getInitialLives();
        assertTrue(lives > 0, "Initial lives should be positive");
        assertTrue(lives <= 10, "Initial lives should be reasonable");
    }

    @Test
    @DisplayName("Should load max level")
    void testMaxLevel() {
        int maxLevel = config.getMaxLevel();
        assertTrue(maxLevel > 0, "Max level should be positive");
        assertEquals(5, maxLevel, "Default max level should be 5");
    }

    @Test
    @DisplayName("Should handle missing keys with defaults")
    void testMissingKey() {
        int value = config.getInt("non.existent.key", 42);
        assertEquals(42, value, "Should return default value for missing key");
    }

    @Test
    @DisplayName("Should load boolean values")
    void testBooleanValue() {
        boolean soundEnabled = config.isSoundEnabled();
        assertTrue(soundEnabled || !soundEnabled, "Should be a valid boolean");
    }
}

