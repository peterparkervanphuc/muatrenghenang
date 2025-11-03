package core;

import entities.Brick;
import factories.BrickFactory;
import java.util.ArrayList;

/**
 * Creates and loads different levels with brick patterns
 */
public class LevelManager {

    /**
     * Load bricks for a specific level
     */
    public static ArrayList<Brick> loadLevel(int level) {
        ArrayList<Brick> bricks = new ArrayList<>();

        switch (level) {
            case 1:
                bricks = createLevel1();
                break;
            case 2:
                bricks = createLevel2();
                break;
            case 3:
                bricks = createLevel3();
                break;
            case 4:
                bricks = createLevel4();
                break;
            case 5:
                bricks = createLevel5();
                break;
            default:
                bricks = createLevel1();
        }

        return bricks;
    }

    /**
     * Level 1: Simple rows (ĐÃ SỬA ĐỂ TEST)
     */
    private static ArrayList<Brick> createLevel1() {
        ArrayList<Brick> bricks = new ArrayList<>();
        int startX = GameBounds.PLAY_LEFT + 5;
        int startY = GameBounds.PLAY_TOP + 40;
        int cols = 11;
        int rows = 6;

        Brick.BrickType[] colors = {
                Brick.BrickType.RED,
                Brick.BrickType.ORANGE,
                Brick.BrickType.YELLOW, // Hàng này (row 2) sẽ bị thay thế
                Brick.BrickType.GREEN,
                Brick.BrickType.BLUE,
                Brick.BrickType.PURPLE
        };

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = startX + col * 60;
                int y = startY + row * 22;

                // === LOGIC MỚI ĐỂ TEST ===

                // HÀNG 2 (index 2) LÀ HÀNG TEST GẠCH DI CHUYỂN
                if (row == 2) {
                    // Chỉ tạo 1 viên gạch di chuyển ở giữa hàng (cột 5)
                    if (col == 5) {
                        bricks.add(BrickFactory.createBrick(Brick.BrickType.MOVING_UNBREAKABLE, x, y));
                    }
                    // Bỏ qua các cột khác -> hàng này sẽ trống
                }
                // HÀNG GẠCH GOLD (hàng 5)
                else if (row == rows - 1 && (col == 4 || col == 5)) {
                    bricks.add(BrickFactory.createBrick(Brick.BrickType.GOLD, x, y));
                }
                // CÁC HÀNG BÌNH THƯỜNG KHÁC (0, 1, 3, 4)
                else {
                    bricks.add(BrickFactory.createBrick(colors[row], x, y));
                }
                // =========================
            }
        }

        return bricks;
    }

    /**
     * Level 2: Pyramid pattern
     */
    private static ArrayList<Brick> createLevel2() {
        ArrayList<Brick> bricks = new ArrayList<>();
        int startX = GameBounds.PLAY_LEFT + 60;
        int startY = GameBounds.PLAY_TOP + 60;

        Brick.BrickType[] colors = {
                Brick.BrickType.WHITE,
                Brick.BrickType.ORANGE,
                Brick.BrickType.LIGHT_BLUE,
                Brick.BrickType.GREEN,
                Brick.BrickType.RED,
                Brick.BrickType.BLUE,
                Brick.BrickType.PURPLE,
                Brick.BrickType.YELLOW
        };

        // Create pyramid
        for (int row = 0; row < 8; row++) {
            int bricksInRow = 10 - row;
            for (int col = 0; col < bricksInRow; col++) {
                int x = startX + row * 30 + col * 60;
                int y = startY + row * 22;
                bricks.add(BrickFactory.createBrick(colors[row], x, y));
            }
        }

        return bricks;
    }

    /**
     * Level 3: Diamond pattern with silver bricks
     */
    private static ArrayList<Brick> createLevel3() {
        ArrayList<Brick> bricks = new ArrayList<>();
        int centerX = GameBounds.PLAY_LEFT + GameBounds.PLAY_WIDTH / 2;
        int centerY = GameBounds.PLAY_TOP + 160;

        // Create diamond shape
        int[] rowWidths = {1, 3, 5, 7, 9, 7, 5, 3, 1};
        Brick.BrickType[] colors = {
                Brick.BrickType.RED,
                Brick.BrickType.ORANGE,
                Brick.BrickType.YELLOW,
                Brick.BrickType.GREEN,
                Brick.BrickType.SILVER, // Center row is silver
                Brick.BrickType.GREEN,
                Brick.BrickType.YELLOW,
                Brick.BrickType.ORANGE,
                Brick.BrickType.RED
        };

        for (int row = 0; row < rowWidths.length; row++) {
            int width = rowWidths[row];
            int startX = centerX - (width * 60) / 2;
            int y = centerY - 100 + row * 22;

            for (int col = 0; col < width; col++) {
                int x = startX + col * 60;
                bricks.add(BrickFactory.createBrick(colors[row], x, y));
            }
        }

        return bricks;
    }

    /**
     * Level 4: Checkerboard pattern
     */
    private static ArrayList<Brick> createLevel4() {
        ArrayList<Brick> bricks = new ArrayList<>();
        int startX = GameBounds.PLAY_LEFT + 5;
        int startY = GameBounds.PLAY_TOP + 20;
        int cols = 11;
        int rows = 8;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Checkerboard pattern
                if ((row + col) % 2 == 0) {
                    int x = startX + col * 60;
                    int y = startY + row * 22;

                    // Mix of colors with some silver bricks
                    Brick.BrickType type;
                    if (row >= 3 && row <= 4 && col >= 4 && col <= 7) {
                        type = Brick.BrickType.SILVER; // Silver in center
                    } else {
                        Brick.BrickType[] colors = {
                                Brick.BrickType.BLUE,
                                Brick.BrickType.GREEN,
                                Brick.BrickType.YELLOW,
                                Brick.BrickType.ORANGE
                        };
                        type = colors[(row + col) % colors.length];
                    }

                    bricks.add(BrickFactory.createBrick(type, x, y));
                }
            }
        }

        return bricks;
    }

    /**
     * Level 5: Complex pattern (final level)
     */
    private static ArrayList<Brick> createLevel5() {
        ArrayList<Brick> bricks = new ArrayList<>();
        int startX = GameBounds.PLAY_LEFT + 5;
        int startY = GameBounds.PLAY_TOP + 20;

        // Top rows - all silver (challenging!)
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 12; col++) {
                int x = startX + col * 60;
                int y = startY + row * 22;
                bricks.add(BrickFactory.createSilverBrick(x, y));
            }
        } // <-- ĐÂY LÀ DẤU NGOẶC BỊ THIẾU 1

        // === PHẦN CODE BỊ CẮT MẤT CỦA BẠN ===

        // Middle section - colored spiral pattern
        int[][] spiralPattern = {
                {1,1,1,1,1,1,1,1,1,1,1,1},
                {1,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,1,1,1,1,1,1,1,1,0,1},
                {1,0,1,0,0,0,0,0,0,1,0,1},
                {1,0,1,0,1,1,1,1,0,1,0,1},
                {1,0,1,0,1,0,0,1,0,1,0,1},
                {1,0,1,0,1,1,1,1,0,1,0,1},
                {1,0,1,0,0,0,0,0,0,1,0,1},
                {1,0,1,1,1,1,1,1,1,1,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1}
        };

        Brick.BrickType[] colors = {
                Brick.BrickType.RED,
                Brick.BrickType.ORANGE,
                Brick.BrickType.YELLOW,
                Brick.BrickType.GREEN,
                Brick.BrickType.BLUE,
                Brick.BrickType.PURPLE
        };

        for (int row = 0; row < spiralPattern.length; row++) {
            for (int col = 0; col < spiralPattern[row].length; col++) {
                if (spiralPattern[row][col] == 1) {
                    int x = startX + col * 60;
                    int y = startY + 50 + row * 22;
                    Brick.BrickType color = colors[(row + col) % colors.length];
                    bricks.add(BrickFactory.createBrick(color, x, y));
                }
            }
        }

        return bricks;
    }
}