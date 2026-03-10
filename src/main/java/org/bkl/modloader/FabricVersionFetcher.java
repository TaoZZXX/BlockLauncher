package org.bkl.modloader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


public class FabricVersionFetcher {
    private static final String FABRIC_LOADER_API = "https://meta.fabricmc.net/v2/versions/loader/";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();
    private static JsonObject versionJson;

    public static void getFabricVersionJson(String mcVersion, String loaderVersion) {
        String apiUrl = FABRIC_LOADER_API + mcVersion + "/" + loaderVersion;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                new RuntimeException("Failed to fetch Fabric loader details: HTTP " + response.statusCode());
            }
            versionJson = gson.fromJson(response.body(), JsonObject.class);
        } catch (Exception e) {
            FabricVersionFetcher.versionJson = null;
        }
    }

    public static List<String> getCompatibleFabricLoaders(String mcVersion) {
        ArrayList<String> fabricLoaders = new ArrayList<>();

        String apiUrl = FABRIC_LOADER_API + mcVersion;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                new RuntimeException("Failed to fetch Fabric versions: HTTP " + response.statusCode());
            }

            JsonArray jsonArray = gson.fromJson(response.body(), JsonArray.class);
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject loaderObj = jsonArray.get(i).getAsJsonObject();
                String loaderVersion = loaderObj.get("loader").getAsJsonObject().get("version").getAsString();
                fabricLoaders.add(loaderVersion);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fabricLoaders;
    }

    public static JsonObject getLibraries(String mcVersion, String loaderVersion) {
        if (FabricVersionFetcher.versionJson == null) {
            getFabricVersionJson(mcVersion, loaderVersion);
        }

        if (versionJson.has("launcherMeta")) {
                JsonObject asJsonObject = versionJson.get("launcherMeta").getAsJsonObject();

                if (asJsonObject.has("libraries")) {
                    JsonObject asJsonObject1 = asJsonObject.get("libraries").getAsJsonObject();

                    if (asJsonObject1.has("common")) {
                        return asJsonObject1;
                    }
                }
            }
        return null;
    }

    public static JsonObject getFabricLoaderMainClassName(String mcVersion, String loaderVersion) {
        if (FabricVersionFetcher.versionJson == null) {
            getFabricVersionJson(mcVersion, loaderVersion);
        }

        if (versionJson.has("launcherMeta")) {
            JsonObject asJsonObject = versionJson.get("launcherMeta").getAsJsonObject();

            if (asJsonObject.has("mainClass")) {
                return  asJsonObject.get("mainClass").getAsJsonObject();
            }
        }
        return null;
    }

    public static String getFabricLoaderName(String mcVersion, String loaderVersion) {
        if (FabricVersionFetcher.versionJson == null) {
            getFabricVersionJson(mcVersion, loaderVersion);
        }

        if (versionJson.has("loader")) {
            JsonObject asJsonObject = versionJson.get("loader").getAsJsonObject();

            if (asJsonObject.has("maven")) {
                return  asJsonObject.get("maven").getAsString();
            }
        }
        return null;
    }

    public static String getIntermediaryName(String mcVersion, String loaderVersion) {
        if (FabricVersionFetcher.versionJson == null) {
            getFabricVersionJson(mcVersion, loaderVersion);
        }

        if (versionJson.has("intermediary")) {
            JsonObject asJsonObject = versionJson.get("intermediary").getAsJsonObject();

            if (asJsonObject.has("maven")) {
                return asJsonObject.get("maven").getAsString();
            }
        }

        return null;
    }

}
