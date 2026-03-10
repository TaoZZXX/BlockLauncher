package org.bkl.game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * @author LongWei
 * @date 2025/9/15 12:08
 */
public class ClasspathBuilder {

    public static String buildClasspath(String version, String minecraftDir) throws IOException {

        StringBuilder classpath = new StringBuilder();

        Path startDir = Paths.get(minecraftDir + "/libraries");

        try (Stream<Path> pathStream = Files.walk(startDir)) {
            pathStream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".jar"))
                    .forEach(jarPath -> {
                        classpath.append(jarPath.toAbsolutePath().toString()).append(";\n");
                    });
        } catch (IOException e) {
            System.err.println("遍历目录时发生错误: " + e.getMessage());
        }

        File versionJar = new File(minecraftDir + "/versions/" + version + "/" + version + ".jar");
        classpath.append(versionJar.getAbsolutePath());


        return classpath.toString();
    }

}
