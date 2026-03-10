package org.bkl.log;

public interface Handler {
    void publish(LogRecord record);
    void flush();
    void close();
}
