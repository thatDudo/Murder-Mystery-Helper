package com.mrqueequeg.hypixel_enhancer.mixin;

import com.mrqueequeg.hypixel_enhancer.HypixelEnhancer;
import com.mrqueequeg.hypixel_enhancer.access.PlayerEntityMixinAccess;
import com.mrqueequeg.hypixel_enhancer.config.Config;
import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    // gets called on round start
    @Inject(at = @At("HEAD"), method = "joinWorld")
    private void onJoinWorld(ClientWorld world, CallbackInfo info) {
        if (ConfigManager.getConfig().enabled && !Config.onHypixelServer) {
            ServerInfo entry = ((MinecraftClient)(Object)this).getCurrentServerEntry();
            if (entry != null) {
                Config.onHypixelServer = entry.address.startsWith("mc.hypixel.");
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "hasOutline", cancellable = true)
    private void onHasOutline(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (ConfigManager.getConfig().enabled) {
            Config config = ConfigManager.getConfig();
            if (config.murdermystery.isEnabled()) {
                if (entity instanceof PlayerEntity && !entity.isSpectator() && !(entity instanceof ClientPlayerEntity)) {
                    if (((Config.MurderMystery.isMurder && config.murdermystery.murderMode)
                            || (((PlayerEntityMixinAccess)entity).isMurder() && config.murdermystery.innocentMode))) {
                        if (HypixelEnhancer.isRealPlayer((PlayerEntity)entity)) {
                            info.setReturnValue(true);
                        }
                    }
                }
            }
        }
    }
}
