package it.polimi.ingsw;

/**
 * Wrapper around {@link java.util.logging.Logger} for easy logging
 */
public class Logger {
    private static boolean enabled = false;

    /**
     * If the logger is enabled, logs a message with level: info
     *
     * @param message the message to log
     */
    public static void info(String message) {
        if (enabled) java.util.logging.Logger.getLogger(Logger.class.getPackageName()).info(message);
    }

    /**
     * If the logger is enabled, logs a message with level: warning
     *
     * @param message the message to log
     */
    public static void warning(String message) {
        if (enabled) java.util.logging.Logger.getLogger(Logger.class.getPackageName()).warning(message);
    }

    /**
     * If the logger is enabled, logs a message with level: severe
     *
     * @param message the message to log
     */
    public static void severe(String message) {
        if (enabled) java.util.logging.Logger.getLogger(Logger.class.getPackageName()).severe(message);
    }

    /**
     * Enables or disables the logger
     *
     * @param enabled if true, enables the logger. if false, disables it
     */
    public static void enable(boolean enabled) {
        Logger.enabled = enabled;
    }
}
