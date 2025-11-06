package factories;

import entities.Powerup;

/**
 * Factory Pattern for creating different types of power-ups
 * Design Pattern: Factory Method
 * Purpose: Centralized power-up creation, easier to maintain and extend
 */
public class PowerUpFactory {

    /**
     * Create a power-up of the specified type at given position
     * @param type The type of power-up to create
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A new entities.Powerup instance of the specified type
     */
    public static Powerup createPowerUp(Powerup.PowerupType type, double x, double y) {
        if (type == null) {
            throw new IllegalArgumentException("PowerUp type cannot be null");
        }
        return new Powerup((int)x, (int)y, type);
    }

    /**
     * Create a random power-up at given position with balanced drop rates
     * Drop rates based on power tier system:
     * - Tier S (Rare): PLAYER 8%, BREAK 7% = 15% total
     * - Tier A (Uncommon): SLOW 20%, DUPLICATE 15% = 35% total
     * - Tier B (Common): LASER 19%, CATCH 16%, ENLARGE 15% = 50% total
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A new random entities.Powerup instance with weighted probability
     */
    public static Powerup createRandomPowerUp(double x, double y) {
        double random = Math.random() * 100; // 0-100 for percentage calculation

        // Tier B - Common (50% total)
        if (random < 15) {
            return createPowerUp(Powerup.PowerupType.ENLARGE, x, y); // 15%
        } else if (random < 31) {
            return createPowerUp(Powerup.PowerupType.CATCH, x, y); // 16%
        } else if (random < 50) {
            return createPowerUp(Powerup.PowerupType.LASER, x, y); // 19%
        }
        // Tier A - Uncommon (35% total)
        else if (random < 70) {
            return createPowerUp(Powerup.PowerupType.SLOW, x, y); // 20%
        } else if (random < 85) {
            return createPowerUp(Powerup.PowerupType.DUPLICATE, x, y); // 15%
        }
        // Tier S - Rare (15% total)
        else if (random < 93) {
            return createPowerUp(Powerup.PowerupType.PLAYER, x, y); // 8%
        } else {
            return createPowerUp(Powerup.PowerupType.BREAK, x, y); // 7%
        }
    }

    /**
     * Create a power-up from brick destruction with probability
     * @param brickX The x-coordinate of the destroyed brick
     * @param brickY The y-coordinate of the destroyed brick
     * @param dropChance The probability (0.0 to 1.0) of dropping a power-up
     * @return A new entities.Powerup instance or null if no drop
     */
    public static Powerup createPowerUpFromBrick(double brickX, double brickY, double dropChance) {
        if (Math.random() < dropChance) {
            return createRandomPowerUp(brickX, brickY);
        }
        return null;
    }

    /**
     * Create a specific beneficial power-up (for bonus/special events)
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A beneficial entities.Powerup instance (ENLARGE, PLAYER, or LASER)
     */
    public static Powerup createBonusPowerUp(double x, double y) {
        Powerup.PowerupType[] beneficialTypes = {
            Powerup.PowerupType.ENLARGE,
            Powerup.PowerupType.PLAYER,
            Powerup.PowerupType.LASER,
            Powerup.PowerupType.CATCH,
            Powerup.PowerupType.DUPLICATE
        };
        int randomIndex = (int) (Math.random() * beneficialTypes.length);
        return createPowerUp(beneficialTypes[randomIndex], x, y);
    }
}

