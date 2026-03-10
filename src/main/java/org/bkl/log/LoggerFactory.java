package org.bkl.log;

import java.io.IOException;
import java.nio.file.Paths;

public class LoggerFactory {
    public static Logger getLogger(String name) {
        Logger logger = new Logger(name);
        Formatter fmt = new SimpleFormatter();
        logger.addHandler(new ConsoleHandler(fmt));
        try {
            logger.addHandler(new FileHandler(Paths.get("logs"), "blk.log", 10 * 1024 * 1024, fmt));
        } catch (IOException e) {
            System.err.println("无法创建文件日志: " + e.getMessage());
        }
        return logger;
    }
}
