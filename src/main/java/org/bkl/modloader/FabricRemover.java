package org.bkl.modloader;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bkl.game.MinecraftPath;
import org.bkl.util.GsonUtil;

import java.io.File;
import java.io.FileReader;

public class FabricRemover {
    public FabricRemover () {
    }

    // 成就感满满满！！！！！！
    // net.minecraft.client.main.Main
    public static void remove(String mcVersion) {
        File versionFolder = new File(MinecraftPath.getMinecraftPath(), "/versions/");
        File[] files = versionFolder.listFiles();

        for (File f : files) {
            if (f.getName().contains(mcVersion)) {
                File f1 = new File(f.getPath(), "/" + mcVersion + ".json");

                if (!f1.exists()) {
                    break;
                }

                try(FileReader reader = new FileReader(f1)) {
                    JsonObject asJsonObject = JsonParser.parseReader(reader).getAsJsonObject();

                    if (asJsonObject.has("mainClass")) {
                        asJsonObject.addProperty("mainClass", "net.minecraft.client.main.Main");
                    }

                    if (asJsonObject.has("libraries")) {
                        JsonArray jsonArray = asJsonObject.get("libraries").getAsJsonArray();

                        for (int i = jsonArray.size() - 1; i >= 0 ; i--) {
                            JsonObject asJsonObjectLibraries = jsonArray.get(i).getAsJsonObject();

                            if (asJsonObjectLibraries.has("name")) {
                                String name = asJsonObjectLibraries.get("name").getAsString();
                                if (name.contains("fabric")) {
                                    jsonArray.remove(i);
                                    continue;
                                }
                            }

                            if (asJsonObjectLibraries.has("url")) {
                                String url = asJsonObjectLibraries.get("url").getAsString();
                                if (url.contains("fabric")) {
                                    jsonArray.remove(i);
                                    continue;
                                }
                            }

                            if (asJsonObjectLibraries.has("downloads")) {
                                JsonObject asJsonObject1 = asJsonObjectLibraries.get("downloads").getAsJsonObject();
                                if (asJsonObject1.has("artifact")) {
                                    JsonObject asJsonObject2 = asJsonObject1.get("artifact").getAsJsonObject();
                                    if (asJsonObject2.has("url")) {
                                        String url = asJsonObject2.get("url").getAsString();
                                        if (url.contains("fabric")) {
                                            jsonArray.remove(i);
                                            continue;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (asJsonObject.has("patches")) {
                        JsonArray patches = asJsonObject.get("patches").getAsJsonArray();

                        for (int i = patches.size() - 1; i >= 0; i--) {
                            JsonElement patch = patches.get(i);
                            if (patch.isJsonObject()) {
                                JsonObject patchObj = patch.getAsJsonObject();
                                if (patchObj.has("id")) {
                                    String id = patchObj.get("id").getAsString();
                                    if (id.contains("fabric")) {
                                        patches.remove(i);
                                    }
                                }
                            }
                        }
                    }

                    GsonUtil.jsonObjectWriter(asJsonObject, f1.getPath());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
