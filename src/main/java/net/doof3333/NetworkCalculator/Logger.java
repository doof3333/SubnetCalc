package net.doof3333.NetworkCalculator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Mostly copied and simplified from another app I made.
 * Logs to System.out. Actual output isn't checked and should support ANSI formatting.
 */
public class Logger {
    // Everything equal or below this level will be logged by default:
    private static final Level MAX_LEVEL = Level.DEBUG;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "HH:mm:ss", new Locale("de", "DE"));

    private final String className;
    private final Level level;
    public Logger(String className) {
        this(className, MAX_LEVEL);
    }

    public Logger(String className, Level level) {
        this.className = className;
        this.level = level;
    }

    public void info(String message) {
        log(message, Level.INFO);
    }

    public void warn(String message) {
        log(message, Level.WARNING);
    }

    public void error(String message) {
        log(message, Level.SEVERE);
    }

    public void debug(String message) {
        log(message, Level.DEBUG);
    }

    public void log(String message, Level level) {
        if (level.value <= this.level.value) {
            System.out.println(format(message, level, className));
        }
    }

    private String format(String message, Level level, String className) {
        String preColor;
        if (level == Level.SEVERE) {
            preColor = level.color;
        } else {
            preColor = "\u001B[0m";
        }

        return String.format("%s[%s|%s|T:%s|C:%s] %s%s%s",
                preColor,
                dateFormat.format(new Date()),
                level.name(),
                Thread.currentThread().getName(),
                className,
                level.color,
                message,
                "\u001B[0m");
    }

    public enum Level {
        DEBUG("\u001B[36m",100),
        INFO("\u001B[32m", 50),
        WARNING("\u001B[33m", 20),
        SEVERE("\u001b[31;1m", 10);
        final String color;
        final int value;
        Level(String color, int value) {
            this.color = color;
            this.value = value;
        }
    }

}
