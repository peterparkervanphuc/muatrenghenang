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
     * @return A new Brick instance of the specified type
     */
    public static Brick createBrick(Brick.BrickType type, int x, int y) {
        if (type == null) {
            throw new IllegalArgumentException("Brick type cannot be null");
        }
        return new Brick(x, y, type);
    }

    /**
     * Create a brick from string type name (useful for level loading)
     * @param typeName Name of the brick type (e.g., "SILVER", "RED")
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A new Brick instance
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
     * Create a random colored brick (excluding silver)
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A new Brick instance with random color
     */
    public static Brick createRandomBrick(int x, int y) {
        Brick.BrickType[] types = Brick.BrickType.values();
        // Exclude SILVER for random generation (it's special)
        int randomIndex = (int) (Math.random() * (types.length - 1));
        return createBrick(types[randomIndex], x, y);
    }

    /**
     * Create a silver brick (requires 2 hits)
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A new silver Brick instance
     */
    public static Brick createSilverBrick(int x, int y) {
        return createBrick(Brick.BrickType.SILVER, x, y);
    }
}

