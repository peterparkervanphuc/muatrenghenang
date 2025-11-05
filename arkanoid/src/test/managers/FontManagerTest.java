package test.managers;

import managers.FontManager;
import org.junit.jupiter.api.Test;
import java.awt.Font;

import static org.junit.jupiter.api.Assertions.*;

public class FontManagerTest {
    //kiêmr tra font mặc định
    @Test
    void getGameFont_DefaultFallbackTest() {
        Font font = FontManager.getGameFont(16f);
        assertNotNull(font);
        assertEquals("Emulogic", font.getFontName()); //??? mịa cái Monospaced đâu? sao t thấy có Emulogic :)
        assertEquals(16f, font.getSize2D(), 0.01);
    }

    @Test
    void getGameFont_StyledFontTest() {
        Font font = FontManager.getGameFont(Font.ITALIC, 20f);
        assertNotNull(font);
        assertTrue(font.isItalic() || font.isBold());
        assertEquals(20f, font.getSize2D(), 0.01);
    }
}
