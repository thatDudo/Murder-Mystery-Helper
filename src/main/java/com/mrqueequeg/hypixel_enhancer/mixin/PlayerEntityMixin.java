package com.mrqueequeg.hypixel_enhancer.mixin;

import com.mojang.authlib.GameProfile;
import com.mrqueequeg.hypixel_enhancer.HypixelEnhancer;
import com.mrqueequeg.hypixel_enhancer.access.PlayerEntityMixinAccess;
import com.mrqueequeg.hypixel_enhancer.config.Config;
import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
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
    private boolean _isRealPlayer = false;
    private boolean _hasBow = false;

    @Override
    public boolean isMurder() {
        return _isMurder;
    }

    @Override
    public boolean isRealPlayer() {
        return _isRealPlayer;
    }

    @Override
    public boolean hasBow() {
        return _hasBow;
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    private void onInit(World world, BlockPos pos, float yaw, GameProfile profile, CallbackInfo info) {
        if (ConfigManager.getConfig().enabled) {
            _isMurder = Config.MurderMystery.markedMurders.contains(profile.getId());
            _hasBow = Config.MurderMystery.markedDetectives.contains(profile.getId());
        }
    }

    @Inject(at = @At("RETURN"), method = "tick")
    private void onTick(CallbackInfo info) {
        if (ConfigManager.getConfig().enabled) {
            PlayerEntity player = (PlayerEntity)(Object)this;
            // Has to be checked regularly
            _isRealPlayer = !player.isSleeping() && !player.isMainPlayer() && HypixelEnhancer.isPlayerInTabList(player);
        }
    }

    @Inject(at = @At("RETURN"), method = "getEquippedStack")
    private void onEquip(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> info) {
        if (ConfigManager.getConfig().enabled) {
            Config config = ConfigManager.getConfig();
            if (Config.MurderMystery.isActive() && !Config.roundHasEnded && !Config.MurderMystery.clientIsMurder) {
                if (!isMurder() && isRealPlayer()) {
                    Item holdItem = info.getReturnValue().getItem();
                    if (!hasBow() && holdItem == Items.BOW || holdItem == Items.ARROW) {
                        _hasBow = true;
                        Config.MurderMystery.markedDetectives.add(((PlayerEntity)(Object)this).getGameProfile().getId());
                    }
                    else if (Config.MurderMystery.isMurderItem(holdItem)) {
                        HypixelEnhancer.printChatMsg(new TranslatableText("message.murder_mystery.murder_marked", Formatting.RED+((PlayerEntity)(Object)this).getGameProfile().getName()));
                        _isMurder = true;
                        Config.MurderMystery.markedMurders.add(((PlayerEntity)(Object)this).getGameProfile().getId());
                    }
                }
            }
        }
    }
}
