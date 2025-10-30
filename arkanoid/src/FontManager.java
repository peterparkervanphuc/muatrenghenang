import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Manages loading and providing the custom Arkanoid font
 */
public class FontManager {
    private static Font gameFont;
    
    /**
     * Load the Emulogic font for retro arcade style
     */
    public static Font getGameFont(float size) {
        if (gameFont == null) {
            try {
                var fontStream = FontManager.class.getClassLoader().getResourceAsStream("Fonts/emulogic.ttf");
                if (fontStream != null) {
                    gameFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(gameFont);
                    fontStream.close();
                } else {
                    System.err.println("Font file not found, using default");
                    gameFont = new Font("Monospaced", Font.BOLD, 12);
                }
            } catch (FontFormatException | IOException e) {
                System.err.println("Error loading font: " + e.getMessage());
                gameFont = new Font("Monospaced", Font.BOLD, 12);
            }
        }
        
        return gameFont.deriveFont(size);
    }
    
    /**
     * Get the default game font with bold style
     */
    public static Font getGameFont(int style, float size) {
        Font font = getGameFont(size);
        return font.deriveFont(style, size);
    }
}
