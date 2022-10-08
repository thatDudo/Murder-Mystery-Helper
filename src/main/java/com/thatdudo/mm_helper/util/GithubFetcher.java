package com.thatdudo.mm_helper.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class GithubFetcher {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private static final Executor executor = Executors.newSingleThreadExecutor();

    public static void checkForUpdate(Consumer<String> consumer) {
        Runnable runnable = () -> {
            String data = fetchRaw("https://api.github.com/repos/thatDudo/Murder-Mystery-Helper/releases");
            List<GithubRelease> releases = gson.fromJson(data, new TypeToken<List<GithubRelease>>(){}.getType());
            if (releases != null){
                for (GithubRelease release : releases) {
                    String[] versions = release.tag_name.split("\\+");
                    Version modVersion = new Version(versions[0]);
                    Version mcVersion = new Version(versions[1]);
                    if (mcVersion.equals(ModProperties.MC_VERSION) && ModProperties.MOD_VERSION.shouldUpdateTo(modVersion)) {
                        consumer.accept(modVersion.toString());
                        return;
                    }
                }
                consumer.accept(null);
            }
        };
        executor.execute(runnable);
    }

    public static void getMurderItems(Consumer<ArrayList<Integer>> consumer) {
        Runnable runnable = () -> {
            String data = fetchRaw("https://raw.githubusercontent.com/thatDudo/Murder-Mystery-Helper/1.18.1/murder_items.json");
            consumer.accept(gson.fromJson(data, MurderItems.class).items);
        };
        executor.execute(runnable);
    }

    public static String fetchRaw(String url) {
        try {
            URL _url = new URL(url);
            URLConnection connection = _url.openConnection();
            connection.setConnectTimeout(5000);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder html = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                html.append(line).append("\n");
            }
            return html.toString();
        } catch (IOException exception) {
            return null;
        }
    }

    public static class GithubRelease {
        @Expose public String name;
        @Expose public String tag_name;
    }

    private static class MurderItems {
        @Expose public ArrayList<Integer> items;
    }
}
