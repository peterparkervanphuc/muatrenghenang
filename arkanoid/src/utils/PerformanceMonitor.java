package utils;

import managers.ConfigManager;

/**
 * Performance monitoring utility for game optimization
 * Tracks FPS, memory usage, and game metrics
 */
public class PerformanceMonitor {
    private static PerformanceMonitor instance;

    private long frameCount = 0;
    private long lastFpsTime = System.currentTimeMillis();
    private int currentFps = 0;
    private long lastFrameTime = System.nanoTime();
    private double averageFrameTime = 0;

    // Memory tracking
    private Runtime runtime = Runtime.getRuntime();

    // Game metrics
    private int totalBricksDestroyed = 0;
    private int totalPowerupsCollected = 0;
    private long sessionStartTime = System.currentTimeMillis();

    private PerformanceMonitor() {}

    public static PerformanceMonitor getInstance() {
        if (instance == null) {
            instance = new PerformanceMonitor();
        }
        return instance;
    }

    /**
     * Call this every frame to track FPS
     */
    public void recordFrame() {
        frameCount++;
        long currentTime = System.currentTimeMillis();

        // Calculate FPS every second
        if (currentTime - lastFpsTime >= 1000) {
            currentFps = (int) frameCount;
            frameCount = 0;
            lastFpsTime = currentTime;

            // Log if FPS drops below threshold
            if (currentFps < ConfigManager.getInstance().getInt("game.fps", 60) * 0.8) {
                GameLogger.warning("Low FPS detected: " + currentFps);
            }
        }

        // Calculate frame time
        long currentFrameTime = System.nanoTime();
        double frameTime = (currentFrameTime - lastFrameTime) / 1_000_000.0; // Convert to ms
        averageFrameTime = averageFrameTime * 0.95 + frameTime * 0.05; // Smooth average
        lastFrameTime = currentFrameTime;
    }

    /**
     * Get current FPS
     */
    public int getCurrentFps() {
        return currentFps;
    }

    /**
     * Get average frame time in milliseconds
     */
    public double getAverageFrameTime() {
        return averageFrameTime;
    }

    /**
     * Get memory usage information
     */
    public String getMemoryUsage() {
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();

        return String.format("Memory: %d/%d MB (%.1f%%)",
            usedMemory / (1024 * 1024),
            maxMemory / (1024 * 1024),
            (usedMemory * 100.0) / maxMemory);
    }

    /**
     * Get used memory in MB
     */
    public long getUsedMemoryMB() {
        return (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
    }

    /**
     * Record game event
     */
    public void recordBrickDestroyed() {
        totalBricksDestroyed++;
    }

    public void recordPowerupCollected() {
        totalPowerupsCollected++;
    }

    /**
     * Get game session duration in seconds
     */
    public long getSessionDuration() {
        return (System.currentTimeMillis() - sessionStartTime) / 1000;
    }

    /**
     * Get comprehensive performance report
     */
    public String getPerformanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Performance Report ===\n");
        report.append(String.format("FPS: %d\n", currentFps));
        report.append(String.format("Avg Frame Time: %.2f ms\n", averageFrameTime));
        report.append(String.format("%s\n", getMemoryUsage()));
        report.append(String.format("Session Duration: %d seconds\n", getSessionDuration()));
        report.append(String.format("Bricks Destroyed: %d\n", totalBricksDestroyed));
        report.append(String.format("Powerups Collected: %d\n", totalPowerupsCollected));
        report.append("========================");
        return report.toString();
    }

    /**
     * Reset session metrics
     */
    public void resetSession() {
        totalBricksDestroyed = 0;
        totalPowerupsCollected = 0;
        sessionStartTime = System.currentTimeMillis();
        GameLogger.info("Performance metrics reset");
    }

    /**
     * Log performance report
     */
    public void logReport() {
        GameLogger.info(getPerformanceReport());
    }

    /**
     * Check if performance is good
     */
    public boolean isPerformanceGood() {
        int targetFps = ConfigManager.getInstance().getInt("game.fps", 60);
        long memoryThreshold = 100; // MB

        return currentFps >= targetFps * 0.9 && getUsedMemoryMB() < memoryThreshold;
    }
}

