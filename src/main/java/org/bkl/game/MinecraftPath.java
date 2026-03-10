package org.bkl.game;

import org.bkl.util.SystemUtil;

import java.io.File;

public class MinecraftPath {

    public static String minecraftPath = null;
    public static String minecraftDefaultPath = null;

    public static String getMinecraftPath() {
        String osName = SystemUtil.getOsName();
        if (osName == null) {
            osName = SystemUtil.getOsName();
        }

        if (osName.contains("win")) {
            MinecraftPath.minecraftPath = System.getenv("APPDATA") + "/.minecraft";
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac")) {
            String home = System.getProperty("user.home");
            minecraftPath = home + "/.minecraft";

            File f = new File(minecraftPath + "/versions");
            if (!f.exists() || f.isDirectory() && f.list().length == 0) {
                minecraftPath = home + "/下载/.minecraft";
            }

        } else {
            minecraftPath = ".minecraft";
        }
        return minecraftPath;
    }

    public static String getDefaultMinecraftPath() {
        String osName = SystemUtil.getOsName();
        if (osName == null) {
            osName = SystemUtil.getOsName();
        }

        if (osName.contains("win")) {
            MinecraftPath.minecraftDefaultPath = System.getenv("APPDATA") + "/.minecraft";
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac")) {
            String home = System.getProperty("user.home");
            minecraftDefaultPath = home + "/.minecraft";
        } else {
            minecraftDefaultPath = ".minecraft";
        }
        return minecraftDefaultPath;
    }

}
