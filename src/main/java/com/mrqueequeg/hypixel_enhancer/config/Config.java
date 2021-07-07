package com.mrqueequeg.hypixel_enhancer.config;

import com.google.gson.annotations.Expose;
import net.minecraft.item.*;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Config {
    // Values that will be saved in json are marked with @Expose
    // These will need to be non static as two Config instances will be made in ScreenBuilder: config and defaults
    // The non-exposed values should be static

    @Expose public boolean enabled = true;
    @Expose public MurderMystery murdermystery = new MurderMystery();

    public static boolean onHypixelServer = false;
    public static HypixelLobbies currentLobby = HypixelLobbies.None;
    public static boolean roundHasEnded = false;

    public static final class MurderMystery {
        @Expose public boolean highlightMurders = true;
        @Expose public InnocentHighlightOptions innocentHighlightOptions = InnocentHighlightOptions.AS_MURDER;
        @Expose public DetectiveHighlightOptions detectiveHighlightOptions = DetectiveHighlightOptions.NEVER;
        @Expose public boolean highlightGold = false;

        @Expose public boolean showNameTags = false;

        public void setHighlightMurders(boolean state) {
            if (highlightMurders != state) {
                if (!state) {
                    markedMurders.clear();
                }
                highlightMurders = state;
            }
        }

        public void setInnocentHighlightOptions(InnocentHighlightOptions state) {
            innocentHighlightOptions = state;
        }

        public void setShowNameTags(boolean state) {
            showNameTags = state;
        }

        public void setHighlightGold(boolean state) {
            highlightGold = state;
        }

        public void setDetectiveHighlightOptions(DetectiveHighlightOptions state) {
            if (detectiveHighlightOptions != state) {
                detectiveHighlightOptions = state;
                if (!shouldHighlightDetectives()) {
                    markedDetectives.clear();
                }
            }
        }

        public boolean shouldHighlightInnocents() {
            return innocentHighlightOptions == InnocentHighlightOptions.ALWAYS || (innocentHighlightOptions == InnocentHighlightOptions.AS_MURDER && clientIsMurder);
        }

        public boolean shouldHighlightDetectives() {
            return detectiveHighlightOptions == DetectiveHighlightOptions.ALWAYS || (detectiveHighlightOptions == DetectiveHighlightOptions.AS_MURDER && clientIsMurder);
        }

        public boolean shouldHighlightMurders() {
            return highlightMurders;
        }

        public boolean shouldHighlightGold() {
            return highlightGold;
        }

        public boolean shouldShowNameTags() {
            return showNameTags;
        }

        public boolean validate() {
            return true;
        }

        public enum InnocentHighlightOptions {
            NEVER(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.innocent.option.never")),
            AS_MURDER(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.innocent.option.as_murder")),
            ALWAYS(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.innocent.option.always"));

            private final TranslatableText text;

            InnocentHighlightOptions(TranslatableText text) {
                this.text = text;
            }

            public TranslatableText getText() {
                return this.text;
            }
        };

        public enum DetectiveHighlightOptions {
            NEVER(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.detective.option.never")),
            AS_MURDER(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.detective.option.as_murder")),
            ALWAYS(new TranslatableText("config.generic.hypixel.murder_mystery.highlight.detective.option.always"));

            private final TranslatableText text;

            DetectiveHighlightOptions(TranslatableText text) {
                this.text = text;
            }

            public TranslatableText getText() {
                return this.text;
            }
        };

        public static boolean isActive() {
            return currentLobby == HypixelLobbies.MurderMystery;
        }

        public static boolean clientIsMurder = false;
        public static ArrayList<UUID> markedMurders = new ArrayList<>();
        public static ArrayList<UUID> markedDetectives = new ArrayList<>();

        public static final int murderTeamColorValue = 0xFF1111;
        public static final int detectiveTeamColorValue = 0x15BFD6;
        public static final int bowTeamColorValue = 0x21E808;
        public static final int goldTeamColorValue = 0xFFF126;

        public static final ArrayList<Item> MURDER_ITEMS = new ArrayList<>(Arrays.asList(
                Items.STICK, Items.DEAD_BUSH, Items.CARROT, Items.GOLDEN_CARROT, Items.NAME_TAG, Items.GOLDEN_PICKAXE,
                Items.SPONGE, Items.BONE, Items.PUMPKIN_PIE, Items.APPLE, Items.BLAZE_ROD, Items.FEATHER,
                Items.COOKIE, Items.PRISMARINE_SHARD, Items.ROSE_BUSH, Items.OAK_BOAT, Items.GLISTERING_MELON_SLICE,
                Items.SALMON, Items.SHEARS, Items.REDSTONE_TORCH, Items.DIAMOND_HOE, Items.WOODEN_AXE, Items.DIAMOND_AXE,
                Items.CARROT_ON_A_STICK
        ));

        public static void reset() {
            clientIsMurder = false;
            markedMurders.clear();
            markedDetectives.clear();
        }
        public static boolean isMurderItem(Item item) {
            return item instanceof SwordItem
                    || (item instanceof ShovelItem && item != Items.WOODEN_SHOVEL)
                    || MURDER_ITEMS.contains(item);
        }
    }

    public enum HypixelLobbies {
        None,
        MurderMystery,
        MurderMysteryLobby // waiting room
    }

    public static void reset() {
        roundHasEnded = false;
        resetLobby(currentLobby);
        currentLobby = HypixelLobbies.None;
    }

    /**
     * Reset configurations
     * @param lobby Lobby from which the config is to be reseted
     */
    public static void resetLobby(HypixelLobbies lobby) {
        if (lobby == HypixelLobbies.MurderMystery) {
            MurderMystery.reset();
        }
    }

    /**
     * @return true if nothing was changed
     */
    public boolean validate() {
        boolean valid = true;

        // since only boolean toggles nothing can be invalid

        return valid && murdermystery.validate();
    }
}
