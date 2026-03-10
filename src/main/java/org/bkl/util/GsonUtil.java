package org.bkl.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.bkl.log.Logger;
import org.bkl.log.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author LongWei
 * @date 2025/10/10 23:58
 */
public class GsonUtil {
    private final static Logger log = LoggerFactory.getLogger(GsonUtil.class.getName());

    /**
     * 将jsonObject回写到指定文件
     * @param jsonObject 要回写的jsonObject
     * @param fileName 要回写的文件名称
     */
    public static void jsonObjectWriter (JsonObject jsonObject, String fileName) {
        com.google.gson.Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            log.info("Failed to write JSON to file: " + e.getMessage());
        }
    }
}
