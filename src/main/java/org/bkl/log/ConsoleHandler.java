package org.bkl.log;

public class ConsoleHandler implements Handler {
    private final Formatter formatter;

    public ConsoleHandler(Formatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void publish(LogRecord record) {
        System.out.println(formatter.format(record));
        if (record.thrown != null) {
            record.thrown.printStackTrace(System.out);
        }
    }

    @Override
    public void flush() {}

    @Override
    public void close() {}
}
