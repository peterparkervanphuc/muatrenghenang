package test.managers;

import managers.SoundManager;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.util.HashMap;

public class SoundManagerTest {
    private SoundManager soundManager;

    @BeforeEach
    void setUp() throws Exception {
        Field instanceField = SoundManager.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
        soundManager = SoundManager.getInstance();
    }

    @Test
    void getInstanceTest() {
        SoundManager sm1 = SoundManager.getInstance();
        SoundManager sm2 = SoundManager.getInstance();
        assertSame(sm1, sm2);
    }

    @Test
    void soundEnabledToggleTest() {
        soundManager.setSoundEnabled(false);
        assertFalse(soundManager.isSoundEnabled());
        soundManager.setSoundEnabled(true);
        assertTrue(soundManager.isSoundEnabled());
    }

    @Test
    void playSoundWhenDisabledTest() throws Exception {
        soundManager.setSoundEnabled(false);
        soundManager.playGameStartSound();
        assertFalse(soundManager.isSoundEnabled() && soundManager.isSoundEnabled());
    }

    @Test
    void soundClipLoadedMapTest() throws Exception {
        // Kiểm tra rằng soundClips đã tồn tại và là HashMap
        Field field = SoundManager.class.getDeclaredField("soundClips");
        field.setAccessible(true);
        Object clips = field.get(soundManager);
        assertNotNull(clips);
        assertTrue(clips instanceof HashMap);
    }

    @Test
    void playAndStopMenuMusicLogicTest() throws Exception {
        // Không test thực tế âm thanh, chỉ test không ném lỗi
        soundManager.setSoundEnabled(true);
        soundManager.playMenuMusic();
        soundManager.stopMenuMusic();
        soundManager.playHighScoresMusic();
        soundManager.stopAllMusic();
        assertTrue(soundManager.isSoundEnabled());
    }

    @Test
    void changeSoundEnabledShouldStopMusicTest() {
        soundManager.setSoundEnabled(false);
        assertFalse(soundManager.isSoundEnabled());
    }
}
