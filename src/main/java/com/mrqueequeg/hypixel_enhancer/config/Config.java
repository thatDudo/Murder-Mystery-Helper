package com.mrqueequeg.hypixel_enhancer.config;

import com.google.gson.annotations.Expose;
import net.minecraft.item.*;

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
        @Expose public boolean murderHelp = true;
        @Expose public boolean innocentHelp = true;
        @Expose public boolean showNameTags = false;
        @Expose public boolean highlightItems = false;
        @Expose public boolean highlightDetectives = false;

        public void setMurderHelp(boolean state) {
            if (murderHelp != state) {
                if (!state) {
                    markedMurders.clear();
                }
                murderHelp = state;
            }
        }

        public void setInnocentHelp(boolean state) {
            innocentHelp = state;
        }

        public void setShowNameTags(boolean state) {
            showNameTags = state;
        }

        public void setHighlightItems(boolean state) {
            highlightItems = state;
        }

        public void setHighlightDetectives(boolean state) {
            if (highlightDetectives != state) {
                if (!state) {
                    markedDetectives.clear();
                }
                highlightDetectives = state;
            }
        }

        public boolean validate() {
            return true;
        }

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
