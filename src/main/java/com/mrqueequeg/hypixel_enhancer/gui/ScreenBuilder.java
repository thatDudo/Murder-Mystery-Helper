package com.mrqueequeg.hypixel_enhancer.gui;

import com.mrqueequeg.hypixel_enhancer.HypixelEnhancer;
import com.mrqueequeg.hypixel_enhancer.config.Config;
import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public class ScreenBuilder {

    public static Screen buildConfigScreen(Screen parent) {
        Config config = ConfigManager.getConfig();
        Config defaults = ConfigManager.getDefaults();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslatableText("config.title"))
                .transparentBackground()
                //.setDoesConfirmSave(true)
                .setSavingRunnable(() -> {
                    ConfigManager.writeConfig(true);
                });

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory catGeneric = builder.getOrCreateCategory(new TranslatableText("config.generic.title"));

        // enable
        AbstractConfigListEntry<Boolean> toggleEnabled = entryBuilder.startBooleanToggle(new TranslatableText("config.generic.enabled.title"), config.enabled)
                .setDefaultValue(defaults.enabled)
                .setTooltip(new TranslatableText("config.generic.enabled.tooltip"))
                .setSaveConsumer(HypixelEnhancer::setModEnabled)
                .build();

        // innocent mode
        AbstractConfigListEntry<Boolean> toggleMurderMysteryInnocentMode = entryBuilder.startBooleanToggle(new TranslatableText("config.generic.hypixel.murder_mystery.innocent_mode.title"), config.murdermystery.innocentHelp)
                .setDefaultValue(defaults.murdermystery.innocentHelp)
                .setTooltip(new TranslatableText("config.generic.hypixel.murder_mystery.innocent_mode.tooltip"))
                .setSaveConsumer(n -> config.murdermystery.innocentHelp = n)
                .build();

        // murder mode
        AbstractConfigListEntry<Boolean> toggleMurderMysteryMurderMode = entryBuilder.startBooleanToggle(new TranslatableText("config.generic.hypixel.murder_mystery.murder_mode.title"), config.murdermystery.murderHelp)
                .setDefaultValue(defaults.murdermystery.murderHelp)
                .setTooltip(new TranslatableText("config.generic.hypixel.murder_mystery.murder_mode.tooltip"))
                .setSaveConsumer(n -> config.murdermystery.murderHelp = n)
                .build();

        // show name tags
        AbstractConfigListEntry<Boolean> toggleMurderMysteryShowNameTag = entryBuilder.startBooleanToggle(new TranslatableText("config.generic.hypixel.murder_mystery.show_name_tags.title"), config.murdermystery.showNameTags)
                .setDefaultValue(defaults.murdermystery.showNameTags)
                .setTooltip(new TranslatableText("config.generic.hypixel.murder_mystery.show_name_tags.tooltip"))
                .setSaveConsumer(n -> config.murdermystery.showNameTags = n)
                .build();



        catGeneric.addEntry(toggleEnabled);
        catGeneric.addEntry(toggleMurderMysteryInnocentMode);
        catGeneric.addEntry(toggleMurderMysteryMurderMode);
        catGeneric.addEntry(toggleMurderMysteryShowNameTag);

        return builder.build();
    }

    public static void openConfigScreen() {
        openConfigScreen(MinecraftClient.getInstance());
    }

    public static void openConfigScreen(MinecraftClient client) {
        client.openScreen(buildConfigScreen(client.currentScreen));
    }
}
