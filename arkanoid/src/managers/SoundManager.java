package managers;

import utils.GameLogger;

import javax.sound.sampled.*;
import java.util.HashMap;

/**
 * Manages all sound effects and music for the game
 * Singleton pattern for global access
 */
public class SoundManager {
    private static SoundManager instance;
    private HashMap<String, Clip> soundClips;
    private Clip menuMusicClip;
    private boolean soundEnabled;

    // Sound file paths
    private static final String MENU_MUSIC = "Sounds/Menu.wav";
    private static final String GAME_START = "Sounds/Game Start.wav";
    private static final String GAME_OVER = "Sounds/Game Over.wav";
    private static final String WIN = "Sounds/Win.wav";
    private static final String HIGH_SCORES_MUSIC = "Sounds/High Scores.wav";
    private static final String WALL_HIT = "Sounds/Wall Hit.wav";
    private static final String SILVER_WALL_HIT = "Sounds/Silver Wall Hit.wav";
    private static final String SHIP_HIT = "Sounds/Ship Hit.wav";
    private static final String DEATH = "Sounds/Death.wav";
    private static final String LASER_BEAM = "Sounds/Laser Beam.wav";
    private static final String LASER_BEAM_HIT = "Sounds/Laser Beam Hit.wav";
    private static final String ENLARGE_POWERUP = "Sounds/Enlarge Powerup.wav";
    private static final String PLAYER_POWERUP = "Sounds/Player Powerup.wav";
    private static final String BREAK_POWERUP = "Sounds/Break Powerup.wav";

    private SoundManager() {
        soundClips = new HashMap<>();
        soundEnabled = ConfigManager.getInstance().isSoundEnabled();
        GameLogger.info("Initializing Sound Manager - Sound " + (soundEnabled ? "enabled" : "disabled"));
        loadSounds();
    }
    
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    private void loadSounds() {
        // Load all sound effects into memory
        loadSound("menu", MENU_MUSIC);
        loadSound("gameStart", GAME_START);
        loadSound("gameOver", GAME_OVER);
        loadSound("win", WIN);
        loadSound("highScores", HIGH_SCORES_MUSIC);
        loadSound("wallHit", WALL_HIT);
        loadSound("silverWallHit", SILVER_WALL_HIT);
        loadSound("shipHit", SHIP_HIT);
        loadSound("death", DEATH);
        loadSound("laserBeam", LASER_BEAM);
        loadSound("laserBeamHit", LASER_BEAM_HIT);
        loadSound("enlargePowerup", ENLARGE_POWERUP);
        loadSound("playerPowerup", PLAYER_POWERUP);
        loadSound("breakPowerup", BREAK_POWERUP);
    }
    
    private void loadSound(String name, String filepath) {
        try {
            var soundStream = getClass().getClassLoader().getResourceAsStream(filepath);
            if (soundStream != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundStream);

                // Get audio format info
                AudioFormat format = audioStream.getFormat();
                
                // Convert to PCM if needed
                AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    format.getSampleRate(),
                    16,
                    format.getChannels(),
                    format.getChannels() * 2,
                    format.getSampleRate(),
                    false
                );
                
                AudioInputStream decodedStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
                
                Clip clip = AudioSystem.getClip();
                clip.open(decodedStream);
                soundClips.put(name, clip);
                GameLogger.debug("Loaded sound: " + name);
            } else {
                GameLogger.warning("Sound file not found: " + filepath);
            }
        } catch (Exception e) {
            // Some WAV files may have unsupported formats - skip them gracefully
            GameLogger.warning("Could not load sound '" + name + "': " + e.getMessage());
        }
    }
    
    private void playSound(String name) {
        if (!soundEnabled) return;
        
        Clip clip = soundClips.get(name);
        if (clip != null) {
            try {
                // Reset to beginning
                clip.setFramePosition(0);
                clip.start();
            } catch (Exception e) {
                GameLogger.error("Error playing sound " + name + ": " + e.getMessage());
            }
        }
    }
    
    private void playSoundLoop(String name) {
        if (!soundEnabled) return;
        
        Clip clip = soundClips.get(name);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    private void stopSound(String name) {
        Clip clip = soundClips.get(name);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    
    private boolean isSoundPlaying(String name) {
        Clip clip = soundClips.get(name);
        return clip != null && clip.isRunning();
    }

    // Public methods for specific sounds
    public void playMenuMusic() {
        // Only restart if menu music is not already playing
        if (!isSoundPlaying("menu")) {
            stopAllMusic();
            playSoundLoop("menu");
        }
    }
    
    public void stopMenuMusic() {
        stopSound("menu");
    }
    
    public void playHighScoresMusic() {
        stopAllMusic();
        playSoundLoop("highScores");
    }
    
    public void playGameStartSound() {
        playSound("gameStart");
    }
    
    public void playGameOverSound() {
        stopAllMusic();
        playSound("gameOver");
    }
    
    public void playWinSound() {
        stopAllMusic();
        playSound("win");
    }

    public void playWallHitSound() {
        playSound("wallHit");
    }
    
    public void playSilverWallHitSound() {
        playSound("silverWallHit");
    }
    
    public void playShipHitSound() {
        playSound("shipHit");
    }
    
    public void playDeathSound() {
        playSound("death");
    }
    
    public void playLaserBeamSound() {
        playSound("laserBeam");
    }
    
    public void playLaserBeamHitSound() {
        playSound("laserBeamHit");
    }
    
    public void playEnlargePowerupSound() {
        playSound("enlargePowerup");
    }
    
    public void playPlayerPowerupSound() {
        playSound("playerPowerup");
    }
    
    public void playBreakPowerupSound() {
        playSound("breakPowerup");
    }
    
    public void stopAllMusic() {
        stopSound("menu");
        stopSound("highScores");
    }
    
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        if (!enabled) {
            stopAllMusic();
        }
    }
    
    public boolean isSoundEnabled() {
        return soundEnabled;
    }
}
