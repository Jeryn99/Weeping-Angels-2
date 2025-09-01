package mc.jeryn.dev.angels.data.model.donators;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mc.jeryn.dev.angels.CommonClass;
import mc.jeryn.dev.angels.WAConstants;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class VIPCacheManager {

    private static final String ENDPOINT = "https://api.jeryn.dev/mc/vips";
    private static final String CACHE_FILE = "vip_cache.json";
    private static final long CACHE_TTL_MS = 60 * 60 * 1000;

    private static final Gson gson = new Gson();
    private static List<Donator> cachedDonators = Collections.emptyList();

    public static List<Donator> getVIPs() {
        if (isCacheValid()) {
            WAConstants.LOG.info("Using cached VIP data.");
            return readFromCache();
        }

        try {
            String json = fetchFromEndpoint();
            saveToCache(json);
            cachedDonators = parseJson(json);
            return cachedDonators;
        } catch (IOException e) {
            WAConstants.LOG.warn("Failed to fetch from endpoint, using cache if available.");
            return readFromCache();
        }
    }

    private static boolean isCacheValid() {
        File file = new File(CACHE_FILE);
        return file.exists() && (Instant.now().toEpochMilli() - file.lastModified() < CACHE_TTL_MS);
    }

    private static List<Donator> readFromCache() {
        try {
            String json = Files.readString(new File(CACHE_FILE).toPath(), StandardCharsets.UTF_8);
            cachedDonators = parseJson(json);
            return cachedDonators;
        } catch (IOException e) {
            WAConstants.LOG.error("Could not read from cache: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private static void saveToCache(String json) {
        try (FileWriter writer = new FileWriter(CACHE_FILE)) {
            writer.write(json);
        } catch (IOException e) {
            WAConstants.LOG.error("Failed to write to cache: {}", e.getMessage());
        }
    }

    private static String fetchFromEndpoint() throws IOException {
        URL url = new URL(ENDPOINT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(5000);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            return response.toString();
        }
    }

    private static List<Donator> parseJson(String json) {
        var jsonObject = gson.fromJson(json, com.google.gson.JsonObject.class);
        var dataArray = jsonObject.getAsJsonArray("data");
        Type listType = new TypeToken<List<Donator>>() {}.getType();
        return gson.fromJson(dataArray, listType);
    }


}
