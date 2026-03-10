package org.bkl.game;


import javafx.application.Platform;
import org.bkl.modloader.ModLoaderManager;
import org.bkl.modloader.ModLoaderType;
import org.bkl.ui.ProgressDialog;
import org.to2mbn.jmccc.auth.OfflineAuthenticator;
import org.to2mbn.jmccc.launch.Launcher;
import org.to2mbn.jmccc.launch.LauncherBuilder;
import org.to2mbn.jmccc.option.LaunchOption;
import org.to2mbn.jmccc.option.MinecraftDirectory;

import java.util.List;

/**
 * @author LongWei
 * @date 2025/9/15 12:10
 */


public class GameLauncher {

    // 游戏全局变量
    private static String version = null;
    private static String minecraftDir = null;
    private static String auth = null;
    private static Boolean isStart = false;
    private static ModLoaderType modLoaderType = null;
    private static String modLoaderVersion = null;

    public GameLauncher(String version, String auth, String minecraftDir) {
        GameLauncher.version = version;
        GameLauncher.minecraftDir = minecraftDir;
        GameLauncher.auth = auth;

        // 首次进入加载游戏版本加载器和版本
        if (MCVersionChecker.getVersionNameList().size() > 0) {
            version = MCVersionChecker.getVersionNameList().get(0);
        }
        modLoaderType = ModLoaderManager.getModLoaderType(version);
        modLoaderVersion = ModLoaderManager.getModLoaderVersion(version);

    }

    public static void start(ProgressDialog progressDialog) {
        if (GameLauncher.isStart ) {
            return;
        }
        GameLauncher.isStart = true;

        GameLauncher.gameLauncher(new LauncherCallback() {
            @Override
            public void onProgress(String message, double progress) {
                ProgressDialog.updateProgress(progress / 100);
            }

            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onLog(String logMessage) {

            }
        });
    }

    private static void gameLauncher(LauncherCallback callback) {
        new Thread(() -> {

            Process gameProcess = null;
            try {
                callback.onProgress("初始化起动器", 10);
                Launcher launcher = LauncherBuilder.buildDefault();

                callback.onProgress("设置Minecraft目录", 30);
                MinecraftDirectory minecraftDirectory = new MinecraftDirectory(minecraftDir);

                callback.onProgress("创建启动项", 50);
                LaunchOption option = new LaunchOption(version, new OfflineAuthenticator(auth), minecraftDirectory);

                callback.onProgress("游戏启动中", 70);
                gameProcess = launcher.launch(option);

                if (gameProcess != null) {
                    callback.onProgress("游戏已启动", 100);
                    callback.onSuccess();
                    gameProcess.waitFor();
                    GameLauncher.isStart = false;
                } else {
                    GameLauncher.isStart = false;
                    callback.onError("游戏启动失败");
                }

                Platform.exit();
                System.exit(0);

                System.out.println("Minecraft 游戏已关闭，启动器准备退出");
            } catch ( Exception e) {
                GameLauncher.isStart = false;
                callback.onError("游戏启动失败");
                e.printStackTrace();
            }
        }).start();

    }

    public static void setVersion(String version) {
        GameLauncher.version = version;
    }

    // 获取全局游戏版本
    public static String getVersion() {
        return GameLauncher.version;
    }

    public static ModLoaderType getModLoaderType() {
        return GameLauncher.modLoaderType;
    }

    public static void setModLoader(ModLoaderType modLoaderType) {
        GameLauncher.modLoaderType = modLoaderType;
    }

    public  static String getModLoaderVersion() {
        return GameLauncher.modLoaderVersion;
    }

    public static void setModLoaderVersion(String modLoaderVersion) {
        GameLauncher.modLoaderVersion = modLoaderVersion;
    }
}
