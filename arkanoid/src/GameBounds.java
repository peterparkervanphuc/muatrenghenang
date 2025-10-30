/**
 * Defines the playable area boundaries based on the background border
 */
public class GameBounds {
    // Border widths from the background images
    public static final int LEFT_BORDER = 26;
    public static final int RIGHT_BORDER = 40;
    public static final int TOP_BORDER = 20;
    public static final int BOTTOM_BORDER = 0;
    
    // Game window dimensions
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    
    // Playable area
    public static final int PLAY_LEFT = LEFT_BORDER;
    public static final int PLAY_RIGHT = WINDOW_WIDTH - RIGHT_BORDER;
    public static final int PLAY_TOP = TOP_BORDER;
    public static final int PLAY_BOTTOM = WINDOW_HEIGHT - BOTTOM_BORDER;
    
    public static final int PLAY_WIDTH = PLAY_RIGHT - PLAY_LEFT;
    public static final int PLAY_HEIGHT = PLAY_BOTTOM - PLAY_TOP;
}
