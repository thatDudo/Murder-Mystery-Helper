package com.thatdudo.mm_helper.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.thatdudo.mm_helper.config.ConfigManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MurderItemsFetcher {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private static final Executor executor = Executors.newSingleThreadExecutor();

    public static void fetchAndUpdate() {
        Runnable runnable = () -> {
            MurderItems murderItems;
            try {
                URL url = new URL("https://raw.githubusercontent.com/thatDudo/Murder-Mystery-Helper/1.18.1/murder_items.json");
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(5000);
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder html = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    html.append(line).append("\n");
                }
                murderItems = gson.fromJson(html.toString(), MurderItems.class);
            } catch (IOException exception) {
                return;
            }

            ConfigManager.getConfig().murdermystery.murderItems = murderItems.items;
            ConfigManager.writeConfig(true);
        };
        executor.execute(runnable);
    }

    private static class MurderItems {
        @Expose public ArrayList<Integer> items;
    }
}
