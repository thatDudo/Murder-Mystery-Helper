package com.thatdudo.mm_helper.gui;

import com.thatdudo.mm_helper.MMHelper;
import com.thatdudo.mm_helper.config.Config;
import com.thatdudo.mm_helper.config.ConfigManager;
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
                .setSaveConsumer(MMHelper::setModEnabled)
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
                .setSaveConsumer(MMHelper::setDetectiveHighlightOptions)
                .build();

        // highlight murders
        AbstractConfigListEntry<Boolean> toggleMurderMysteryHighlightMurders = entryBuilder.startBooleanToggle(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.murder.title"), config.murdermystery.highlightMurders)
                .setDefaultValue(defaults.murdermystery.highlightMurders)
                .setTooltip(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.murder.tooltip"))
                .setSaveConsumer(MMHelper::setHighlightMurders)
                .build();

        // highlight gold
        AbstractConfigListEntry<Boolean> toggleMurderMysteryHighlightGold = entryBuilder.startBooleanToggle(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.gold.title"), config.murdermystery.highlightGold)
                .setDefaultValue(defaults.murdermystery.highlightGold)
                .setTooltip(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.gold.tooltip"))
                .setSaveConsumer(config.murdermystery::setHighlightGold)
                .build();

        // highlight bows
        AbstractConfigListEntry<Boolean> toggleMurderMysteryHighlightBows = entryBuilder.startBooleanToggle(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.bow.title"), config.murdermystery.highlightBows)
                .setDefaultValue(defaults.murdermystery.highlightBows)
                .setTooltip(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.bow.tooltip"))
                .setSaveConsumer(config.murdermystery::setHighlightBows)
                .build();

        // show name tags
        AbstractConfigListEntry<Boolean> toggleMurderMysteryShowNameTags = entryBuilder.startBooleanToggle(new TranslatableText("config.generic.hypixel.murder_mystery.misc.show_name_tags.title"), config.murdermystery.showNameTags)
                .setDefaultValue(defaults.murdermystery.showNameTags)
                .setTooltip(new TranslatableText("config.generic.hypixel.murder_mystery.misc.show_name_tags.tooltip"))
                .setSaveConsumer(config.murdermystery::setShowNameTags)
                .build();

        // highlight spectators
        AbstractConfigListEntry<Boolean> toggleMurderMysteryHighlightSpectators = entryBuilder.startBooleanToggle(new TranslatableText("config.generic.hypixel.murder_mystery.misc.highlight_spectators.title"), config.murdermystery.highlightSpectators)
                .setDefaultValue(defaults.murdermystery.highlightSpectators)
                .setTooltip(new TranslatableText("config.generic.hypixel.murder_mystery.misc.highlight_spectators.tooltip"))
                .setSaveConsumer(config.murdermystery::setHighlightSpectators)
                .build();

        // check for updates
        AbstractConfigListEntry<Boolean> toggleCheckForUpdates = entryBuilder.startBooleanToggle(new TranslatableText("config.generic.hypixel.murder_mystery.misc.check_for_updates.title"), config.checkForUpdates)
                .setDefaultValue(defaults.checkForUpdates)
//                .setTooltip(new TranslatableText("config.generic.hypixel.murder_mystery.misc.check_for_updates.tooltip"))
                .setSaveConsumer(MMHelper::setCheckForUpdates)
                .build();


        SubCategoryBuilder subCatHighlight = entryBuilder.startSubCategory(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.title"));
        subCatHighlight.add(enumMurderMysteryInnocentHighlightOptions);
        subCatHighlight.add(enumMurderMysteryDetectiveHighlightOptions);
        subCatHighlight.add(toggleMurderMysteryHighlightMurders);
        subCatHighlight.add(toggleMurderMysteryHighlightGold);
        subCatHighlight.add(toggleMurderMysteryHighlightBows);
        subCatHighlight.setExpanded(true);

        SubCategoryBuilder subCatMisc = entryBuilder.startSubCategory(new TranslatableText("config.generic.hypixel.murder_mystery.misc.title"));
        subCatMisc.add(toggleMurderMysteryShowNameTags);
        subCatMisc.add(toggleMurderMysteryHighlightSpectators);
        subCatMisc.add(toggleCheckForUpdates);
        subCatMisc.setExpanded(true);

        catGeneric.addEntry(toggleEnabled);
        catGeneric.addEntry(subCatHighlight.build());
        catGeneric.addEntry(subCatMisc.build());

        return builder.build();
    }

    public static void openConfigScreen() {
        openConfigScreen(MinecraftClient.getInstance());
    }

    public static void openConfigScreen(MinecraftClient client) {
        client.setScreenAndRender(buildConfigScreen(client.currentScreen));
    }
}
