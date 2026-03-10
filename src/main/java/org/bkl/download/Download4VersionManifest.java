package org.bkl.download;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bkl.game.MinecraftPath;
import org.bkl.log.Logger;
import org.bkl.log.LoggerFactory;
import org.bkl.util.GsonUtil;
import org.bkl.util.HashUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class Download4VersionManifest {
    private final static Logger log = LoggerFactory.getLogger(Download4VersionManifest.class.getName());
    private static HttpClient client = HttpClient.newHttpClient();
    protected static JsonObject gameVersionJsonObject = null;
    private static String gameInstallPath = null;

    private static void init(String mcVersion) {
        JsonObject gameInfo = GameVersionManifest.getVersionInfo(mcVersion);
        if (gameInfo == null) {
            log.info("Game version " + mcVersion + " not found in manifest.");
            return;
        }

        String versionUrl = gameInfo.get("url").getAsString();
        if (versionUrl == null) {
            log.info("No URL found for version " + mcVersion);
            return;
        }
        Download4VersionManifest.gameVersionJsonObject = JsonParser.parseString(httpGet(versionUrl)).getAsJsonObject();
        Download4VersionManifest.gameInstallPath = MinecraftPath.getDefaultMinecraftPath();
    }

    public static void downloadVersionManifest(String mcVersion) {
        if (Download4VersionManifest.gameVersionJsonObject == null || Download4VersionManifest.gameInstallPath == null) {
            init(mcVersion);
        }

        File parentPath = new File(gameInstallPath);
        if (!parentPath.exists()) {
            parentPath.mkdirs();
        }
        File versionPath = new File(gameInstallPath + "/versions/");
        if (!versionPath.exists()) {
            versionPath.mkdirs();
        }
        File mcVersionPath = new File(gameInstallPath + "/versions/" + mcVersion + "/");
        if (!mcVersionPath.exists()) {
            mcVersionPath.mkdirs();
        }

        String versionJsonPath = Download4VersionManifest.gameInstallPath + "/versions/" + mcVersion + "/" + mcVersion + ".json";
        File versionJsonFile = new File(versionJsonPath);
        if (!versionJsonFile.exists()) {
            try {
                versionJsonFile.createNewFile();
            } catch (Exception e) {
                log.info("Failed to create version json file: " + e.getMessage());
            }
        }

        GsonUtil.jsonObjectWriter(Download4VersionManifest.gameVersionJsonObject, versionJsonPath);

    }

    public static void downloadClientJar(String mcVersion) {
        if (Download4VersionManifest.gameVersionJsonObject == null || gameInstallPath == null) {
            init(mcVersion);
        }
        JsonObject downloads = Download4VersionManifest.gameVersionJsonObject.getAsJsonObject("downloads");
        if (downloads == null) {
            log.info("No downloads section found in version JSON.");
            return;
        }
        JsonObject client = downloads.getAsJsonObject("client");
        if (client == null) {
            log.info("No client download info found in version JSON.");
            return;
        }
        String clientUrl = client.get("url").getAsString();
        if (clientUrl == null) {
            log.info("No URL found for client jar.");
            return;
        }

        String clientJarPath = Download4VersionManifest.gameInstallPath + "/versions/" + mcVersion + "/" + mcVersion + ".jar";
        File clientJarFile = new File(clientJarPath);
        if (!clientJarFile.exists()) {
            try {
                clientJarFile.createNewFile();
            } catch (Exception e) {
                log.info("Failed to create client jar file: " + e.getMessage());
            }
        }

        String sha1 = client.get("sha1").getAsString();
        if (sha1 == null) {
            log.info("No SHA-1 hash found for client jar.");
            return;
        }
        if (!HashUtil.verifyHash(Path.of(clientJarPath), sha1, "SHA-1")) {
            log.info("Client jar hash verification failed.");
        } else {
            log.info("Client jar already exists and hash is verified.");
            return;
        }

        // Download the client jar
        byte[] jarData = httpGetBytes(clientUrl);
        if (jarData != null) {
            try {
                java.nio.file.Files.write(clientJarFile.toPath(), jarData);
            } catch (Exception e) {
                log.info("Failed to write client jar file: " + e.getMessage());
            }
        }
        log.info("Client jar downloaded successfully.");

    }

    public static void downloadLibraries(String mcVersion) {
        if (Download4VersionManifest.gameVersionJsonObject == null ||  Download4VersionManifest.gameInstallPath == null) {
            init(mcVersion);
        }

        JsonArray librariesJsonArray = Download4VersionManifest.gameVersionJsonObject.getAsJsonArray("libraries");
        if (librariesJsonArray == null || librariesJsonArray.isEmpty()) {
            log.info("Game version JSON object or install path is not initialized.");
            return;
        }

        for (int i = 0; i < librariesJsonArray.size(); i++) {
            JsonObject libraryJsonObject = librariesJsonArray.get(i).getAsJsonObject();
            if (libraryJsonObject.has("downloads")) {
                JsonObject downloads = libraryJsonObject.getAsJsonObject("downloads");
                if (downloads.has("artifact")) {
                    JsonObject artifact = downloads.getAsJsonObject("artifact");
                    String url = artifact.get("url").getAsString();
                    String path = artifact.get("path").getAsString();
                    String sha1 = artifact.get("sha1").getAsString();

                    File libFile = new File(Download4VersionManifest.gameInstallPath + "/libraries/" + path);
                    if (!libFile.exists()) {
                        libFile.getParentFile().mkdirs();
                        try {
                            libFile.createNewFile();
                        } catch (Exception e) {
                            log.info("Failed to create library file: " + e.getMessage());
                        }
                    }

                    if (!HashUtil.verifyHash(libFile.toPath(), sha1, "SHA-1")) {
                        log.info("Downloading library: " + path);
                        byte[] libData = httpGetBytes(url);
                        if (libData != null) {
                            try {
                                java.nio.file.Files.write(libFile.toPath(), libData);
                            } catch (Exception e) {
                                log.info("Failed to write library file: " + e.getMessage());
                            }
                        }
                    } else {
                        log.info("Library already exists and hash is verified: " + path);
                    }
                }
            }

        }

    }

    public static void downloadAssets(String mcVersion) {
        if (Download4VersionManifest.gameVersionJsonObject == null || gameInstallPath == null) {
            init(mcVersion);
        }
        JsonObject assetIndex = Download4VersionManifest.gameVersionJsonObject.getAsJsonObject("assetIndex");
        if (assetIndex == null) {
            log.info("No assetIndex found in version JSON.");
            return;
        }
        String indexId = assetIndex.has("id") ? assetIndex.get("id").getAsString() : null;
        String indexUrl = assetIndex.has("url") ? assetIndex.get("url").getAsString() : null;
        String indexSha1 = assetIndex.has("sha1") ? assetIndex.get("sha1").getAsString() : null;
        if (indexId == null || indexUrl == null) {
            log.info("Asset index id or url missing.");
            return;
        }

        String indexesDir = gameInstallPath + "/assets/indexes/";
        new File(indexesDir).mkdirs();
        String indexPath = indexesDir + indexId + ".json";
        File indexFile = new File(indexPath);

        // 下载并保存索引（若缺失或校验失败）
        boolean needDownloadIndex = true;
        if (indexFile.exists() && indexSha1 != null) {
            if (HashUtil.verifyHash(indexFile.toPath(), indexSha1, "SHA-1")) {
                needDownloadIndex = false;
            } else {
                log.info("Asset index sha1 mismatch, re-downloading index: " + indexId);
            }
        }
        if (needDownloadIndex) {
            byte[] idxBytes = httpGetBytes(indexUrl);
            if (idxBytes == null) {
                log.info("Failed to download asset index: " + indexUrl);
                return;
            }
            try {
                java.nio.file.Files.write(indexFile.toPath(), idxBytes);
            } catch (Exception e) {
                log.info("Failed to write asset index: " + e.getMessage());
                return;
            }
        }

        // 解析索引并下载 objects
        String s = null;
        try {
            s = new String(Files.readAllBytes(indexFile.toPath()));
        } catch (IOException e) {
            log.info("Failed to read asset index file: " + e.getMessage());
            return;
        }
        JsonObject indexJson = JsonParser.parseString(s).getAsJsonObject();
        JsonObject objects = indexJson.getAsJsonObject("objects");
        if (objects == null) {
            log.info("No objects in asset index.");
            return;
        }

        String objectsRoot = gameInstallPath + "/assets/objects/";
        for (String key : objects.keySet()) {
            JsonObject obj = objects.getAsJsonObject(key);
            String hash = obj.get("hash").getAsString();
            String sub = hash.substring(0, 2);
            File out = new File(objectsRoot + sub + "/" + hash);
            if (!out.exists() || !HashUtil.verifyHash(out.toPath(), hash, "SHA-1")) {
                out.getParentFile().mkdirs();
                String downloadUrl = "https://resources.download.minecraft.net/" + sub + "/" + hash;
                log.info("Downloading asset: " + key + " -> " + downloadUrl);
                byte[] data = httpGetBytes(downloadUrl);
                if (data != null) {
                    try {
                        java.nio.file.Files.write(out.toPath(), data);
                    } catch (Exception e) {
                        log.info("Failed to write asset file: " + e.getMessage());
                    }
                } else {
                    log.info("Failed to download asset: " + downloadUrl);
                }
            }
        }
    }

    private static byte[] httpGetBytes(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .build();
        HttpResponse<byte []> resp = null;
        try {
            resp = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        } catch (Exception e) {
            log.info("fail fetch game version manifest: " + e.getMessage());
        }
        if (resp == null || resp.statusCode() != 200) {
            log.info("Failed to fetch game version manifest: HTTP " + resp.statusCode());
        }
        return resp.body();
    }

    private static String httpGet(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> resp = null;
        try {
             resp = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            log.info("fail fetch game version manifest: " + e.getMessage());
        }
        if (resp == null || resp.statusCode() != 200) {
            log.info("Failed to fetch game version manifest: HTTP " + resp.statusCode());
        }
        return resp.body();
    }
}
