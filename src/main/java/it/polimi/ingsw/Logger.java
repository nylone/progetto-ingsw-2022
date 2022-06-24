package it.polimi.ingsw;

public class Logger {
    private static boolean enabled = false;

    public static void info(String message) {
        if (enabled) java.util.logging.Logger.getLogger(Logger.class.getPackageName()).info(message);
    }

    public static void warning(String message) {
        if (enabled) java.util.logging.Logger.getLogger(Logger.class.getPackageName()).warning(message);
    }

    public static void severe(String message) {
        if (enabled) java.util.logging.Logger.getLogger(Logger.class.getPackageName()).severe(message);
    }

    public static void enable(boolean enabled) {
        Logger.enabled = enabled;
    }
}
