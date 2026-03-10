package org.bkl.modloader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bkl.game.MinecraftPath;

import java.io.File;
import java.io.FileReader;

public class ForgeChecker {
    public ForgeChecker() {
    }

    /**
     * 检查是否安装了 Forge（支持任意版本）
     * @return 是否安装 Forge
     */
    public static boolean isForgeInstalled(String mcVersion) {
        File versionDir = new File(MinecraftPath.getMinecraftPath() + "/versions/");

        if (!versionDir.exists()) {
            return false;
        }

        /**
         * 遍历版本文件夹
         * 查找包含选择版本的版本json文件 version.json
         */
        File[] versionFolder = versionDir.listFiles();

        if (versionFolder != null) {
            for (File versionFile : versionFolder) {
                /**
                 * 如果包含本号的文件存在
                 * 则使用该版本号创建对应的版本json文件 再判断该文件是否存在
                 */
                if (versionFile.getName().contains(mcVersion)) {
                    File f = new File(versionFile.getPath(),mcVersion + ".json");
                    if (f.exists() && checkerJsonForForge(f)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 检查版本 JSON 文件是否属于 Forge
     * @param versionJson 版本 JSON 文件
     * @return 是否为 Forge 的 JSON 配置
     */
    private static boolean checkerJsonForForge(File versionJson) {
        try(FileReader reader = new FileReader(versionJson)){
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            if (jsonObject.has("libraries")) {
                JsonArray jsonArray = jsonObject.getAsJsonArray("libraries");

                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();

                    if (jsonObject1.has("name")) {
                        String name = jsonObject1.get("name").getAsString();

                        if (name.startsWith("net.minecraftforge:forge")) {
                            return true;
                        }
                    }
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
