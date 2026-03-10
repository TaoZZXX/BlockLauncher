package org.bkl.game;

import org.bkl.log.Logger;
import org.bkl.log.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LongWei
 * @date 2025/9/15 10:59
 */
public class MCVersionChecker {
    private final static Logger log = LoggerFactory.getLogger(MCVersionChecker.class.getName());

    private static List<File> versionFolder = null;
    private static List<String> versionNameList = null;

    public static void refresh () {

        MCVersionChecker.versionFolder = new ArrayList<File>();
        MCVersionChecker.versionNameList = new ArrayList<String>();

        String versionFolderPath = MinecraftPath.getMinecraftPath() + "/versions";
        File mcDir = new File(versionFolderPath);

        if (!mcDir.exists() || !mcDir.isDirectory()) {
            log.info("Minecraft installation not found");
            return;
        }

        File[] mcVersionFolder = mcDir.listFiles(File::isDirectory);
        if (mcVersionFolder == null || mcVersionFolder.length == 0) {
            log.info("Minecraft installation not found");
            return;
        }

        for (File versionFolder : mcVersionFolder) {
            MCVersionChecker.versionNameList.add(versionFolder.getName());
        }

        MCVersionChecker.versionFolder = List.of(mcVersionFolder);
        log.info("Minecraft version list: " + MCVersionChecker.versionNameList.toString());
    }

    public static List<File> getVersionFolder() {
        if (MCVersionChecker.versionFolder == null || MCVersionChecker.versionNameList == null) {
            refresh();
        }
        return MCVersionChecker.versionFolder;
    }

    public static List<String> getVersionNameList() {
        if (MCVersionChecker.versionFolder == null || MCVersionChecker.versionNameList == null) {
            refresh();
        }
        return MCVersionChecker.versionNameList;
    }




}
