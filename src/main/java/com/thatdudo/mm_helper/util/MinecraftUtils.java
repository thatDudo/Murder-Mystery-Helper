package com.thatdudo.mm_helper.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class MinecraftUtils {
    public static void printChatMsg(Text msg) {
        if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().inGameHud != null) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(msg);
        }
    }

    public static boolean isPlayerInTabList(PlayerEntity player) {
        return isPlayerInTabList(player.getGameProfile());
    }

    public static boolean isPlayerInTabList(GameProfile profile) {
        // ids of players are sometimes different from this player list but using names works
        if (MinecraftClient.getInstance().player != null) {
            String name = profile.getName();
            for (PlayerListEntry entry : MinecraftClient.getInstance().player.networkHandler.getPlayerList()) {
                if (entry.getProfile().getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }
}
