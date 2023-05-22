package dev.finleywood.simplesmtpserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger class, can be instantiated in any class
 */
public class Logger {

    private Class<?> instantiatedClass;
    public Logger(Class<?> instantiatedClass) {
        this.instantiatedClass = instantiatedClass;
    }

    /**
     * Formats the string to output using System.out.println()
     * @param logLevel
     * @param message
     */
    private void outputMessage(LOG_LEVEL logLevel, String message) {
        // Constructs log message
        StringBuilder logSB = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        logSB.append("["+logLevel.name()+"]");
        logSB.append(" ");
        logSB.append("["+dtf.format(ldt).toString()+"]");
        logSB.append(" ");
        logSB.append("[" + this.instantiatedClass + "]").append(" ");
        logSB.append(message);
        System.out.println(logSB.toString());
    }

    /**
     * Outputs an informational log message
     * @param message
     */
    public void info(String message) {
        this.outputMessage(LOG_LEVEL.INFO, message);
    }


    /**
     * Outputs a warning log message
     * @param message
     */
    public void warning(String message) {
        this.outputMessage(LOG_LEVEL.WARNING, message);
    }


    /**
     * Outputs an error log message
     * @param message
     */
    public void error(String message) {
        this.outputMessage(LOG_LEVEL.ERROR, message);
    }


    /**
     * Log levels
     */
    private enum LOG_LEVEL {
        INFO,
        WARNING,
        ERROR,
    }
}
