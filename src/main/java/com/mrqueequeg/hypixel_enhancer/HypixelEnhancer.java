package com.mrqueequeg.hypixel_enhancer;

import com.mojang.authlib.GameProfile;
import com.mrqueequeg.hypixel_enhancer.config.Config;
import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
import com.mrqueequeg.hypixel_enhancer.debug.Logger;
import com.mrqueequeg.hypixel_enhancer.gui.ScreenBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class HypixelEnhancer implements ClientModInitializer {
	private static KeyBinding keyBindingOpenSettings;
	private static KeyBinding keyToggleEnabled;

	public static final String MOD_ID = "hypixel_enhancer";
	public static final String MOD_NAME = "Murder Mystery Helper";
	public static final String CHAT_TAG = "[Murder Mystery Helper] ";

	@Override
	public void onInitializeClient() {
		Logger.init();
		ConfigManager.init();

		// adding keybindings to minecraft settings
		keyBindingOpenSettings = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key."+MOD_ID+".settings", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_UNKNOWN, // The keycode of the key
				"key.category."+MOD_ID // The translation key of the keybinding's category.
		));
		keyToggleEnabled = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key."+MOD_ID+".enable", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_UNKNOWN, // The keycode of the key
				"key.category."+MOD_ID // The translation key of the keybinding's category.
		));
		ClientTickEvents.END_CLIENT_TICK.register(this::tick);
	}

	public void tick(MinecraftClient client) {
		if (keyBindingOpenSettings.wasPressed()) {
			ScreenBuilder.openConfigScreen(client);
		}
		else if (keyToggleEnabled.wasPressed()) {
			if (client.player != null) {
				if (ConfigManager.getConfig().enabled) {
					ConfigManager.getConfig().enabled = false;
					client.player.sendMessage(new TranslatableText("message.disabled", MOD_NAME).formatted(Formatting.RED), true);
				}
				else {
					ConfigManager.getConfig().enabled = true;
					client.player.sendMessage(new TranslatableText("message.enabled", MOD_NAME).formatted(Formatting.GREEN), true);
				}
			}
		}
	}

	public static void setModEnabled(boolean state) {
		Config config = ConfigManager.getConfig();
		if (state != config.enabled) {
			config.enabled = state;
		}
	}

	public static void printChatMsg(Text msg) {
		if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().inGameHud != null) {
			MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(msg);
		}
	}

	public static boolean isPlayerInTabList(PlayerEntity player) {
		return isPlayerInTabList(player.getGameProfile());
	}

	public static boolean isPlayerInTabList(GameProfile profile) {
		// ids of players are sometimes different from this player list
		// DOESN'T WORK: return MinecraftClient.getInstance().player.networkHandler.getPlayerUuids().contains(player.getGameProfile().getId());

		// but using names works:
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
