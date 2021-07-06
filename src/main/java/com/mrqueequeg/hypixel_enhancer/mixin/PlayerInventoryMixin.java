package com.mrqueequeg.hypixel_enhancer.mixin;

import com.mrqueequeg.hypixel_enhancer.HypixelEnhancer;
import com.mrqueequeg.hypixel_enhancer.config.Config;
import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Inject(at = @At("HEAD"), method = "setStack")
    private void onSetStack(int slot, ItemStack stack, CallbackInfo info) {
        if (ConfigManager.getConfig().enabled) {
            if (Config.MurderMystery.isActive() && !Config.MurderMystery.clientIsMurder) {
                // If player gets murder item set isMurder to true
                if (stack.hasCustomName()) {
                    //if (stack.getName().getString().equals("Knife")) {
                    if (Config.MurderMystery.isMurderItem(stack.getItem())) {
                        HypixelEnhancer.printChatMsg(new TranslatableText("message.murder_mystery.starting_murder_mode").formatted(Formatting.RED));
                        Config.MurderMystery.clientIsMurder = true;
                    }
                }
            }
        }
    }
}
