package org.bkl.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleFormatter implements Formatter {
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String format(LogRecord record) {
        String ts = df.format(new Date(record.timestamp));
        StringBuilder sb = new StringBuilder();
        sb.append(ts).append(" ")
                .append(record.level).append(" ")
                .append("[").append(record.loggerName).append("] ")
                .append(record.message);
        if (record.thrown != null) {
            sb.append(" - ").append(record.thrown.toString());
        }
        return sb.toString();
    }
}
