package org.bkl.game;


import org.to2mbn.jmccc.internal.org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author LongWei
 * @date 2025/9/15 11:59
 */
public class VersionParser {

    /**
     * 根据游戏版本获取游戏版本json路径
     * @param version
     * @return
     * @throws Exception
     */
    public static JSONObject versionParser(String version) throws Exception{
        String versionFilePath = System.getenv("APPDATA") + "\\.minecraft\\versions\\"+ version + "\\" + version + ".json";
        String jsonContent = new String(Files.readAllBytes(Paths.get(versionFilePath)));
        JSONObject versionJson = new JSONObject(jsonContent);
        return versionJson;
    }


}
