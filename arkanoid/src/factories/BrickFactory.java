package factories;

import entities.Brick;
import java.util.List; // <-- THÊM IMPORT NÀY
import java.util.ArrayList; // <-- THÊM IMPORT NÀY

/**
 * Factory Pattern for creating different types of bricks
 * Design Pattern: Factory Method
 * Purpose: Centralized brick creation, easier to maintain and extend
 */
public class BrickFactory {

    /**
     * Create a brick of the specified type at given position
     * @param id The id of brick to create
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A new entities.Brick instance of the specified type
     */
    public static Brick createBrick(int id, int x, int y) {
        if (id == 0) {
            return null;
        } else if ( id >= 1 && id <= 14) {
            return new Brick(x, y, Brick.byId(id));
        } else if ( id == 15) {
            return createRandomBrick(x, y);
        } else {
            throw new IllegalArgumentException("Invalid brick ID: " + id);
        }
    }

    /**
     * Create a random colored brick (CHỈ CÁC LOẠI GẠCH THƯỜNG VỠ ĐƯỢC)
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A new entities.Brick instance with random color
     */
    public static Brick createRandomBrick(int x, int y) {
        // Danh sách ID gạch thường vỡ được (không bao gồm SILVER, GOLD, MOVING)
        int[] validIds = {1, 2, 3, 4, 5, 6, 7, 8}; // WHITE, ORANGE, LIGHT_BLUE, GREEN, RED, BLUE, PURPLE, YELLOW

        int randomIndex = (int) (Math.random() * validIds.length);
        return createBrick(validIds[randomIndex], x, y);
    }

    /**
     * Create a silver brick (requires 3 hits)
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A new silver entities.Brick instance
     */
    public static Brick createSilverBrick(int x, int y) {
        return createBrick(9, x, y);  // SILVER brick ID = 9
    }
}