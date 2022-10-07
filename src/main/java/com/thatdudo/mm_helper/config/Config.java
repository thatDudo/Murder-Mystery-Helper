package com.thatdudo.mm_helper.config;

import com.google.gson.annotations.Expose;
import net.minecraft.item.*;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Config {
    @Expose public boolean enabled = true;
    @Expose public MurderMystery murdermystery = new MurderMystery();

    public static final class MurderMystery {
        @Expose public boolean highlightMurders = true;
        @Expose public InnocentHighlightOptions innocentHighlightOptions = InnocentHighlightOptions.AS_MURDER;
        @Expose public DetectiveHighlightOptions detectiveHighlightOptions = DetectiveHighlightOptions.NEVER;
        @Expose public boolean highlightGold = false;
        @Expose public boolean highlightBows = false;

        @Expose public boolean showNameTags = false;
        @Expose public boolean highlightSpectators = false;

        public void setInnocentHighlightOptions(InnocentHighlightOptions state) {
            innocentHighlightOptions = state;
        }

        public void setShowNameTags(boolean state) {
            showNameTags = state;
        }

        public void setHighlightGold(boolean state) {
            highlightGold = state;
        }

        public void setHighlightBows(boolean state) {
            highlightBows = state;
        }

        public void setHighlightSpectators(boolean state) {
            highlightSpectators = state;
        }

        public boolean shouldHighlightInnocents(boolean clientIsMurder) {
            return innocentHighlightOptions == InnocentHighlightOptions.ALWAYS || (innocentHighlightOptions == InnocentHighlightOptions.AS_MURDER && clientIsMurder);
        }

        public boolean shouldHighlightDetectives(boolean clientIsMurder) {
            return detectiveHighlightOptions == DetectiveHighlightOptions.ALWAYS || (detectiveHighlightOptions == DetectiveHighlightOptions.AS_MURDER && clientIsMurder);
        }

        public boolean shouldHighlightMurders() {
            return highlightMurders;
        }

        public boolean shouldHighlightGold() {
            return highlightGold;
        }

        public boolean shouldHighlightBows() {
            return highlightBows;
        }

        public boolean shouldShowNameTags() {
            return showNameTags;
        }

        public boolean shouldHighlightSpectators() {
            return highlightSpectators;
        }

        public boolean validate() {
            boolean valid = true;

            if (innocentHighlightOptions == null) {
                innocentHighlightOptions = InnocentHighlightOptions.NEVER;
                valid = false;
            }
            if (detectiveHighlightOptions == null) {
                detectiveHighlightOptions = DetectiveHighlightOptions.NEVER;
                valid = false;
            }

            return valid;
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
        }

        public static final int murderTeamColorValue = 0xFF1111;
        public static final int detectiveTeamColorValue = 0x15BFD6;
        public static final int goldTeamColorValue = 0xFFF126;
        public static final int bowTeamColorValue = 0x21E808;

        public static final ArrayList<Item> MURDER_ITEMS = new ArrayList<>(Arrays.asList(
                Items.IRON_SWORD, Items.ENDER_CHEST, Items.COOKED_CHICKEN, Items.BONE, Items.BLAZE_ROD, Items.NETHER_BRICK, Items.CARROT_ON_A_STICK,
                Items.STONE_SWORD, Items.SPONGE, Items.DEAD_BUSH, Items.OAK_BOAT, Items.GLISTERING_MELON_SLICE, Items.GOLDEN_PICKAXE,
                Items.COOKED_BEEF, Items.BOOK, Items.APPLE, Items.PRISMARINE_SHARD, Items.QUARTZ, Items.DIAMOND_SWORD, Items.NAME_TAG,
                Items.DIAMOND_SHOVEL, Items.ROSE_BUSH, Items.PUMPKIN_PIE, Items.DIAMOND_HOE, Items.CARROT, Items.RED_DYE, Items.SALMON,
                Items.SHEARS, Items.IRON_SHOVEL, Items.GOLDEN_CARROT, Items.WOODEN_SWORD, Items.STICK, Items.STONE_SHOVEL, Items.COOKIE,
                Items.DIAMOND_AXE, Items.GOLDEN_SWORD, Items.WOODEN_AXE
        ));

        public static boolean isMurderItem(Item item) {
            return item instanceof SwordItem
                    || (item instanceof ShovelItem && item != Items.WOODEN_SHOVEL)
                    || (item instanceof MusicDiscItem)
                    || MURDER_ITEMS.contains(item);
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