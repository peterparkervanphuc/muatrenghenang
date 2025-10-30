import java.io.IOException;
import java.util.logging.*;

/**
 * Simple logging utility for the game
 * Provides consistent logging across all game components
 */
public class GameLogger {
    private static final Logger logger = Logger.getLogger("ArkanoidGame");
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;

        try {
            // Remove default console handler
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers) {
                rootLogger.removeHandler(handler);
            }

            // Create console handler with custom format
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(new SimpleFormatter() {
                private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

                @Override
                public synchronized String format(LogRecord lr) {
                    return String.format(format,
                            new java.util.Date(lr.getMillis()),
                            lr.getLevel().getLocalizedName(),
                            lr.getMessage()
                    );
                }
            });

            logger.addHandler(consoleHandler);
            logger.setLevel(Level.INFO);

            // Add file handler (optional)
            try {
                FileHandler fileHandler = new FileHandler("arkanoid.log", true);
                fileHandler.setFormatter(consoleHandler.getFormatter());
                fileHandler.setLevel(Level.ALL);
                logger.addHandler(fileHandler);
            } catch (IOException e) {
                logger.warning("Could not create log file: " + e.getMessage());
            }

            initialized = true;
            logger.info("Game logger initialized");

        } catch (Exception e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warning(String message) {
        logger.warning(message);
    }

    public static void error(String message) {
        logger.severe(message);
    }

    public static void error(String message, Throwable t) {
        logger.log(Level.SEVERE, message, t);
    }

    public static void debug(String message) {
        if (ConfigManager.getInstance().getBoolean("debug.mode", false)) {
            logger.fine(message);
        }
    }
}

