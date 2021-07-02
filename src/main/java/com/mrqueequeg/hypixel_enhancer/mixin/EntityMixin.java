package com.mrqueequeg.hypixel_enhancer.mixin;

import com.mrqueequeg.hypixel_enhancer.config.Config;
import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    public boolean isRealPlayer(PlayerEntity player) {
        // ids of players are sometimes different from this player list
        //return MinecraftClient.getInstance().player.networkHandler.getPlayerUuids().contains(player.getGameProfile().getId());
        // using names
        for (PlayerListEntry entry : MinecraftClient.getInstance().player.networkHandler.getPlayerList()) {
            if (entry.getProfile().getName().equals(player.getGameProfile().getName())) {
                return true;
            }
        }
        return false;
    }

    @Inject(at = @At("HEAD"), method = "isGlowing", cancellable = true)
    private void onIsGlowing(CallbackInfoReturnable<Boolean> info) {
        if (ConfigManager.getConfig().enabled) {
            Config config = ConfigManager.getConfig();
            if (config.murdermystery.isEnabled() && config.murdermystery.innocentMode) {
                Object t = (Object) this;
                if (t instanceof PlayerEntity && !((PlayerEntity)t).isSpectator()) {
                    if (((Config.MurderMystery.isMurder && config.murdermystery.murderMode && !(t instanceof ClientPlayerEntity))
                            || Config.MurderMystery.markedMurders.contains(((PlayerEntity)t).getGameProfile().getId()))) {
                        if (isRealPlayer((PlayerEntity)t)) {
                            info.setReturnValue(true);
                        }
                    }
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getTeamColorValue", cancellable = true)
    private void onGetTeamColorValue(CallbackInfoReturnable<Integer> info) {
        if (ConfigManager.getConfig().enabled) {
            Config config = ConfigManager.getConfig();
            if (config.murdermystery.isEnabled() && config.murdermystery.innocentMode) {
                Object t = (Object) this;
                if (t instanceof PlayerEntity && Config.MurderMystery.markedMurders.contains(((PlayerEntity)t).getGameProfile().getId())) {
                    info.setReturnValue(0xFF1111);
                }
            }
        }
    }
}
