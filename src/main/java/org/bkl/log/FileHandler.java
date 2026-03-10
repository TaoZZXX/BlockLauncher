package org.bkl.log;

import java.io.*;
import java.nio.file.*;

public class FileHandler implements Handler {
    private final Formatter formatter;
    private final Path base;
    private final long maxBytes;
    private BufferedWriter writer;
    private Path current;

    public FileHandler(Path base, String filename, long maxBytes, Formatter formatter) throws IOException {
        this.base = base;
        this.maxBytes = maxBytes;
        this.formatter = formatter;
        Files.createDirectories(base);
        this.current = base.resolve(filename);
        this.writer = Files.newBufferedWriter(current, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    @Override
    public synchronized void publish(LogRecord record) {
        try {
            String line = formatter.format(record);
            writer.write(line);
            writer.newLine();
            if (record.thrown != null) {
                writer.write(record.thrown.toString());
                writer.newLine();
            }
            writer.flush();
            rotateIfNeeded();
        } catch (IOException e) {
            System.err.println("FileHandler write error: " + e.getMessage());
        }
    }

    private void rotateIfNeeded() throws IOException {
        long size = Files.size(current);
        if (size >= maxBytes) {
            writer.close();
            // simple rotation: rename with timestamp
            String rotated = current.getFileName().toString() + "." + System.currentTimeMillis();
            Files.move(current, base.resolve(rotated), StandardCopyOption.REPLACE_EXISTING);
            writer = Files.newBufferedWriter(current, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }

    @Override
    public synchronized void flush() {
        try { writer.flush(); } catch (IOException ignored) {}
    }

    @Override
    public synchronized void close() {
        try { writer.close(); } catch (IOException ignored) {}
    }
}