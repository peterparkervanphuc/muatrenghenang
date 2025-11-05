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
     * Create a random colored brick (CHỈ CÁC LOẠI GẠCH THƯỜNG)
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A new entities.Brick instance with random color
     */
    public static Brick createRandomBrick(int x, int y) {
        int randomIndex = (int) (Math.random() * Brick.BrickType.values().length) + 1;
        while ( Brick.byId(randomIndex) == Brick.BrickType.SILVER ||
                !Brick.byId(randomIndex).isBreakable() ||
                randomIndex == 14 ||
                randomIndex == 13 ) {
            randomIndex = (int) (Math.random() * Brick.BrickType.values().length) + 1;
        }
        return createBrick(randomIndex, x, y);
    }

    /**
     * Create a silver brick (requires 3 hits)
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A new silver entities.Brick instance
     */
    public static Brick createSilverBrick(int x, int y) {
        return createBrick(11, x, y);
    }
}