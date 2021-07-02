package com.mrqueequeg.hypixel_enhancer.debug;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

// TODO: Remove class because not needed...
public class Logger {
    public static boolean enabled = true;
    public static boolean debug = true;

    public static MinecraftClient client;

    public static void init() {
        client = MinecraftClient.getInstance();
    }

    public static void sendChatMessage(Text msg) {
        sendMessage(msg, false);
    }

    public static void sendMessage(Text msg, boolean actionBar) {
        if (enabled && client.player != null) {
            client.player.sendMessage(msg, actionBar);
        }
    }

    public static void println(String msg) {
        if (enabled) {
            System.out.println(msg);
        }
    }

    public Logger() {

    }

    public void d(String msg) {
        d(Text.of(msg));
    }

    public void d(Text msg) {
        if (enabled && debug) {

        }
    }

}
