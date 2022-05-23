package it.polimi.ingsw;

import java.util.logging.Level;

public class Logger {
    public static void info(String message) {
        java.util.logging.Logger.getLogger(Logger.class.getPackageName()).info(message);
    }

    public static void warning(String message) {
        java.util.logging.Logger.getLogger(Logger.class.getPackageName()).warning(message);
    }

    public static void severe(String message) {
        java.util.logging.Logger.getLogger(Logger.class.getPackageName()).severe(message);
    }

    public static void setLevel(Level level) {
        java.util.logging.Logger.getLogger(Logger.class.getPackageName()).setLevel(level);
    }
}
