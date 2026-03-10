package org.bkl.modloader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bkl.game.MinecraftPath;

import java.io.File;
import java.io.FileReader;

public class OptiFineChecker {
    public OptiFineChecker() {
    }

    public static boolean isOptiFineInstalled(String mcVersion) {
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
                if (versionFile.exists()) {
                    File f = new File(versionFile.getPath(),mcVersion + ".json");
                    if (f.exists() && checkerJsonForOptiFine(f)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean checkerJsonForOptiFine(File versionJson) {
        try(FileReader reader = new FileReader(versionJson)){
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            if (jsonObject.has("libraries")) {
                JsonArray jsonArray = jsonObject.getAsJsonArray("libraries");

                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();

                    if (jsonObject1.has("name")) {
                        String name = jsonObject1.get("name").getAsString();
                        if (name.startsWith("optifine:")) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
