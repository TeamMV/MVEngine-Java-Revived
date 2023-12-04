package dev.mv.engine.utils.logger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MVUtils logger. Used to log messages globally.
 *
 * @author Julian Hohenhausen
 */
public class Logger {
    private static LoggerOutput loggerOutput = null;

    private static boolean globalDebugMessages = false;
    private static boolean globalInfoMessages = true;
    private static boolean globalWarningMessages = true;
    private static String logFormat = "[${date}] <${level}> ${message}";
    private static String dateFormat = "dd/MM/yyyy 'at' HH:mm:ss.SSS";

    /**
     * Sets the global logger output.
     *
     * @param logOutput the new logger output.
     */
    public static void setLogOutput(LoggerOutput logOutput) {
        loggerOutput = logOutput;
    }

    /**
     * Sets the global logger output to an output stream.
     *
     * @param output the new output stream.
     */
    public static void setLogOutput(OutputStream output) {
        if (output == null) {
            loggerOutput = null;
            return;
        }
        loggerOutput = (logOutput, logLevel) -> {
            try {
                output.write(logOutput.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Log a debug message to the logger output, debug messages can be toggled, off by default.
     *
     * @param msg the message to log.
     */
    public static void debug(String msg) {
        if (globalDebugMessages) output(buildLog(msg, LogLevel.DEBUG), LogLevel.DEBUG);
    }

    /**
     * Log an info message to the logger output, info messages can be toggled, on by default.
     *
     * @param msg the message to log.
     */
    public static void info(String msg) {
        if (globalInfoMessages) output(buildLog(msg, LogLevel.INFO), LogLevel.INFO);
    }

    /**
     * Log a warning message to the logger output, warning messages can be toggled, on by default.
     *
     * @param msg the message to log.
     */
    public static void warn(String msg) {
        if (globalWarningMessages) output(buildLog(msg, LogLevel.WARN), LogLevel.WARN);
    }

    /**
     * Log an error message to the logger output, error messages cannot be toggled.
     *
     * @param msg the message to log.
     */
    public static void error(String msg) {
        output(buildLog(msg, LogLevel.ERROR), LogLevel.ERROR);
    }

    private static String buildLog(String msg, LogLevel logLevel) {
        if (msg == null) msg = "";
        String date = new SimpleDateFormat(dateFormat).format(new Date());
        String log = logFormat.replace("${date}", date).replace("${level}", logLevel.toString()).replace("${message}", msg);
        log += "\n";
        return log;
    }

    private static void output(String log, LogLevel logLevel) {
        if (loggerOutput != null) loggerOutput.output(log, logLevel);
    }

    public static LoggerOutput getLoggerOutput() {
        return loggerOutput;
    }

    public static void setLoggerOutput(LoggerOutput loggerOutput) {
        Logger.loggerOutput = loggerOutput;
    }

    public static boolean isGlobalDebugMessages() {
        return globalDebugMessages;
    }

    public static void setGlobalDebugMessages(boolean globalDebugMessages) {
        Logger.globalDebugMessages = globalDebugMessages;
    }

    public static boolean isGlobalInfoMessages() {
        return globalInfoMessages;
    }

    public static void setGlobalInfoMessages(boolean globalInfoMessages) {
        Logger.globalInfoMessages = globalInfoMessages;
    }

    public static boolean isGlobalWarningMessages() {
        return globalWarningMessages;
    }

    public static void setGlobalWarningMessages(boolean globalWarningMessages) {
        Logger.globalWarningMessages = globalWarningMessages;
    }

    public static String getLogFormat() {
        return logFormat;
    }

    /**
     * Sets the log format. Use a normal string, and insert ${date} for the date, ${level} for log level, and ${message} for the message.
     *
     * @param logFormat the new log format.
     */
    public static void setLogFormat(String logFormat) {
        Logger.logFormat = logFormat;
    }

    public static String getDateFormat() {
        return dateFormat;
    }

    public static void setDateFormat(String dateFormat) {
        Logger.dateFormat = dateFormat;
    }

    public enum LogLevel {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    /**
     * A wrapper class to allow setting the logger output.
     */
    public interface LoggerOutput {
        void output(String logOutput, LogLevel logLevel);
    }
}
