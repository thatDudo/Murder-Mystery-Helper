package com.thatdudo.mm_helper.mixin;

import com.thatdudo.mm_helper.MMHelper;
import com.thatdudo.mm_helper.config.ConfigManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Inject(at = @At("HEAD"), method = "setStack")
    private void onSetStack(int slot, ItemStack stack, CallbackInfo info) {
        if (MMHelper.isMurderMysteryActive() && !MMHelper.clientIsMurder) {
            if (stack.hasCustomName()) {
                if (ConfigManager.getConfig().murdermystery.isMurderItem(stack.getItem())) {
                    MMHelper.clientIsMurder = true;
                }
            }
        }
    }
}
