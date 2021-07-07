package com.mrqueequeg.hypixel_enhancer.gui;

import com.mrqueequeg.hypixel_enhancer.HypixelEnhancer;
import com.mrqueequeg.hypixel_enhancer.config.Config;
import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
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

        // highlight innocents
        EnumListEntry<Config.MurderMystery.InnocentHighlightOptions> enumMurderMysteryInnocentHighlightOptions = entryBuilder.startEnumSelector(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.innocent.title"), Config.MurderMystery.InnocentHighlightOptions.class, config.murdermystery.innocentHighlightOptions)
                .setDefaultValue(defaults.murdermystery.innocentHighlightOptions)
//                .setTooltip(new TranslatableText(""))
                .setEnumNameProvider((e) -> ((Config.MurderMystery.InnocentHighlightOptions)e).getText())
                .setSaveConsumer(config.murdermystery::setInnocentHighlightOptions)
                .build();

        // highlight detectives
        EnumListEntry<Config.MurderMystery.DetectiveHighlightOptions> enumMurderMysteryDetectiveHighlightOptions = entryBuilder.startEnumSelector(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.detective.title"), Config.MurderMystery.DetectiveHighlightOptions.class, config.murdermystery.detectiveHighlightOptions)
                .setDefaultValue(defaults.murdermystery.detectiveHighlightOptions)
//                .setTooltip(new TranslatableText(""))
                .setEnumNameProvider((e) -> ((Config.MurderMystery.DetectiveHighlightOptions)e).getText())
                .setSaveConsumer(config.murdermystery::setDetectiveHighlightOptions)
                .build();

        // highlight murders
        AbstractConfigListEntry<Boolean> toggleMurderMysteryHighlightMurders = entryBuilder.startBooleanToggle(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.murder.title"), config.murdermystery.highlightMurders)
                .setDefaultValue(defaults.murdermystery.highlightMurders)
                .setTooltip(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.murder.tooltip"))
                .setSaveConsumer(config.murdermystery::setHighlightMurders)
                .build();

        // highlight items
        AbstractConfigListEntry<Boolean> toggleHighlightGold = entryBuilder.startBooleanToggle(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.gold.title"), config.murdermystery.highlightGold)
                .setDefaultValue(defaults.murdermystery.highlightGold)
                .setTooltip(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.gold.tooltip"))
                .setSaveConsumer(config.murdermystery::setHighlightGold)
                .build();

        // show name tags
        AbstractConfigListEntry<Boolean> toggleMurderMysteryShowNameTags = entryBuilder.startBooleanToggle(new TranslatableText("config.generic.hypixel.murder_mystery.misc.show_name_tags.title"), config.murdermystery.showNameTags)
                .setDefaultValue(defaults.murdermystery.showNameTags)
                .setTooltip(new TranslatableText("config.generic.hypixel.murder_mystery.misc.show_name_tags.tooltip"))
                .setSaveConsumer(config.murdermystery::setShowNameTags)
                .build();


        SubCategoryBuilder subCatHighlight = entryBuilder.startSubCategory(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.title"));
        subCatHighlight.add(enumMurderMysteryInnocentHighlightOptions);
        subCatHighlight.add(enumMurderMysteryDetectiveHighlightOptions);
        subCatHighlight.add(toggleMurderMysteryHighlightMurders);
        subCatHighlight.add(toggleHighlightGold);
        subCatHighlight.setExpanded(true);

        SubCategoryBuilder subCatMisc = entryBuilder.startSubCategory(new TranslatableText("config.generic.hypixel.murder_mystery.misc.title"));
        subCatMisc.add(toggleMurderMysteryShowNameTags);
        subCatMisc.setExpanded(false);

        catGeneric.addEntry(toggleEnabled);
        catGeneric.addEntry(subCatHighlight.build());
        catGeneric.addEntry(subCatMisc.build());

        return builder.build();
    }

    public static void openConfigScreen() {
        openConfigScreen(MinecraftClient.getInstance());
    }

    public static void openConfigScreen(MinecraftClient client) {
        client.openScreen(buildConfigScreen(client.currentScreen));
    }
}
