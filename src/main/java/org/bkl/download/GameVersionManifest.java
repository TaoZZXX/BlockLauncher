package org.bkl.download;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bkl.log.Logger;
import org.bkl.log.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


public class GameVersionManifest {
    private static final Logger log = LoggerFactory.getLogger(GameVersionManifest.class.getName());
    private static final String MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest_v2.json";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static JsonObject manifest = null;

    public static JsonObject getManifestFetcher() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MANIFEST_URL))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> resp;
        try {
             resp = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) {
                log.info("Failed to fetch game version manifest: HTTP " + resp.statusCode());
            }

            GameVersionManifest.manifest = JsonParser.parseString(resp.body()).getAsJsonObject();
            return GameVersionManifest.manifest;
        } catch (Exception e) {
            log.info("fail fetch game version manifest: " + e.getMessage());
        }
        return null;
    }

    public static List<String> getReleaseVersions() {
        if (manifest == null) {
            getManifestFetcher();
        }

        List<String> releaseVersions = new ArrayList<>();
        JsonArray versions = manifest.get("versions").getAsJsonArray();
        for (int i = 0; i < versions.size(); i++) {
            JsonObject version = versions.get(i).getAsJsonObject();
            String type = version.get("type").getAsString();
            if (type.contains("release")) {
                releaseVersions.add(version.get("id").getAsString());
            }
        }
        return releaseVersions;
    }

    public static List<String> getSnapShotVersions() {
        if (manifest == null) {
            getManifestFetcher();
        }

        List<String> snapShopVersions = new ArrayList<>();
        JsonArray versions = manifest.get("versions").getAsJsonArray();
        for (int i = 0; i < versions.size(); i++) {
            JsonObject version = versions.get(i).getAsJsonObject();
            String type = version.get("type").getAsString();
            if (type.contains("snapshot")) {
                snapShopVersions.add(version.get("id").getAsString());
            }
        }
        return snapShopVersions;
    }

    public static List<String> getOldVersions() {
        if (manifest == null) {
            getManifestFetcher();
        }

        List<String> oldVersions = new ArrayList<>();
        JsonArray versions = manifest.get("versions").getAsJsonArray();
        for (int i = 0; i < versions.size(); i++) {
            JsonObject version = versions.get(i).getAsJsonObject();
            String type = version.get("type").getAsString();
            if (type.contains("old")) {
                oldVersions.add(version.get("id").getAsString());
            }
        }
        return oldVersions;
    }

    public static JsonObject getVersionInfo(String versionId) {
        if (manifest == null) {
            getManifestFetcher();
        }

        JsonArray versions = manifest.get("versions").getAsJsonArray();
        for (int i = 0; i < versions.size(); i++) {
            JsonObject version = versions.get(i).getAsJsonObject();
            String id = version.get("id").getAsString();
            if (id.equals(versionId)) {
                return version;
            }
        }
        return null;
    }

}
