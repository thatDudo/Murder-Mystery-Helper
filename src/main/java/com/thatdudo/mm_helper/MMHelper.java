package com.thatdudo.mm_helper;

import com.thatdudo.mm_helper.config.Config;
import com.thatdudo.mm_helper.config.ConfigManager;
import com.thatdudo.mm_helper.gui.ScreenBuilder;
import com.thatdudo.mm_helper.util.GithubFetcher;
import com.thatdudo.mm_helper.util.MinecraftUtils;
import com.thatdudo.mm_helper.util.ModProperties;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.UUID;

public class MMHelper implements ClientModInitializer {
	private static KeyBinding keyBindingOpenSettings;
	private static KeyBinding keyToggleEnabled;

	public static boolean onHypixelServer = false;
	public enum HypixelLobbies {
		None,
		MurderMystery,
		MurderMysteryLobby
	}
	public static HypixelLobbies currentLobby = HypixelLobbies.None;
	public static boolean roundHasEnded = false;

	public static boolean isEnabled() {
		return ConfigManager.getConfig().enabled;
	}

	public static boolean isActive() {
		return isEnabled() && currentLobby == HypixelLobbies.MurderMystery;
	}

	public static boolean clientIsMurder = false;
	public static boolean clientIsDead = false;
	public static ArrayList<UUID> markedMurders = new ArrayList<>();
	public static ArrayList<UUID> markedDetectives = new ArrayList<>();

	private static String newAvailableVersion;

	@Override
	public void onInitializeClient() {
		ConfigManager.init();
		if (ConfigManager.getConfig().checkForUpdates) {
			checkForUpdates();
		}
		GithubFetcher.getMurderItems(items -> {
			ConfigManager.getConfig().murdermystery.murderItems = items;
			ConfigManager.writeConfig();
		});

		// adding keybindings to minecraft settings
		keyBindingOpenSettings = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key."+ ModProperties.MOD_ID+".settings", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_UNKNOWN, // The keycode of the key
				"key.category."+ModProperties.MOD_ID // The translation key of the keybinding's category.
		));
		keyToggleEnabled = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key."+ModProperties.MOD_ID+".enable", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_UNKNOWN, // The keycode of the key
				"key.category."+ModProperties.MOD_ID // The translation key of the keybinding's category.
		));
		ClientTickEvents.END_CLIENT_TICK.register(this::tick);
	}

	public void tick(MinecraftClient client) {
		if (keyBindingOpenSettings.wasPressed()) {
			ScreenBuilder.openConfigScreen(client);
		}
		else if (keyToggleEnabled.wasPressed()) {
			if (client.player != null) {
				if (isEnabled()) {
					setModEnabled(false);
					client.player.sendMessage(new TranslatableText("message.disabled", ModProperties.MOD_NAME).formatted(Formatting.RED), true);
				}
				else {
					setModEnabled(true);
					client.player.sendMessage(new TranslatableText("message.enabled", ModProperties.MOD_NAME).formatted(Formatting.GREEN), true);
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

	public static void setHighlightMurders(boolean state) {
		Config config = ConfigManager.getConfig();
		if (config.murdermystery.highlightMurders != state) {
			config.murdermystery.highlightMurders = state;
			if (!state) {
				markedMurders.clear();
			}
		}
	}

	public static void setDetectiveHighlightOptions(Config.MurderMystery.DetectiveHighlightOptions state) {
		Config config = ConfigManager.getConfig();
		if (config.murdermystery.detectiveHighlightOptions != state) {
			config.murdermystery.detectiveHighlightOptions = state;
			if (!config.murdermystery.shouldHighlightDetectives(clientIsMurder)) {
				markedDetectives.clear();
			}
		}
	}

	public static void setCurrentLobby(HypixelLobbies lobby) {
		resetLobby(currentLobby);
		currentLobby = lobby;
		if (isActive()) {
			if (!ConfigManager.getConfig().hasShownUpdateNotification) {
				showUpdateNotification();
			}
		}
	}

	public static void resetLobby(HypixelLobbies lobby) {
		if (lobby == HypixelLobbies.MurderMystery) {
			roundHasEnded = false;
			clientIsMurder = false;
			clientIsDead = false;
			markedMurders.clear();
			markedDetectives.clear();
		}
	}

	public static void checkForUpdates() {
		if (newAvailableVersion == null) {
			GithubFetcher.checkForUpdate(version -> {
				newAvailableVersion = version;
				if (newAvailableVersion != null) {
					if (isActive()) {
						showUpdateNotification();
					}
				}
				else {
					ConfigManager.getConfig().hasShownUpdateNotification = false;
					ConfigManager.writeConfig();
				}
			});
		}
	}

	public static void showUpdateNotification() {
		if (newAvailableVersion != null) {
			MinecraftUtils.showToast(new TranslatableText("notification.update.title"),
					new TranslatableText("notification.update.description", newAvailableVersion));
			ConfigManager.getConfig().hasShownUpdateNotification = true;
			ConfigManager.writeConfig();
		}
	}

	public static void setCheckForUpdates(boolean doCheck) {
		Config config = ConfigManager.getConfig();
		if (config.checkForUpdates != doCheck) {
			config.checkForUpdates = doCheck;
			config.hasShownUpdateNotification = false;
			if (doCheck) {
				checkForUpdates();
			}
		}
	}
}
