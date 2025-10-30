import java.awt.Color;

public enum BrickType {
    NORMAL(Color.ORANGE, true, false),
    UNBREAKABLE(Color.GRAY, false, false),
    FIREBALL_POWER(Color.RED, true, true),
    LASER_POWER(Color.BLUE, true, true);  // Đổi màu từ GREEN sang BLUE

    private final Color color;
    private final boolean isBreakable;
    private final boolean hasPowerUp;

    BrickType(Color color, boolean isBreakable, boolean hasPowerUp) {
        this.color = color;
        this.isBreakable = isBreakable;
        this.hasPowerUp = hasPowerUp;
    }

    public Color getColor() { return color; }
    public boolean isBreakable() { return isBreakable; }
    public boolean hasPowerUp() { return hasPowerUp; }
}