package com.mrqueequeg.hypixel_enhancer;

import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
import com.mrqueequeg.hypixel_enhancer.debug.Logger;
import com.mrqueequeg.hypixel_enhancer.gui.ScreenBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class HypixelEnhancer implements ClientModInitializer {
	private static KeyBinding keyBindingOpenSettings;
	private static KeyBinding keyToggleEnabled;

	public static final String MOD_ID = "hypixel_enhancer";
	public static final String MOD_NAME = "Murder Mystery Mod";
	public static final String CHAT_TAG = "[Murder Mystery Mod] ";

	@Override
	public void onInitializeClient() {
		Logger.init();
		ConfigManager.init();

		// adding keybinding to settings
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
			//client.inGameHud.getChatHud().addMessage(new TranslatableText(""));
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

	@Deprecated
	public static void printChatMsg(Text msg) {
		if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().player != null) {
			MinecraftClient.getInstance().player.sendMessage(msg, false);
		}
	}
}
