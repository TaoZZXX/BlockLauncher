package org.bkl.log;

public class LogRecord {
    public final LogLevel level;
    public final String loggerName;
    public final String message;
    public final long timestamp;
    public final Throwable thrown;

    public LogRecord(LogLevel level, String loggerName, String message, Throwable thrown) {
        this.level = level;
        this.loggerName = loggerName;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        this.thrown = thrown;
    }
}
