package com.mrqueequeg.hypixel_enhancer.config;

import com.google.gson.annotations.Expose;
import com.mrqueequeg.hypixel_enhancer.access.PlayerEntityMixinAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Config {
    // Values that will be saved in json are marked with @Expose
    // These will need to be non static as two Config instances will be made in ScreenBuilder: config and defaults
    // The non-exposed values should be static

    @Expose public boolean enabled = true;
    @Expose public MurderMystery murdermystery = new MurderMystery();

    public static boolean onHypixelServer = false;
    public static HypixelLobbies activeMiniGame = HypixelLobbies.None;

    public static final class MurderMystery {
        @Expose public boolean murderMode = true;
        @Expose public boolean innocentMode = true;
        @Expose public boolean showNameTags = false;

        public boolean isEnabled() {
            return activeMiniGame == HypixelLobbies.MurderMystery;
        }

        public boolean validate() {
            return true;
        }

        public static boolean isMurder = false;
        public static ArrayList<PlayerEntity> markedMurders = new ArrayList<>();

        public static final ArrayList<Item> MURDER_ITEMS = new ArrayList<>(Arrays.asList(
                Items.STICK, Items.DEAD_BUSH, Items.CARROT, Items.GOLDEN_CARROT, Items.NAME_TAG, Items.GOLDEN_PICKAXE,
                Items.SPONGE, Items.BONE, Items.PUMPKIN_PIE, Items.APPLE, Items.BLAZE_ROD, Items.FEATHER,
                Items.COOKIE, Items.PRISMARINE_SHARD, Items.ROSE_BUSH, Items.OAK_BOAT, Items.GLISTERING_MELON_SLICE,
                Items.SALMON, Items.SHEARS, Items.REDSTONE_TORCH, Items.DIAMOND_HOE, Items.WOODEN_AXE, Items.DIAMOND_AXE,
                Items.CARROT_ON_A_STICK
        ));
        public static void resetMarkedPlayers() {
            for (PlayerEntity entity : markedMurders) {
                ((PlayerEntityMixinAccess)entity).resetMurderState();
            }
            markedMurders.clear();
        }
        public static void reset() {
            isMurder = false;
            resetMarkedPlayers();
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
        MurderMysteryLobby
    }

    /**
     * Reset configurations
     * @param lobby Lobby from which the config is to be reseted
     */
    public static void reset(HypixelLobbies lobby) {
        if (lobby == HypixelLobbies.MurderMystery) {
            MurderMystery.reset();
        }
        activeMiniGame = HypixelLobbies.None;
    }

    /**
     * @return true if nothing was changed
     */
    public boolean validate() {
        boolean valid = true;

        return valid && murdermystery.validate();
    }
}
