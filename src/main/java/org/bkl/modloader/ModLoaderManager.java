package org.bkl.modloader;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ModLoaderManager {
    private List<ModLoaderType> installed = new ArrayList<>();

    public ModLoaderManager() {
    }

    public static List<ModLoaderType> checkInstalledModLoaders(String mcVersion) {
        List<ModLoaderType> installed = new ArrayList<>();

        // 检查是否安装fabric
        if (FabricChecker.isFabricInstall(mcVersion)) {
            installed.add(ModLoaderType.FABRIC);
        }

        // 检查是否安装forge
        if (ForgeChecker.isForgeInstalled(mcVersion)) {
            installed.add(ModLoaderType.FORGE);
        }

        // 检查是否安装quilt
        if (QuiltChecker.isQuiltInstalled(mcVersion)) {
            installed.add(ModLoaderType.QUILT);
        }

        // 检查是否安装rift
        if (OptiFineChecker.isOptiFineInstalled(mcVersion)) {
            installed.add(ModLoaderType.OPTIFINE);
        }

        return installed;
    }

    public static ModLoaderType getModLoaderType(String mcVersion) {
        if (FabricChecker.isFabricInstall(mcVersion)) {
            return ModLoaderType.FABRIC;
        }

        return null;
    }

    public static List<String> loadRemoteVersions(String mcVersion, ModLoaderType modLoaderType) {
        switch (modLoaderType) {
            case FABRIC -> {
                return FabricVersionFetcher.getCompatibleFabricLoaders(mcVersion);
            }
        }
        return null;
    }

    public static void removeModLoader(String mcVersion, ModLoaderType modLoaderType) {
        if (ModLoaderType.FABRIC.equals(modLoaderType)) {
            FabricRemover.remove(mcVersion);
        }
    }

    public static void installModLoader(String mcPath, String mcVersion, String loaderVersion ,ModLoaderType modLoaderType) {
        switch (modLoaderType) {
            case FABRIC -> FabricInstall.installFabric(mcPath, mcVersion, loaderVersion);
        }

    }

    public static String getModLoaderVersion(String mcVersion) {
        if (FabricChecker.getModLoaderVersion(mcVersion) != null) {
            return FabricChecker.getModLoaderVersion(mcVersion);
        }

        return null;
    }

}
