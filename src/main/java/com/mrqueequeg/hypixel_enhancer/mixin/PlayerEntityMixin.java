package com.mrqueequeg.hypixel_enhancer.mixin;

import com.mrqueequeg.hypixel_enhancer.HypixelEnhancer;
import com.mrqueequeg.hypixel_enhancer.config.Config;
import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(at = @At("RETURN"), method = "getEquippedStack")
    private void onEquip(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> info) {
        if (ConfigManager.getConfig().enabled) {
            Config config = ConfigManager.getConfig();
            if (config.murdermystery.isEnabled() && !Config.MurderMystery.isMurder) {
                Object t = (Object) this;
                if (!(t instanceof ClientPlayerEntity) && !Config.MurderMystery.markedMurders.contains(((PlayerEntity)t).getGameProfile().getId())) {
                    if (Config.MurderMystery.isMurderItem(info.getReturnValue().getItem())) {
                        HypixelEnhancer.printChatMsg(new TranslatableText("message.murder_mystery.murder_marked", Formatting.RED+((PlayerEntity)(Object)this).getGameProfile().getName()));
                        Config.MurderMystery.markedMurders.add(((PlayerEntity)t).getGameProfile().getId());
                    }
                }
            }
        }
    }
}
