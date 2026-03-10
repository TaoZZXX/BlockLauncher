package org.bkl.log;

public enum LogLevel {
    TRACE, DEBUG, INFO, WARN, ERROR;

    public boolean enabledFor(LogLevel current) {
        return this.ordinal() >= current.ordinal();
    }
}
