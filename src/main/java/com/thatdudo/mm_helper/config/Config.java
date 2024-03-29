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
    @Expose public boolean checkForUpdates = true;
    @Expose public boolean hasShownUpdateNotification = false;

    public static final class MurderMystery {
        @Expose public boolean highlightMurders = true;
        @Expose public InnocentHighlightOptions innocentHighlightOptions = InnocentHighlightOptions.ALWAYS;
        @Expose public DetectiveHighlightOptions detectiveHighlightOptions = DetectiveHighlightOptions.ALWAYS;
        @Expose public boolean highlightGold = true;
        @Expose public boolean highlightBows = true;
        @Expose public boolean showNameTags = true;
        @Expose public boolean highlightSpectators = false;
        @Expose public ArrayList<Integer> murderItems = getDefaultMurderItemsIds();

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

        public boolean isMurderItem(Item item) {
            return item instanceof SwordItem
                    || (item instanceof ShovelItem && item != Items.WOODEN_SHOVEL)
                    || (item instanceof MusicDiscItem)
                    || murderItems.contains(Item.getRawId(item));
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
        }

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

        public static final ArrayList<Item> DEFAULT_MURDER_ITEMS = new ArrayList<>(Arrays.asList(
                Items.IRON_SWORD, Items.ENDER_CHEST, Items.BOOK, Items.GLISTERING_MELON_SLICE, Items.OAK_BOAT, Items.RED_DYE, Items.SALMON,
                Items.SHEARS, Items.DIAMOND_HOE, Items.DIAMOND_SWORD, Items.GOLDEN_SWORD, Items.GOLDEN_HOE, Items.COOKED_CHICKEN,
                Items.NETHER_BRICK, Items.COOKED_BEEF, Items.PRISMARINE_SHARD, Items.ROSE_BUSH, Items.DIAMOND_AXE, Items.COOKIE, Items.GOLDEN_CARROT,
                Items.CARROT, Items.BONE, Items.CARROT_ON_A_STICK, Items.SPONGE, Items.NAME_TAG, Items.APPLE, Items.GOLDEN_PICKAXE,
                Items.PUMPKIN_PIE, Items.QUARTZ, Items.DIAMOND_SHOVEL, Items.BLAZE_ROD, Items.STONE_SHOVEL, Items.DEAD_BUSH, Items.WOODEN_SWORD,
                Items.WOODEN_AXE, Items.STICK, Items.IRON_SHOVEL, Items.STONE_SWORD
        ));

        public ArrayList<Integer> getDefaultMurderItemsIds() {
            ArrayList<Integer> ids = new ArrayList<>();
            for (Item item : DEFAULT_MURDER_ITEMS) {
                ids.add(Item.getRawId(item));
            }
            return ids;
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
