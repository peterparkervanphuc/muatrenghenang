package effects;

/**
 * Camera shake effect for game feel
 * Creates screen shake effect when triggered
 */
public class CameraShake {
    private double offsetX;
    private double offsetY;
    private int intensity;
    private int duration;
    private int currentTime;
    private boolean active;

    public CameraShake() {
        this.offsetX = 0;
        this.offsetY = 0;
        this.intensity = 0;
        this.duration = 0;
        this.currentTime = 0;
        this.active = false;
    }

    /**
     * Start a shake effect
     * @param intensity Shake intensity (1-10)
     * @param duration Duration in frames (typically 10-30)
     */
    public void shake(int intensity, int duration) {
        this.intensity = intensity;
        this.duration = duration;
        this.currentTime = 0;
        this.active = true;
    }

    /**
     * Update shake effect (call every frame)
     */
    public void update() {
        if (!active) {
            offsetX = 0;
            offsetY = 0;
            return;
        }

        currentTime++;

        if (currentTime >= duration) {
            active = false;
            offsetX = 0;
            offsetY = 0;
            return;
        }

        // Calculate decay (shake reduces over time)
        double decay = 1.0 - ((double)currentTime / duration);
        double currentIntensity = intensity * decay;

        // Generate random offset using sine/cosine for smooth shake
        double angle = Math.random() * Math.PI * 2;
        offsetX = Math.cos(angle) * currentIntensity;
        offsetY = Math.sin(angle) * currentIntensity;
    }

    /**
     * Get X offset for rendering
     */
    public int getOffsetX() {
        return (int)offsetX;
    }

    /**
     * Get Y offset for rendering
     */
    public int getOffsetY() {
        return (int)offsetY;
    }

    /**
     * Check if shake is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Stop shake immediately
     */
    public void stop() {
        active = false;
        offsetX = 0;
        offsetY = 0;
    }
}

