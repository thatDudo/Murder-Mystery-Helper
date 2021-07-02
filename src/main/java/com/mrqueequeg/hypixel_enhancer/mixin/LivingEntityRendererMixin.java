package com.mrqueequeg.hypixel_enhancer.mixin;

import com.mrqueequeg.hypixel_enhancer.config.Config;
import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
import com.mrqueequeg.hypixel_enhancer.debug.Logger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

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

    @Inject(at = @At("HEAD"), method = "hasLabel", cancellable = true)
    public void onShouldRenderName(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> info) {
        if (ConfigManager.getConfig().enabled) {
            Config config = ConfigManager.getConfig();
            if (config.murdermystery.isEnabled() && config.murdermystery.showNameTags) {
                Object t = (Object) livingEntity;
                if (t instanceof PlayerEntity && isRealPlayer((PlayerEntity)t)) {
                    //Logger.sendChatMessage(Text.of(Formatting.RED+"Name: "+((PlayerEntityRenderer)t)..getGameProfile().getName()+"; Shown: "+((PlayerEntity)t).getCustomName()));
                    info.setReturnValue(true);
                }
            }
        }
    }
}
