package org.bkl.log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Logger {
    private final String name;
    private volatile LogLevel level = LogLevel.INFO;
    private final List<Handler> handlers = new CopyOnWriteArrayList<>();

    public Logger(String name) { this.name = name; }

    public void setLevel(LogLevel level) { this.level = level; }
    public void addHandler(Handler h) { handlers.add(h); }

    private void log(LogLevel lvl, String msg, Throwable t) {
        if (!lvl.enabledFor(level)) return;
        LogRecord r = new LogRecord(lvl, name, msg, t);
        for (Handler h : handlers) h.publish(r);
    }

    public void info(String msg) { log(LogLevel.INFO, msg, null); }
    public void debug(String msg) { log(LogLevel.DEBUG, msg, null); }
    public void warn(String msg) { log(LogLevel.WARN, msg, null); }
    public void error(String msg, Throwable t) { log(LogLevel.ERROR, msg, t); }
}
