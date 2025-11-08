package entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import core.GameBounds; // <-- THÊM IMPORT NÀY

/**
 * Represents a brick in the Arkanoid game
 * OOP Principles Applied:
 * - Inheritance: Extends entities.GameObject (bricks don't move)
 * - Encapsulation: Private fields with controlled access
 * - Polymorphism: Enum for type safety, overrides update() and render()
 * - Abstraction: Hides hit detection and rendering complexity
 */
public class Brick extends GameObject {
    // Encapsulation: Private constants
    private static final int BRICK_WIDTH = 60;
    private static final int BRICK_HEIGHT = 20;

    // Encapsulation: Private fields
    private final BrickType type;
    private int hits;
    private BufferedImage brickImage;
    private double dx; // <-- THÊM BIẾN TỐC ĐỘ NGANG

    public int getHits() {return hits;}

    /**
     * Polymorphism: Enum for type-safe brick types
     * Each type has different properties (hits, points, image)
     */
    public enum BrickType {
        WHITE(1, 1, 50, "Sprites/Walls/WhiteWall.png", true),
        ORANGE(2, 1, 60, "Sprites/Walls/OrangeWall.png", true),
        LIGHT_BLUE(3, 1, 70, "Sprites/Walls/LightBlueWall.png", true),
        GREEN(4, 1, 80, "Sprites/Walls/GreenWall.png", true),
        RED(5, 1, 90, "Sprites/Walls/RedWall.png", true),
        BLUE(6, 1, 100, "Sprites/Walls/BlueWall.png", true),
        PURPLE(7, 1, 110, "Sprites/Walls/PurpleWall.png", true),
        YELLOW(8, 1, 120, "Sprites/Walls/YellowWall.png", true),
        SILVER(9, 3, 150, "Sprites/Walls/SilverWall.png", true), // Gạch Bạc VỠ ĐƯỢC
        GOLD(10, 1, 0, "Sprites/Walls/GoldWall.png", false), // Gạch Vàng BẤT TỬ
        MOVING_UNBREAKABLE_RF(11, 1, 0, "Sprites/Walls/MovingWall.png", false, 1.5), // Gạch di chuyển bất tử, sang phải, tốc độ 1.5
        MOVING_UNBREAKABLE_LF(12, 1, 0, "Sprites/Walls/MovingWall.png", false, 1.5), // Gạch di chuyển bất tử, sang trái, tốc độ 1.5
        MOVING_RF(13, 1, 100, "Sprites/Walls/SilverWall.png", true, 1.5), // Gạch di chuyển vỡ được, sang phải, tốc độ 1.5
        MOVING_LF(14, 1, 100, "Sprites/Walls/SilverWall.png", true, 1.5); // Gạch di chuyển vỡ được, sang trái, tốc độ 1.5

        private final int id;
        private final int maxHits;
        private final int points;
        private final String imagePath;
        private final boolean isBreakable; // <-- THÊM BIẾN NÀY
        private final double initialSpeed; // <-- THÊM BIẾN NÀY

        // Constructor cho gạch thường (không di chuyển)
        BrickType(int id, int maxHits, int points, String imagePath, boolean isBreakable) {
            this(id, maxHits, points, imagePath, isBreakable, 0.0); // Gọi constructor mới với tốc độ 0
        }

        // Constructor MỚI (có tốc độ)
        BrickType(int id, int maxHits, int points, String imagePath, boolean isBreakable, double initialSpeed) {
            this.id = id;
            this.maxHits = maxHits;
            this.points = points;
            this.imagePath = imagePath;
            this.isBreakable = isBreakable;
            this.initialSpeed = initialSpeed;
        }

        public int getId() {return id;}
        public boolean isBreakable() { return isBreakable; } // <-- THÊM HÀM NÀY
        public double getInitialSpeed() { return initialSpeed; } // <-- THÊM HÀM NÀY
        public int getMaxHits() { return maxHits; }
        public int getPoints() { return points; }
        public String getImagePath() { return imagePath; }
    }
    /**
     * Constructor: Create brick at position with specific type
     * Encapsulation: Initializes all private fields properly
     */
    public Brick(int x, int y, BrickType type) {
        super(x, y, BRICK_WIDTH, BRICK_HEIGHT);
        this.type = type;
        this.hits = type.getMaxHits();
        loadImage();

        // Gán tốc độ ban đầu - LF types di chuyển sang trái (dx âm)
        if (type == BrickType.MOVING_UNBREAKABLE_LF || type == BrickType.MOVING_LF) {
            this.dx = -type.getInitialSpeed(); // Âm = đi trái
        } else {
            this.dx = type.getInitialSpeed(); // Dương = đi phải
        }
    }

    public static BrickType byId(int id) {
        for (BrickType type : BrickType.values()) {
            if (type.getId() == id) return type;
        }
        return null;
    }

    private void loadImage() {
        try {
            var brickStream = getClass().getClassLoader().getResourceAsStream(type.getImagePath());
            if (brickStream != null) {
                brickImage = ImageIO.read(brickStream);
                brickStream.close();
            }
        } catch (Exception e) {
            System.err.println("Could not load brick image: " + e.getMessage());
        }
    }

    public void hit() {
        if (type.isBreakable()) { // <-- SỬA LOGIC: CHỈ TRỪ MÁU NẾU VỠ ĐƯỢC
            hits--;
        }
    }

    public boolean isDestroyed() {
        return hits <= 0;
    }

    public boolean isSilver() {
        return type == BrickType.SILVER;
    }

    public boolean isBreakable() { // <-- THÊM HÀM NÀY ĐỂ GAMERPANEL GỌI
        return type.isBreakable();
    }

    /**
     * Polymorphism: Override abstract update() method
     * Gạch có tốc độ > 0 sẽ di chuyển
     */
    @Override
    public void update() {
        if (type.getInitialSpeed() > 0) {
            // Di chuyển gạch (dx đã tự động có dấu âm/dương)
            setX(getX() + dx);

            // Đổi hướng khi chạm biên
            if (getX() <= GameBounds.PLAY_LEFT || getX() + getWidth() >= GameBounds.PLAY_RIGHT) {
                dx *= -1; // Đổi hướng
                // Chống kẹt - đẩy gạch ra khỏi tường
                if (getX() <= GameBounds.PLAY_LEFT) {
                    setX(GameBounds.PLAY_LEFT);
                }
                if (getX() + getWidth() >= GameBounds.PLAY_RIGHT) {
                    setX(GameBounds.PLAY_RIGHT - getWidth());
                }
            }
        }
        // Gạch không di chuyển (initialSpeed = 0) không làm gì
    }

    /**
     * Polymorphism: Override abstract render() method
     * Abstraction: Hides complex rendering logic
     */
    @Override
    public void render(Graphics2D g2d) {
        if (brickImage != null) {
            // Draw darker if damaged
            if (hits < type.getMaxHits() && type.isBreakable()) { // Thêm check isBreakable
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            }
            g2d.drawImage(brickImage, (int)getX(), (int)getY(), getWidth(), getHeight(), null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        } else {
            // Fallback color drawing
            Color color = getColorForType();
            g2d.setColor(color);
            g2d.fillRect((int)getX(), (int)getY(), getWidth(), getHeight());

            g2d.setColor(Color.BLACK);
            g2d.drawRect((int)getX(), (int)getY(), getWidth(), getHeight());
        }
    }

    private Color getColorForType() {
        switch (type) {
            case WHITE: return Color.WHITE;
            case ORANGE: return Color.ORANGE;
            case LIGHT_BLUE: return new Color(173, 216, 230);
            case GREEN: return Color.GREEN;
            case RED: return Color.RED;
            case BLUE: return Color.BLUE;
            case PURPLE: return new Color(128, 0, 128);
            case YELLOW: return Color.YELLOW;
            case SILVER: return Color.LIGHT_GRAY;
            case GOLD: return Color.YELLOW.darker(); // <-- THÊM VÀNG
            case MOVING_UNBREAKABLE_LF: return Color.DARK_GRAY.brighter(); // <-- THÊM DI CHUYỂN
            case MOVING_UNBREAKABLE_RF: return Color.DARK_GRAY.brighter();
            default: return Color.GRAY;
        }
    }

    // Encapsulation: Public getter for brick score value
    public int getPoints() { return type.getPoints(); }
    public BrickType getType() { return type; }
    public int getHitsRemaining() { return hits; }

    // Setter for restoring game state
    public void setHitsRemaining(int hits) {
        this.hits = hits;
    }

    // Getter/Setter for velocity - needed for save/load moving bricks
    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }
}