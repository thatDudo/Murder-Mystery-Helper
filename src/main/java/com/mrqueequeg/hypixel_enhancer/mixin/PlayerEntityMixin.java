package com.mrqueequeg.hypixel_enhancer.mixin;

import com.mojang.authlib.GameProfile;
import com.mrqueequeg.hypixel_enhancer.HypixelEnhancer;
import com.mrqueequeg.hypixel_enhancer.access.PlayerEntityMixinAccess;
import com.mrqueequeg.hypixel_enhancer.config.Config;
import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityMixinAccess {

    private boolean _isMurder = false;

    @Override
    public boolean isMurder() {
        return _isMurder;
    }

    @Override
    public void resetMurderState() {
        _isMurder = false;
    }

    @Inject(at = @At("RETURN"), method = "getEquippedStack")
    private void onEquip(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> info) {
        if (ConfigManager.getConfig().enabled) {
            Config config = ConfigManager.getConfig();
            if (config.murdermystery.isEnabled() && !Config.MurderMystery.isMurder) {
                if (!(((Object)this) instanceof ClientPlayerEntity) && !_isMurder) {
                    if (Config.MurderMystery.isMurderItem(info.getReturnValue().getItem())) {
                        HypixelEnhancer.printChatMsg(new TranslatableText("message.murder_mystery.murder_marked", Formatting.RED+((PlayerEntity)(Object)this).getGameProfile().getName()));
                        _isMurder = true;
                        Config.MurderMystery.markedMurders.add((PlayerEntity)(Object)this);
                    }
                }
            }
        }
    }
}
