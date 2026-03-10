package org.bkl.download;

import org.bkl.game.MinecraftPath;
import org.bkl.log.Logger;
import org.bkl.log.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameManage {
    private final static Logger log = LoggerFactory.getLogger(GameManage.class.getName());


    public static void downloaderGame(String mcVersion) {
        Download4VersionManifest.downloadVersionManifest(mcVersion);
        Download4VersionManifest.downloadClientJar(mcVersion);
        Download4VersionManifest.downloadLibraries(mcVersion);
        Download4VersionManifest.downloadAssets(mcVersion);
    }

    public static boolean deleteGame(String mcVersion) {
        Path verDir = Paths.get(MinecraftPath.getMinecraftPath(), "versions", mcVersion);
        if (!Files.exists(verDir) || !Files.isDirectory(verDir)) {
            return false;
        }

        AtomicBoolean anyDeleted = new AtomicBoolean(false);

        try {
            Files.walkFileTree(verDir, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        if (Files.deleteIfExists(file)) {
                            anyDeleted.set(true);
                        }
                    } catch (IOException e) {
                        log.info("fail delete file: " + file + " -> " + e.getMessage());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    try {
                        if (Files.deleteIfExists(dir)) {
                            anyDeleted.set(true);
                        }
                    } catch (IOException e) {
                        log.info("fail delete dir: " + dir + " -> " + e.getMessage());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            log.info("递归删除失败: " + mcVersion + " -> " + e.getMessage());
            return false;
        }

        return anyDeleted.get();
    }
}
