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
     * @param type The type of brick to create
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A new entities.Brick instance of the specified type
     */
    public static Brick createBrick(Brick.BrickType type, int x, int y) {
        if (type == null) {
            throw new IllegalArgumentException("entities.Brick type cannot be null");
        }
        return new Brick(x, y, type);
    }

    /**
     * Create a brick from string type name (useful for level loading)
     * @param typeName Name of the brick type (e.g., "SILVER", "RED")
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A new entities.Brick instance
     */
    public static Brick createBrick(String typeName, int x, int y) {
        try {
            Brick.BrickType type = Brick.BrickType.valueOf(typeName.toUpperCase());
            return createBrick(type, x, y);
        } catch (IllegalArgumentException e) {
            System.err.println("Unknown brick type: " + typeName + ", using WHITE as default");
            return createBrick(Brick.BrickType.WHITE, x, y);
        }
    }

    /**
     * Create a random colored brick (CHỈ CÁC LOẠI GẠCH THƯỜNG)
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A new entities.Brick instance with random color
     */
    public static Brick createRandomBrick(int x, int y) {
        // Dùng java.util.List và java.util.ArrayList
        List<Brick.BrickType> randomTypes = new ArrayList<>();

        // Lọc ra các gạch "thường"
        for (Brick.BrickType type : Brick.BrickType.values()) {
            // Chỉ thêm gạch CÓ THỂ VỠ (isBreakable() == true)
            // VÀ KHÔNG PHẢI GẠCH BẠC
            if (type.isBreakable() && type != Brick.BrickType.SILVER) {
                randomTypes.add(type);
            }
        }

        // Nếu list rỗng (lỗi gì đó), trả về gạch WHITE
        if (randomTypes.isEmpty()) {
            return createBrick(Brick.BrickType.WHITE, x, y);
        }

        // Random từ list gạch thường đó
        int randomIndex = (int) (Math.random() * randomTypes.size());
        return createBrick(randomTypes.get(randomIndex), x, y);
    }

    /**
     * Create a silver brick (requires 3 hits)
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A new silver entities.Brick instance
     */
    public static Brick createSilverBrick(int x, int y) {
        return createBrick(Brick.BrickType.SILVER, x, y);
    }
}